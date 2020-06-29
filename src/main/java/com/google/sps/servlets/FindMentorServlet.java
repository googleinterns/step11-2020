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

import com.google.appengine.api.users.User;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.DummyDataAccess;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MentorshipRequest;
import com.google.sps.data.UserAccount;
import com.google.sps.data.UserType;
import com.google.sps.util.ErrorMessages;
import com.google.sps.util.ResourceConstants;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = URLPatterns.FIND_MENTOR)
public class FindMentorServlet extends HttpServlet {
  private Jinjava jinjava;
  private String findMentorTemplate;

  private DummyDataAccess dataAccess;

  @Override
  public void init() {
    dataAccess = new DummyDataAccess();

    JinjavaConfig config = new JinjavaConfig();
    jinjava = new Jinjava(config);
    try {
      jinjava.setResourceLocator(
          new FileLocator(
              new File(this.getClass().getResource(ResourceConstants.TEMPLATES).toURI())));
    } catch (URISyntaxException|FileNotFoundException e) {
      System.err.println(ErrorMessages.TEMPLATES_DIRECTORY_NOT_FOUND);
    }

    Map<String, Object> context = new HashMap<>();
    context.put(URLPatterns.URL, URLPatterns.FIND_MENTOR);

    try {
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_FIND_MENTOR), Charsets.UTF_8);
      findMentorTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      System.err.println(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_FIND_MENTOR));
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    User user = dataAccess.getCurrentUser();
    if (user != null) {
      Mentee mentee = dataAccess.getMentee(user.getUserId());
      if (mentee != null) {
        response.setContentType("text/html;");

        Map<String, Object> context = new HashMap<>();

        Collection<Mentor> relatedMentors = dataAccess.getRelatedMentors(null);
        context.put("mentors", relatedMentors);

        String renderedTemplate = jinjava.render(findMentorTemplate, context);

        response.getWriter().println(renderedTemplate);
        return;
      }
    }
    response.sendRedirect("/landing");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String mentorKey = request.getParameter("mentorID");
    final String choice = request.getParameter("choice");

    boolean success = false;

    User user = dataAccess.getCurrentUser();
    if (user != null) {
      Mentee mentee = dataAccess.getMentee(user.getUserId());
      if (mentee != null) {
        MentorshipRequest mentorshipRequest = new MentorshipRequest(Long.parseLong(mentorKey), mentee.getDatastoreKey());
        dataAccess.publishRequest(mentorshipRequest);
        success = true;
      }
    }

    response.setContentType("application/json;");
    response.getWriter().println("{\"success\": " + success + "}");
  }
}
