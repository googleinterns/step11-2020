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

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.sps.data.Country;
import com.google.sps.data.DataAccess;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.EducationLevel;
import com.google.sps.data.Ethnicity;
import com.google.sps.data.Gender;
import com.google.sps.data.Language;
import com.google.sps.data.MeetingFrequency;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MentorType;
import com.google.sps.data.TimeZone;
import com.google.sps.data.Topic;
import com.google.sps.data.UserAccount;
import com.google.sps.data.UserType;
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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet supports HTTP GET and returns an html page with a series of questions about a user's
 * demographics and goals for mentorship. This questionnaire is for people to sign up on the
 * mentor-matching platform. This servlet supports HTTP POST for users to submit the form and
 * create/update their profiles on the platform.
 *
 * @author tquintanilla
 * @author guptamudit
 * @version 1.0
 * @param URLPatterns.QUESTIONNAIRE this servlet serves requests at /questionnaire
 */
@WebServlet(urlPatterns = URLPatterns.QUESTIONNAIRE)
public class QuestionnaireServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(QuestionnaireServlet.class.getName());

  private static final String MENTOR = "mentor";
  private static final String MENTEE = "mentee";

  private String questionnaireTemplate;
  private Jinjava jinjava;
  private DataAccess dataAccess;

  public QuestionnaireServlet() {
    dataAccess = new DatastoreAccess();
  }

  public QuestionnaireServlet(
      UserService userService,
      DatastoreService datastoreService,
      BlobstoreService blobstoreService) {
    dataAccess =
        DatastoreAccess.newBuilder()
            .userService(userService)
            .datastoreService(datastoreService)
            .blobstoreService(blobstoreService)
            .build();
  }

  @Override
  public void init() {
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
    response.setContentType(ServletUtils.CONTENT_HTML);

    if (questionnaireTemplate == null) {
      response.setStatus(500);
      return;
    }

    Map<String, Object> context = dataAccess.getDefaultRenderingContext(URLPatterns.QUESTIONNAIRE);
    UserAccount currentUser = (UserAccount) context.get(ContextFields.CURRENT_USER);
    String formType =
        currentUser == null
            ? ServletUtils.getParameter(request, ParameterConstants.FORM_TYPE, "").toLowerCase()
            : currentUser.getUserType().getTitle().toLowerCase();

    if (formType.equals(MENTOR) || formType.equals(MENTEE)) {
      BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
      String uploadUrl = blobstoreService.createUploadUrl(URLPatterns.QUESTIONNAIRE);
      try {
        uploadUrl = new URL(uploadUrl).getPath();
      } catch (MalformedURLException e) {
      }
      context.put(ContextFields.FORM_TYPE, formType.toLowerCase());
      context.put(ContextFields.QUESTIONNAIRE_SUBMIT_URL, uploadUrl);
      context.put("ethnicities", EnumSet.complementOf(EnumSet.of(Ethnicity.UNSPECIFIED)));
      context.put("topics", Topic.valuesSorted());
      String renderTemplate = jinjava.render(questionnaireTemplate, context);
      response.getWriter().println(renderTemplate);
    } else {
      LOG.warning(ErrorMessages.INVALID_PARAMATERS);
      response.sendRedirect(URLPatterns.LANDING);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    User authUser = dataAccess.getCurrentUser();
    if (authUser == null) {
      return;
    }
    UserAccount user = constructNewUserFromRequest(request);
    response.getWriter().println(new Gson().toJson(user));
    boolean infoAdded;
    UserAccount oldUser = dataAccess.getUser(authUser.getUserId());
    if (oldUser == null) {
      infoAdded = dataAccess.createUser(user);
    } else {
      String oldProfilePicKey = oldUser.getProfilePicBlobKey();
      oldUser.copyProfileData(user);
      if (oldUser.getProfilePicBlobKey() != null && !oldUser.getProfilePicBlobKey().equals(oldProfilePicKey)) {
        dataAccess.deleteBlob(oldProfilePicKey);
      }
      infoAdded = dataAccess.updateUser(oldUser);
    }
    if (infoAdded) {
      if (user.getUserType().equals(UserType.MENTEE)) {
        response.sendRedirect(URLPatterns.FIND_MENTOR);
      } else {
        response.sendRedirect(URLPatterns.PROFILE);
      }
    } else {
      LOG.warning(ErrorMessages.INVALID_PARAMATERS);
    }
  }

  private UserAccount constructNewUserFromRequest(HttpServletRequest request) {
    UserType userType =
        ServletUtils.getEnumParameter(
            UserType.class, request, ParameterConstants.FORM_TYPE, "MENTEE");
    String name = ServletUtils.getParameter(request, ParameterConstants.NAME, "John Doe");
    Date dateOfBirth;
    try {
      dateOfBirth =
          new SimpleDateFormat("yyyy-MM-dd")
              .parse(
                  ServletUtils.getParameter(
                      request, ParameterConstants.DATE_OF_BIRTH, "2000-01-01"));
    } catch (ParseException e) {
      dateOfBirth = new Date();
      LOG.warning(ErrorMessages.BAD_DATE_PARSE);
    }
    Country country =
        ServletUtils.getEnumParameter(
            Country.class, request, ParameterConstants.COUNTRY, Country.US.toString());
    TimeZone timeZone =
        ServletUtils.getEnumParameter(
            TimeZone.class, request, ParameterConstants.TIMEZONE, TimeZone.EST.toString());
    Language language =
        ServletUtils.getEnumParameter(
            Language.class, request, ParameterConstants.LANGUAGE, Language.EN.toString());

    List<Ethnicity> ethnicities =
        ServletUtils.getListOfCheckedValues(
            Ethnicity.class,
            request,
            ParameterConstants.ETHNICITY,
            Ethnicity.UNSPECIFIED.toString());

    String ethnicityOther =
        ethnicities.contains(Ethnicity.OTHER)
            ? ServletUtils.getParameter(request, ParameterConstants.ETHNICITY_OTHER, "")
            : "";

    Gender gender =
        ServletUtils.getEnumParameter(
            Gender.class, request, ParameterConstants.GENDER, Gender.UNSPECIFIED.toString());
    String genderOther =
        ServletUtils.getOtherStringValue(
            gender, Gender.class, request, ParameterConstants.GENDER_OTHER);

    EducationLevel educationLevel =
        ServletUtils.getEnumParameter(
            EducationLevel.class,
            request,
            ParameterConstants.EDUCATION_LEVEL,
            EducationLevel.UNSPECIFIED.toString());
    String educationLevelOther =
        ServletUtils.getOtherStringValue(
            educationLevel,
            EducationLevel.class,
            request,
            ParameterConstants.EDUCATION_LEVEL_OTHER);
    boolean firstGen =
        Boolean.parseBoolean(
            ServletUtils.getParameter(request, ParameterConstants.FIRST_GEN, "false"));
    boolean lowIncome =
        Boolean.parseBoolean(
            ServletUtils.getParameter(request, ParameterConstants.LOW_INCOME, "false"));
    MentorType mentorType =
        ServletUtils.getEnumParameter(
            MentorType.class, request, ParameterConstants.MENTOR_TYPE, MentorType.TUTOR.toString());
    String description = ServletUtils.getParameter(request, ParameterConstants.DESCRIPTION, "");

    String profilePicBlobKey = getUploadedFileBlobKey(request, ParameterConstants.PROFILE_PICTURE);

    if (userType.equals(UserType.MENTEE)) {
      MeetingFrequency desiredMeetingFrequency =
          ServletUtils.getEnumParameter(
              MeetingFrequency.class,
              request,
              ParameterConstants.MENTEE_DESIRED_MEETING_FREQUENCY,
              MeetingFrequency.WEEKLY.toString());
      Topic goal =
          ServletUtils.getEnumParameter(
              Topic.class, request, ParameterConstants.MENTEE_GOAL, Topic.values()[0].toString());
      return Mentee.Builder.newBuilder()
          .isFakeUser(false)
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
          .profilePicBlobKey(profilePicBlobKey)
          .goal(goal)
          .desiredMeetingFrequency(desiredMeetingFrequency)
          .desiredMentorType(mentorType)
          .build();

    } else {
      List<Topic> focusList =
          ServletUtils.getListOfCheckedValues(
              Topic.class,
              request,
              ParameterConstants.MENTOR_FOCUS_LIST,
              Topic.values()[0].toString());

      return Mentor.Builder.newBuilder()
          .isFakeUser(false)
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
          .profilePicBlobKey(profilePicBlobKey)
          .mentorType(mentorType)
          .visibility(true)
          .focusList(focusList)
          .build();
    }
  }

  /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
  private String getUploadedFileBlobKey(HttpServletRequest request, String formInputElementName) {
    Map<String, List<BlobKey>> blobs = dataAccess.getBlobUploads(request);
    List<BlobKey> blobKeys = blobs.get(formInputElementName);

    // User submitted form without selecting a file, so we can't get a URL. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = dataAccess.getBlobInfo(blobKey);
    if (blobInfo != null) {
      if (blobInfo.getSize() == 0) {
        dataAccess.deleteBlob(blobKey.getKeyString());
        return null;
      }

      // make sure it's an image file
      String contentType = blobInfo.getContentType();
      if (!contentType.contains("png")
          && !contentType.contains("jpg")
          && !contentType.contains("jpeg")) {
        return null;
      }
    }

    return blobKey.getKeyString();
  }

  private Map<String, Object> selectionListsForFrontEnd() {
    Map<String, Object> map = new HashMap<>();
    map.put("countries", Country.valuesSorted());
    map.put("genders", Gender.values());
    map.put("languages", Language.valuesSorted());
    map.put("mentorTypes", MentorType.values());
    map.put("timezones", TimeZone.valuesSorted());
    map.put("educationLevels", EducationLevel.values());
    map.put("meetingFrequencies", MeetingFrequency.values());
    return map;
  }
}
