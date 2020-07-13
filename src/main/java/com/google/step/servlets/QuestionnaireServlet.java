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

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.step.data.Country;
import com.google.step.data.DataAccess;
import com.google.step.data.DummyDataAccess;
import com.google.step.data.EducationLevel;
import com.google.step.data.Ethnicity;
import com.google.step.data.Gender;
import com.google.step.data.Language;
import com.google.step.data.MeetingFrequency;
import com.google.step.data.Mentee;
import com.google.step.data.Mentor;
import com.google.step.data.MentorType;
import com.google.step.data.TimeZoneInfo;
import com.google.step.data.Topic;
import com.google.step.data.UserAccount;
import com.google.step.data.UserType;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = URLPatterns.QUESTIONNAIRE)
public class QuestionnaireServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(QuestionnaireServlet.class.getName());

  private static final String MENTOR = "mentor";
  private static final String MENTEE = "mentee";

  private String questionnaireTemplate;
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

    Map<String, Object> context = selectionListsForFrontEnd();

    try {
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_QUESTIONNAIRE),
              Charsets.UTF_8);
      questionnaireTemplate = jinjava.render(template, context);
    } catch (IOException e) {
      LOG.severe(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_QUESTIONNAIRE));
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");

    if (questionnaireTemplate == null) {
      response.setStatus(500);
      return;
    }
    String formType = request.getParameter(ContextFields.FORM_TYPE);
    if (formType != null
        && (formType.equalsIgnoreCase(MENTOR) || formType.equalsIgnoreCase(MENTEE))) {
      Map<String, Object> context =
          dataAccess.getDefaultRenderingContext(URLPatterns.QUESTIONNAIRE);
      context.put(ContextFields.FORM_TYPE, formType.toLowerCase());
      String renderTemplate = jinjava.render(questionnaireTemplate, context);
      response.getWriter().println(renderTemplate);
    } else {
      LOG.warning(ErrorMessages.INVALID_PARAMATERS);
      response.sendRedirect(URLPatterns.LANDING);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserAccount user = constructNewUserFromRequest(request);
    dataAccess.createUser(user);
    response.getWriter().println(new Gson().toJson(user));
    if (user.getUserType().equals(UserType.MENTEE)) {
      response.sendRedirect(URLPatterns.FIND_MENTOR);
    } else {
      response.sendRedirect(URLPatterns.PROFILE);
    }
  }

  private UserAccount constructNewUserFromRequest(HttpServletRequest request) {
    UserType userType = UserType.valueOf(getParameter(request, ContextFields.FORM_TYPE, "MENTEE"));
    String name = getParameter(request, ParameterConstants.NAME, "John Doe");
    Date dateOfBirth;
    try {
      dateOfBirth =
          new SimpleDateFormat("yyyy-MM-dd")
              .parse(getParameter(request, ParameterConstants.DATE_OF_BIRTH, "2000-01-01"));
    } catch (ParseException e) {
      dateOfBirth = new Date();
      LOG.warning(ErrorMessages.BAD_DATE_PARSE);
    }
    Country country =
        Country.valueOf(getParameter(request, ParameterConstants.COUNTRY, Country.US.toString()));
    TimeZoneInfo timeZone =
        new TimeZoneInfo(
            TimeZone.getTimeZone(getParameter(request, ParameterConstants.TIMEZONE, "est")));
    Language language =
        Language.valueOf(
            getParameter(request, ParameterConstants.LANGUAGE, Language.EN.toString()));

    ArrayList<Ethnicity> ethnicities = new ArrayList<>();
    String ethnicityString = getParameter(request, ParameterConstants.ETHNICITY, "UNSPECIFIED");
    try {
      for (String ethnicity : ethnicityString.split(", ")) {
        ethnicities.add(Ethnicity.valueOf(ethnicity));
      }
    } catch (IllegalArgumentException e) {
      LOG.warning(ErrorMessages.INVALID_PARAMATERS);
    }

    String ethnicityOther = getParameter(request, ParameterConstants.ETHNICITY_OTHER, "");
    Gender gender = Gender.valueOf(getParameter(request, ParameterConstants.GENDER, "UNSPECIFIED"));
    String genderOther = getParameter(request, ParameterConstants.GENDER_OTHER, "");
    EducationLevel educationLevel =
        EducationLevel.valueOf(
            getParameter(request, ParameterConstants.EDUCATION_LEVEL, "UNSPECIFIED"));
    String educationLevelOther =
        getParameter(request, ParameterConstants.EDUCATION_LEVEL_OTHER, "");
    boolean firstGen =
        Boolean.parseBoolean(getParameter(request, ParameterConstants.FIRST_GEN, "false"));
    boolean lowIncome =
        Boolean.parseBoolean(getParameter(request, ParameterConstants.LOW_INCOME, "false"));
    MentorType mentorType =
        MentorType.valueOf(
            getParameter(request, ParameterConstants.MENTOR_TYPE, MentorType.TUTOR.toString()));
    String description = getParameter(request, ParameterConstants.DESCRIPTION, "");

    if (userType.equals(UserType.MENTEE)) {
      MeetingFrequency desiredMeetingFrequency =
          MeetingFrequency.valueOf(
              getParameter(
                  request,
                  ParameterConstants.MENTEE_DESIRED_MEETING_FREQUENCY,
                  MeetingFrequency.WEEKLY.toString()));
      Topic goal =
          Topic.valueOf(
              getParameter(request, ParameterConstants.MENTEE_GOAL, Topic.OTHER.toString()));
      return (new Mentee.Builder())
          .name(name)
          .userID(dataAccess.getCurrentUser().getUserId())
          .email(dataAccess.getCurrentUser().getEmail())
          .userType(userType)
          .dateOfBirth(dateOfBirth)
          .country(country)
          .language(language)
          .timezone(timeZone)
          .ethnicityList(ethnicities)
          .ethnicityOther(ethnicityOther)
          .gender(gender)
          .genderOther(genderOther)
          .firstGen(firstGen)
          .lowIncome(lowIncome)
          .educationLevel(educationLevel)
          .educationLevelOther(educationLevelOther)
          .description(description)
          .goal(goal)
          .desiredMeetingFrequency(desiredMeetingFrequency)
          .desiredMentorType(mentorType)
          .build();

    } else {
      ArrayList<Topic> focusList = new ArrayList<>();
      String focusListString =
          getParameter(request, ParameterConstants.MENTOR_FOCUS_LIST, Topic.OTHER.toString());
      try {
        for (String focus : focusListString.split(", ")) {
          focusList.add(Topic.valueOf(focus));
        }
      } catch (IllegalArgumentException e) {
        LOG.warning(ErrorMessages.INVALID_PARAMATERS);
      }

      return (new Mentor.Builder())
          .name(name)
          .userID(dataAccess.getCurrentUser().getUserId())
          .email(dataAccess.getCurrentUser().getEmail())
          .dateOfBirth(dateOfBirth)
          .userType(userType)
          .country(country)
          .language(language)
          .timezone(timeZone)
          .ethnicityList(ethnicities)
          .ethnicityOther(ethnicityOther)
          .gender(gender)
          .genderOther(genderOther)
          .firstGen(firstGen)
          .lowIncome(lowIncome)
          .educationLevel(educationLevel)
          .educationLevelOther(educationLevelOther)
          .description(description)
          .mentorType(mentorType)
          .visibility(true)
          .focusList(focusList)
          .build();
    }
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null || value.equals("")) {
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
