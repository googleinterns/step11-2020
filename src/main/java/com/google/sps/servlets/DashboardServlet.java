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

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.DataAccess;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.MentorMenteeRelation;
import com.google.sps.data.UserAccount;
import com.google.sps.util.ContextFields;
import com.google.sps.util.ErrorMessages;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet supports HTTP GET and returns an html page with a information about each of the
 * users that the currently logged in user is connected with.
 *
 * @author tquintanilla
 * @author guptamudit
 * @version 1.0
 * @param URLPatterns.DASHBOARD this servlet serves requests at /dashboard
 */
@WebServlet(urlPatterns = URLPatterns.DASHBOARD)
public class DashboardServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(DashboardServlet.class.getName());

  private DataAccess dataAccess;
  private Jinjava jinjava;
  private String dashboardMentorTemplate;
  private String dashboardMenteeTemplate;

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
              this.getClass().getResource(ResourceConstants.TEMPLATE_MENTOR_DASHBOARD),
              Charsets.UTF_8);
      dashboardMentorTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      LOG.severe(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_MENTOR_DASHBOARD));
    }
    try {
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_MENTEE_DASHBOARD),
              Charsets.UTF_8);
      dashboardMenteeTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      LOG.severe(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_MENTOR_DASHBOARD));
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, Object> context = dataAccess.getDefaultRenderingContext(URLPatterns.DASHBOARD);

    if (!(boolean) context.get(ContextFields.IS_LOGGED_IN)) {
      response.sendRedirect(URLPatterns.LANDING);
      return;
    }

    UserAccount currentUser = (UserAccount) context.get(ContextFields.CURRENT_USER);
    if (currentUser == null) {
      response.sendRedirect(URLPatterns.LANDING);
      return;
    }

    Collection<MentorMenteeRelation> connectedUsers =
        dataAccess.getMentorMenteeRelations(currentUser);
    context.put(ContextFields.MENTOR_MENTEE_RELATIONS, connectedUsers);

    String template =
        (boolean) context.get(ContextFields.IS_MENTOR)
            ? dashboardMentorTemplate
            : dashboardMenteeTemplate;

    String renderedTemplate = jinjava.render(template, context);

    response.setContentType(ServletUtils.CONTENT_HTML);
    response.getWriter().println(renderedTemplate);
  }
}
