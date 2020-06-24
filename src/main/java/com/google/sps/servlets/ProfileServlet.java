// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.UserAccount;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that maintains the comment section. */
@WebServlet("/load-profile")
public final class ProfileServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String requestedUserID = getParameter(request, "userPath", "");
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect('/');
    } else {
      // Remember to explicitly supply the userId when the User instance is
      // created upon registration
      String userId = userService.getCurrentUser().getUserId();
      Query query =
          new Query("UserAccount")
              .setFilter(new Query.FilterPredicate("userID", Query.FilterOperator.EQUAL, userId));
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery result = datastore.prepare(query);
      Entity userEntity = result.asSingleEntity();
      String jsonString;
      if (userEntity.getProperty("userType" == 0) {
        jsonString = new Gson().toJson(new Mentor(userEntity));
      }
      else {
        jsonString = new Gson().toJson(new Mentee(userEntity));
      }
      response.setContentType("application/json;");
      response.getWriter().println(jsonString);
    }
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
