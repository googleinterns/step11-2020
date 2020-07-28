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
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MentorshipRequest;
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
 * This servlet supports HTTP GET and returns an html page with a information about each of the
 * mentors that may be similar to the currently logged in mentee. If the mentor finds one of the
 * mentors relatable, they can send them a mentorship request. This servlet supports HTTP POST for
 * mentees to send requests to or dislike mentors.
 *
 * @author guptamudit
 * @version 1.0
 * @param URLPatterns.FIND_MENTOR this servlet serves requests at /find-mentor
 */
@WebServlet(urlPatterns = URLPatterns.FIND_MENTOR)
public class FindMentorServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(FindMentorServlet.class.getName());

  private static final String SEND = "sendRequest";
  private static final String DISLIKE = "dislikeMentor";

  private Jinjava jinjava;
  private String findMentorTemplate;

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
              this.getClass().getResource(ResourceConstants.TEMPLATE_FIND_MENTOR), Charsets.UTF_8);
      findMentorTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      LOG.severe(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_FIND_MENTOR));
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, Object> context = dataAccess.getDefaultRenderingContext(URLPatterns.FIND_MENTOR);

    if (!(boolean) context.get(ContextFields.IS_LOGGED_IN)) {
      response.sendRedirect(URLPatterns.LANDING);
      return;
    }

    UserAccount currentUser = (UserAccount) context.get(ContextFields.CURRENT_USER);
    if (currentUser == null || !(boolean) context.get(ContextFields.IS_MENTEE)) {
      response.sendRedirect(URLPatterns.LANDING);
      return;
    }
    String renderedTemplate = jinjava.render(findMentorTemplate, context);
    response.setContentType(ServletUtils.CONTENT_HTML);
    response.getWriter().println(renderedTemplate);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String mentorKey = ServletUtils.getParameter(request, ParameterConstants.MENTOR_ID, "");
    final String choice = ServletUtils.getParameter(request, ParameterConstants.CHOICE, "");

    boolean success = false;

    Long mentorDatastoreKey = null;
    try {
      mentorDatastoreKey = Long.parseLong(mentorKey);
    } catch (NumberFormatException e) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    if (mentorDatastoreKey == null || (!choice.equals(SEND) && !choice.equals(DISLIKE))) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    User user = dataAccess.getCurrentUser();
    if (user == null) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    Mentee mentee = dataAccess.getMentee(user.getUserId());
    if (mentee == null) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    Mentor mentor = dataAccess.getMentor(mentorDatastoreKey);
    if (mentor == null) {
      writeJsonSuccessToResponse(response, false);
      return;
    }
    if (choice.equals(SEND)) {
      MentorshipRequest mentorshipRequest =
          new MentorshipRequest(mentor.getDatastoreKey(), mentee.getDatastoreKey());
      dataAccess.publishRequest(mentorshipRequest);
      dataAccess.requestMentor(mentee, mentor);
    } else if (choice.equals(DISLIKE)) {
      dataAccess.dislikeMentor(mentee, mentor);
    }

    writeJsonSuccessToResponse(response, true);
  }

  private void writeJsonSuccessToResponse(HttpServletResponse response, boolean success)
      throws IOException {
    response.setContentType(ServletUtils.CONTENT_JSON);
    response.getWriter().println("{\"success\": " + success + "}");
  }
}
