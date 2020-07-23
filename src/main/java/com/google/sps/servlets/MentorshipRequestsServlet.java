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
import com.google.sps.data.Mentor;
import com.google.sps.data.MentorshipRequest;
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
 * This servlet supports HTTP GET and returns an html page with a information about each of the
 * mentees that wants help from the currently logged in mentor. The mentor can then accept or deny
 * these mentorship requests. Mentee users will be redirected to the /landing page. This servlet
 * supports HTTP POST for mentors approving/denying mentorship requests.
 *
 * @author tquintanilla
 * @author guptamudit
 * @version 1.0
 * @param URLPatterns.MENTORSHIP_REQUESTS this servlet serves requests at /mentorship-requests
 */
@WebServlet(urlPatterns = URLPatterns.MENTORSHIP_REQUESTS)
public class MentorshipRequestsServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(MentorshipRequestsServlet.class.getName());

  private static final String ACCEPT = "accept";
  private static final String DENY = "deny";

  private DataAccess dataAccess;
  private Jinjava jinjava;
  private String mentorshipRequestTemplate;

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
              this.getClass().getResource(ResourceConstants.TEMPLATE_MENTORSHIP_REQUESTS),
              Charsets.UTF_8);
      mentorshipRequestTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      LOG.severe(
          ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_MENTORSHIP_REQUESTS));
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(ServletUtils.CONTENT_HTML);

    if (mentorshipRequestTemplate == null) {
      response.setStatus(500);
      return;
    }

    User user = dataAccess.getCurrentUser();
    if (user != null) {
      Mentor mentor = dataAccess.getMentor(user.getUserId());
      if (mentor != null) {
        response.setContentType(ServletUtils.CONTENT_HTML);
        Map<String, Object> context =
            dataAccess.getDefaultRenderingContext(URLPatterns.MENTORSHIP_REQUESTS);
        context.put(ContextFields.MENTORSHIP_REQUESTS, dataAccess.getIncomingRequests(mentor));
        String renderTemplate = jinjava.render(mentorshipRequestTemplate, context);
        response.getWriter().println(renderTemplate);
        return;
      }
    }
    response.sendRedirect(URLPatterns.LANDING);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String requestKey = ServletUtils.getParameter(request, ParameterConstants.REQUEST_ID, "");
    final String choice = ServletUtils.getParameter(request, ParameterConstants.CHOICE, "");

    Long requestDatastoreKey = null;
    try {
      requestDatastoreKey = Long.parseLong(requestKey);
    } catch (NumberFormatException e) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    if (requestDatastoreKey == null || (!choice.equals(ACCEPT) && !choice.equals(DENY))) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    User user = dataAccess.getCurrentUser();
    if (user == null) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    Mentor mentor = dataAccess.getMentor(user.getUserId());
    if (mentor == null) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    MentorshipRequest mentorshipRequest = dataAccess.getMentorshipRequest(requestDatastoreKey);
    if (mentorshipRequest == null) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    if (choice.equals(ACCEPT)) {
      dataAccess.approveRequest(mentorshipRequest);
    } else if (choice.equals(DENY)) {
      dataAccess.denyRequest(mentorshipRequest);
    }

    writeJsonSuccessToResponse(response, true);
  }

  private void writeJsonSuccessToResponse(HttpServletResponse response, boolean success)
      throws IOException {
    response.setContentType(ServletUtils.CONTENT_JSON);
    response.getWriter().println("{\"success\": " + success + "}");
  }
}
