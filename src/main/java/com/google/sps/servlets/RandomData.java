package com.google.sps.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import com.google.sps.util.RandomObjects;
import com.google.sps.util.ServletUtils;
import com.google.sps.util.URLPatterns;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;
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
@WebServlet("random")
public class RandomData extends HttpServlet {

  private Gson gson;
  private Collection<UserAccount> users;

  @Override
  public void init() {
    gson = new GsonBuilder().setDateFormat("MMM dd, yyyy, HH:mm:ss a").create();

  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Collection<Entity> entities;
    boolean success = false;
    if (seeded < users.size()) {
      entities =
          users.stream()
              .skip(seeded)
              .limit(1000)
              .map(UserAccount::convertToEntity)
              .collect(Collectors.toList());
      success = dataAccess.seed_db(entities);
      if (success) {
        seeded += 1000;
      }
    }

    response.setContentType(ServletUtils.CONTENT_JSON);
    response.getWriter().println("{\"seeded\":\n" + success + "}");
  }
}
