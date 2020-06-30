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
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.util.ResourceConstants;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/profile"})
public final class ProfileServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String requestedUserID = getParameter(request, "userID", "alice");

    UserService userService = UserServiceFactory.getUserService();
    JinjavaConfig config = new JinjavaConfig();
    Jinjava jinjava = new Jinjava(config);
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/landing");
    } else {
      String userId = userService.getCurrentUser().getUserId();
      Query query =
          new Query("UserAccount")
              .setFilter(
                  new Query.FilterPredicate("userID", Query.FilterOperator.EQUAL, requestedUserID));
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery result = datastore.prepare(query);
      Entity userEntity = result.asSingleEntity();
      Map<String, Object> context = new HashMap();
      context.put("url", "/profile?userID=" + requestedUserID);
      context.put("userType", (int) (userEntity.getProperty("userType")));
      context.put("browsingUserProfileURL", "/profile?userID=" + userId);
      if ((int) (userEntity.getProperty("userType")) == 0) {
        context.put("mentor", new Mentor(userEntity));
      } else {
        context.put("mentee", new Mentee(userEntity));
      }
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_PROFILE), Charsets.UTF_8);
      String renderedTemplate = jinjava.render(template, context);
      response.setContentType("text/html;");
      response.getWriter().println(renderedTemplate);
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
