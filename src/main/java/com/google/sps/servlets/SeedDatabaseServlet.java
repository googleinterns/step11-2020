package com.google.sps.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.UserAccount;
import com.google.sps.util.DummyDataConstants;
import com.google.sps.util.ErrorMessages;
import com.google.sps.util.ParameterConstants;
import com.google.sps.util.ResourceConstants;
import com.google.sps.util.ServletUtils;
import com.google.sps.util.URLPatterns;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Seeds the database with based on a large input dataset in JSON format. Upon calling HTTP GET,
 * this servlet attempts to seed the database and returns a JSON object representing the operations
 * success.
 *
 * @author sylviaziyuz
 * @author guptamudit
 * @author tquintanilla
 * @version 1.0
 * @param URLPatterns.SEED_DB this servlet serves requests at /seed-db
 */
@WebServlet(URLPatterns.SEED_DB)
public class SeedDatabaseServlet extends HttpServlet {
  private static final int FAKE_USER_COUNT = 502;

  private DatastoreAccess dataAccess;
  private Gson gson;
  private Collection<UserAccount> users;
  private Collection<Entity> entities;

  @Override
  public void init() {
    dataAccess = new DatastoreAccess();
    gson = new Gson();
    users = new ArrayList<>(FAKE_USER_COUNT);

    JsonParser jsonParser = new JsonParser();
    String jsonData = "";
    try {
      jsonData =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.DUMMY_DATA_USERS), Charsets.UTF_8);
    } catch (IOException e) {
      System.err.println(ErrorMessages.SEEDING_FAILED);
      return;
    }

    JsonObject jsonObject = new JsonParser().parse(jsonData).getAsJsonObject();
    JsonArray usersJson = jsonObject.getAsJsonArray(DummyDataConstants.USERS);

    for (JsonElement element : usersJson) {
      JsonObject user = element.getAsJsonObject();
      if (user.get(ParameterConstants.USER_TYPE)
          .getAsString()
          .equals(DummyDataConstants.MENTOR_CAPS)) {
        users.add(gson.fromJson(user, Mentor.class));
      } else {
        users.add(gson.fromJson(user, Mentee.class));
      }
    }

    entities = users.stream().map(UserAccount::convertToEntity).collect(Collectors.toList());
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    boolean success = dataAccess.seed_db(entities);

    response.setContentType(ServletUtils.CONTENT_JSON);
    response.getWriter().println("{\"seeded\":\n" + success + "}");
  }
}
