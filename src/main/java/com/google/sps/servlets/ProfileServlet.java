// Copyright 2020 Google LLC
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

import com.google.appengine.api.users.User;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.DataAccess;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.UserAccount;
import com.google.sps.util.ContextFields;
import com.google.sps.util.ErrorMessages;
import com.google.sps.util.ParameterConstants;
import com.google.sps.util.ResourceConstants;
import com.google.sps.util.ServletUtils;
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
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet supports HTTP GET and returns an html page with a summary of a user's (not
 * necessarily of the currently logged in user) data. If a user is viewing their own profile, they
 * may access the questionnaire page from here to edit their information.
 *
 * @author sylviaziyuz
 * @author guptamudit
 * @version 1.0
 * @param URLPatterns.PROFILE this servlet serves requests at /profile
 */
@WebServlet(urlPatterns = URLPatterns.PROFILE)
public final class ProfileServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(ProfileServlet.class.getName());

  private String profileTemplate;
  private Jinjava jinjava;
  private DataAccess dataAccess;

  @Override
  public void init() {
    dataAccess = new DatastoreAccess();

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

    String requestedUserID = ServletUtils.getParameter(request, ParameterConstants.USER_ID, userID);
    UserAccount requestedUserAccount =
        requestedUserID.equals(userID) ? currentUserAccount : dataAccess.getUser(requestedUserID);
    if (requestedUserAccount == null) {
      response.sendRedirect(URLPatterns.PROFILE);
      return;
    }
    Map<String, Object> context = dataAccess.getDefaultRenderingContext(URLPatterns.PROFILE);
    context.put(ContextFields.PROFILE_USER, requestedUserAccount);

    String renderedTemplate = jinjava.render(profileTemplate, context);
    response.setContentType(ServletUtils.CONTENT_HTML);
    response.getWriter().println(renderedTemplate);
  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    User user = dataAccess.getCurrentUser();
    if (user == null) {
      ServletUtils.writeJsonSuccessToResponse(response, false);
      return;
    }
    UserAccount userAccount = dataAccess.getUser(user.getUserId());
    if (userAccount == null) {
      ServletUtils.writeJsonSuccessToResponse(response, false);
      return;
    }

    boolean success = dataAccess.deleteUser(userAccount);

    ServletUtils.writeJsonSuccessToResponse(response, success);
  }
}
