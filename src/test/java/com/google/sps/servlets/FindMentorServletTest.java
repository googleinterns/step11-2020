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

import static org.mockito.Mockito.when;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.Country;
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
import com.google.sps.data.UserType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * TO RUN PROJECT WITHOUT TESTS RUN COMMAND: mvn package appengine:run -DskipTests Basic structure
 * of servlet tests using LocalServiceTestHelper for intiailizing services (dodges no api in thread
 * error), for more help with unit testing visit:
 * https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
@RunWith(JUnit4.class)
public final class FindMentorServletTest {
  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  private DatastoreAccess dataAccess;
  private FindMentorServlet servlet;
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
              new LocalUserServiceTestConfig(), new LocalDatastoreServiceTestConfig())
          .setEnvAttributes(
              Collections.singletonMap(
                  "com.google.appengine.api.users.UserService.user_id_key", "102"))
          .setEnvEmail("mudito@example.com")
          .setEnvAuthDomain("gmail.com")
          .setEnvIsLoggedIn(true);
  private Mentor defaultMentor;
  private Mentee defaultMentee;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    helper.setUp();
    dataAccess = new DatastoreAccess();
    servlet = new FindMentorServlet();
    servlet.init();
    defaultMentor =
        (Mentor.Builder.newBuilder())
            .name("Mudito Mentor")
            .userID("101")
            .email("mudito.mentor@example.com")
            .dateOfBirth(new Date(984787200000L))
            .country(Country.US)
            .language(Language.EN)
            .timezone(TimeZone.GMT)
            .ethnicityList((Arrays.asList(Ethnicity.INDIAN)))
            .ethnicityOther("")
            .gender(Gender.MALE)
            .genderOther("")
            .firstGen(false)
            .lowIncome(false)
            .educationLevel(EducationLevel.HIGHSCHOOL)
            .educationLevelOther("")
            .description("I am very cool.")
            .mentorType(MentorType.TUTOR)
            .visibility(true)
            .userType(UserType.MENTOR)
            .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
            .build();
    defaultMentee =
        (Mentee.Builder.newBuilder())
            .name("Mudito Mentee")
            .userID("102")
            .email("mudito.mentee@example.com")
            .dateOfBirth(new Date(984787200000L))
            .country(Country.US)
            .language(Language.EN)
            .timezone(TimeZone.GMT)
            .ethnicityList((Arrays.asList(Ethnicity.INDIAN)))
            .ethnicityOther("")
            .gender(Gender.MALE)
            .genderOther("")
            .firstGen(false)
            .lowIncome(false)
            .educationLevel(EducationLevel.HIGHSCHOOL)
            .educationLevelOther("")
            .description("I am very cool.")
            .userType(UserType.MENTEE)
            .goal(Topic.COMPUTER_SCIENCE)
            .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
            .dislikedMentorKeys(Collections.emptySet())
            .desiredMentorType(MentorType.CAREER)
            .build();
    dataAccess.createUser(defaultMentee);
    dataAccess.createUser(defaultMentor);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void mentorshipRequestSent() throws Exception {
    long mentorDatastoreKey = defaultMentor.getDatastoreKey();
    when(request.getParameter("mentorID")).thenReturn("" + mentorDatastoreKey);
    when(request.getParameter("choice")).thenReturn("sendRequest");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("true"));
    Assert.assertTrue(dataAccess.getIncomingRequests(defaultMentor).size() > 0);
    Assert.assertTrue(dataAccess.getOutgoingRequests(defaultMentee).size() > 0);
  }

  @Test
  public void dislikedMentorAdded() throws Exception {
    long mentorDatastoreKey = defaultMentor.getDatastoreKey();
    when(request.getParameter("mentorID")).thenReturn("" + mentorDatastoreKey);
    when(request.getParameter("choice")).thenReturn("dislikeMentor");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("true"));
    Assert.assertTrue(defaultMentee.getLastDislikedMentorKey() != null);
  }

  @Test
  public void notValidChoiceFails() throws Exception {
    long mentorDatastoreKey = defaultMentor.getDatastoreKey();
    when(request.getParameter("mentorID")).thenReturn("" + mentorDatastoreKey);
    when(request.getParameter("choice")).thenReturn("indifferent");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("false"));
  }
}
