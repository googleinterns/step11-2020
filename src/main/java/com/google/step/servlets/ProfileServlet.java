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

package com.google.step.servlets;

import com.google.appengine.api.users.User;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.step.data.DataAccess;
import com.google.step.data.DummyDataAccess;
import com.google.step.data.UserAccount;
import com.google.step.util.ContextFields;
import com.google.step.util.ErrorMessages;
import com.google.step.util.ParameterConstants;
import com.google.step.util.ResourceConstants;
import com.google.step.util.URLPatterns;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.loader.FileLocator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = URLPatterns.PROFILE)
public final class ProfileServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(ProfileServlet.class.getName());

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
      LOG.severe(ErrorMessages.TEMPLATES_DIRECTORY_NOT_FOUND);
    }

    Map<String, Object> context = new HashMap<>();

    try {
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_PROFILE), Charsets.UTF_8);
      profileTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      LOG.severe(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_PROFILE));
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

    UserAccount currentUserAccount = dataAccess.getUser(userID);
    if (currentUserAccount == null) {
      response.sendRedirect(URLPatterns.LANDING);
      return;
    }

    String requestedUserID = getParameter(request, ParameterConstants.USER_ID, userID);
    UserAccount requestedUserAccount =
        requestedUserID.equals(userID) ? currentUserAccount : dataAccess.getUser(requestedUserID);
    if (requestedUserAccount == null) {
      response.sendRedirect(URLPatterns.PROFILE);
      return;
    }
    Map<String, Object> context = dataAccess.getDefaultRenderingContext(URLPatterns.PROFILE);
    context.put(ContextFields.PROFILE_USER, requestedUserAccount);

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
