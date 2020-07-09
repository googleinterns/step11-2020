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

import static java.lang.Math.toIntExact;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.DataAccess;
import com.google.sps.data.DummyDataAccess;
import com.google.sps.data.UserAccount;
import com.google.sps.data.UserType;
import com.google.sps.util.ContextFields;
import com.google.sps.util.ErrorMessages;
import com.google.sps.util.ParameterConstants;
import com.google.sps.util.ResourceConstants;
import com.google.sps.util.URLPatterns;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.loader.FileLocator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = URLPatterns.PROFILE)
public final class ProfileServlet extends HttpServlet {

  private String profileTemplate;
  private Jinjava jinjava;
  private DataAccess dataAccess;

  @Override
  public void init() {
    dataAccess = new DummyDataAccess();

    JinjavaConfig config = new JinjavaConfig();
    jinjava = new Jinjava(config);
    try {
      jinjava.setResourceLocator(
          new FileLocator(
              new File(this.getClass().getResource(ResourceConstants.TEMPLATES).toURI())));
    } catch (URISyntaxException | FileNotFoundException e) {
      System.err.println(ErrorMessages.TEMPLATES_DIRECTORY_NOT_FOUND);
    }

    Map<String, Object> context = new HashMap<>();

    try {
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_PROFILE), Charsets.UTF_8);
      profileTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      System.err.println(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_PROFILE));
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    User user = dataAccess.getCurrentUser();
    if (user == null) {
      response.sendRedirect(URLPatterns.LANDING);
      return;
    }
    String userID = user.getUserId();

    UserAccount userAccount = dataAccess.getUser(userID);
    if (userAccount == null) {
      response.sendRedirect(URLPatterns.LANDING);
      return;
    }

    String requestedUserID = getParameter(request, ParameterConstants.USER_ID, userID);
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT)
            .setFilter(
                new Query.FilterPredicate(
                    ParameterConstants.USER_ID, Query.FilterOperator.EQUAL, requestedUserID));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery result = datastore.prepare(query);
    Entity userEntity = result.asSingleEntity();
    if (userEntity == null) {
      response.sendRedirect(URLPatterns.PROFILE);
      return;
    }
    Map<String, Object> context = dataAccess.getDefaultRenderingContext(URLPatterns.PROFILE);
    context.put(
        ParameterConstants.USER_TYPE,
        UserType.values()[toIntExact((long) (userEntity.getProperty(ParameterConstants.USER_TYPE)))]
                == UserType.MENTOR
            ? "mentor"
            : "mentee");
    context.put(ContextFields.PROFILE_USER, UserAccount.fromEntity(userEntity));

    String renderedTemplate = jinjava.render(profileTemplate, context);
    response.setContentType("text/html;");
    response.getWriter().println(renderedTemplate);
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null || value == "") {
      return defaultValue;
    }
    return value;
  }
}
