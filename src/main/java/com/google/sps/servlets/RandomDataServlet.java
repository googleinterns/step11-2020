package com.google.sps.servlets;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.sps.data.UserAccount;
import com.google.sps.util.RandomObjects;
import com.google.sps.util.ServletUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generates random data and returns it as JSON data. This servlet will only handle responses in the
 * development environment to avoid leaking the internal object structure of the UserAccount to
 * external users. Upon calling HTTP GET, this servlet will generate 10000 fake UserAccounts and
 * send them as a JSON response to the caller. This servlet does not support HTTP POST.
 *
 * @author guptamudit
 * @version 1.0
 * @param "random" this servlet serves requests at /random
 */
@WebServlet("random")
public class RandomDataServlet extends HttpServlet {

  private Gson gson;

  @Override
  public void init() {
    gson = new GsonBuilder().setDateFormat("MMM dd, yyyy, HH:mm:ss a").create();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
      Collection<UserAccount> users =
          Stream.generate(RandomObjects::randomMentee).limit(5000).collect(Collectors.toList());
      Stream.generate(RandomObjects::randomMentor).limit(5000).forEach(users::add);

      response.setContentType(ServletUtils.CONTENT_JSON);
      response.getWriter().println("{\"users\":\n" + gson.toJson(users) + "}");
    }
  }
}
