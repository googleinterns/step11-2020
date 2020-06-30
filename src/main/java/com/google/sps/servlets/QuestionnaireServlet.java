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

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.Country;
import com.google.sps.data.EducationLevel;
import com.google.sps.data.Ethnicity;
import com.google.sps.data.Gender;
import com.google.sps.data.Language;
import com.google.sps.data.MeetingFrequency;
import com.google.sps.data.MentorType;
import com.google.sps.data.TimeZoneInfo;
import com.google.sps.data.Topic;
import com.google.sps.util.ResourceConstants;
import com.google.sps.util.ErrorMessages;
import com.google.sps.util.URLPatterns;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.loader.FileLocator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = URLPatterns.QUESTIONNAIRE)
public class QuestionnaireServlet extends HttpServlet {
  private static final String MENTOR = "mentor";
  private static final String MENTEE = "mentee";

  private String questionnaireTemplate;
  private Jinjava jinjava;

  @Override
  public void init() {
    JinjavaConfig config = new JinjavaConfig();
    jinjava = new Jinjava(config);
    try {
      jinjava.setResourceLocator(
          new FileLocator(
              new File(this.getClass().getResource(ResourceConstants.TEMPLATES).toURI())));
    } catch (URISyntaxException | FileNotFoundException e) {
      System.err.println("templates dir not found!");
    }

    Map<String, Object> context = selectionListsForFrontEnd();
    context.put("url", "/");

    try {
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_QUESTIONNAIRE), Charsets.UTF_8);
      questionnaireTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      System.err.println(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_QUESTIONNAIRE));
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("REQUEST AT: " + request.getServletPath());
    response.setContentType("text/html;");

    if (questionnaireTemplate == null) {
      response.setStatus(500);
      return;
    }
    String formType = request.getParameter("formType");
    System.out.println(formType);
    if(formType != null ) {
      Map<String, Object> context = new HashMap<>();

      context.put("isMentor", true);
      String renderTemplate = jinjava.render(questionnaireTemplate, context);
      response.getWriter().println(renderTemplate);
    } else {
      System.err.println("insufficient or invalid parameters");
      response.sendRedirect("/landing");
    }
  }

  private Map<String, Object> selectionListsForFrontEnd() {
    Map<String, Object> map = new HashMap<>();
    map.put("countries", Country.values());
    map.put("ethnicities", Ethnicity.values());
    map.put("genders", Gender.values());
    map.put("languages", Language.values());
    map.put("mentorTypes", MentorType.values());

    map.put(
        "timezones",
        TimeZoneInfo.getListOfNamesToDisplay(
            Arrays.asList(TimeZone.getAvailableIDs()).stream()
                .filter(strID -> strID.toUpperCase().equals(strID))
                .map(strID -> TimeZone.getTimeZone(strID))
                .collect(Collectors.toList())));
    map.put("educationLevels", EducationLevel.values());
    map.put("topics", Topic.values());
    map.put("meetingFrequencies", MeetingFrequency.values());
    return map;
  }
}
