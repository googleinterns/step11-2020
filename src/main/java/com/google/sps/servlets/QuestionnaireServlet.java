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
import com.google.sps.util.ErrorMessages;
import com.google.sps.util.ResourceConstants;
import com.google.sps.util.ParameterConstants;
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
  private String formType;
  private DummyDataAccess dataAccess;

  @Override
  public void init() {
    JinjavaConfig config = new JinjavaConfig();
    jinjava = new Jinjava(config);
    formType = "";
    try {
      jinjava.setResourceLocator(
          new FileLocator(
              new File(this.getClass().getResource(ResourceConstants.TEMPLATES).toURI())));
    } catch (URISyntaxException | FileNotFoundException e) {
      System.err.println("templates dir not found!");
    }

    Map<String, Object> context = selectionListsForFrontEnd();
    context.put(URLPatterns.URL, URLPatterns.QUESTIONNAIRE);

    try {
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_QUESTIONNAIRE),
              Charsets.UTF_8);
      questionnaireTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      System.err.println(
          ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_QUESTIONNAIRE));
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
    formType = request.getParameter("formType");
    if (formType != null && (formType.equals(MENTOR) || formType.equals(MENTEE))) {
      Map<String, Object> context = new HashMap<>();
      context.put("isMentor", formType.equals(MENTOR));
      String renderTemplate = jinjava.render(questionnaireTemplate, context);
      response.getWriter().println(renderTemplate);
    } else {
      System.err.println(ErrorMessages.INVALID_PARAMATERS);
      response.sendRedirect(URLPatterns.LANDING);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getParameter(request, ParameterConstants.NAME, "John Doe");
    Date dateOfBirth = getParameter(request, ParameterConstants.DATE_OF_BIRTH, "2000-01-01");
    Country country = getParameter(request, ParameterConstants.COUNTRY, Country.US);
    TimeZone timeZone = getParameter(request, ParameterConstants.TIMEZONE, "est");
    Language language = getParameter(request, ParameterConstants.LANGUAGE, Language.ENGLISH);
    ArrayList<Ethnicity> ethnicities = getParameter(request, ParameterConstants.ETHNICITY, "");
    String ethnicityOther = getParameter(request, ParameterConstants.ETHNICITY_OTHER, "")
    Gender gender = getParameter(request, ParameterConstants.GENDER, "");
    String genderOther = getParameter(request, ParameterConstants.GENDER_OTHER, "");
    Education educationLevel = getParameter(request, ParameterConstants.EDUCATION_LEVEL, "");
    String educationLevelOther = getParameter(request, ParameterConstants.EDUCATION_LEVEL_OTHER, "");
    boolean firstGen = getParameter(request, ParameterConstants.FIRST_GEN, "no");
    boolean lowIncome = getParameter(request, ParameterConstants.LOW_INCOME, "no");
    MentorType mentorType = getParameter(request, ParameterConstants.MENTOR_TYPE, MentorType.TUTOR);
    String description = getParameter(request, ParameterConstants.DESCRIPTION, "");

    if (formType.equals(MENTEE)) {
      MeetingFrequency desiredMeetingFrequency = getParameter(request, ParameterConstants.MENTEE_DESIRED_MEETING_FREQUENCY, MeetingFrequency.BIWEEKLY);
      Topic goal = getParameter(request, ParameterConstants.MENTEE_GOAL, "");
      dataAccess.saveUser(new Mentee());
      response.sendRedirect(URLPatterns.FIND_MENTOR);

    } else {
      ArrayList<Topic> focusList = getParameter(request, ParameterConstants.MENTOR_FOCUS_LIST);
      dataAccess.saveUser(new Mentor());
      response.sendRedirect(URLPatterns.PROFILE);
    }
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      value = defaultValue;
    }
    return value;
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
