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

package com.google.sps.data;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.util.ContextFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * This class provides access to hardcoded mock data meant for testing.
 *
 * @author guptamudit
 * @author sylviaziyuz
 * @author tquintanilla
 * @version 1.0
 */
public class DummyDataAccess implements DataAccess {

  private static final UserService userService = UserServiceFactory.getUserService();

  public Map<String, Object> getDefaultRenderingContext(String currentURL) {
    Map<String, Object> context = new HashMap<String, Object>();
    context.put(ContextFields.URL, currentURL);
    boolean loggedIn = userService.isUserLoggedIn();
    context.put(ContextFields.IS_LOGGED_IN, loggedIn);
    UserAccount currentUser = null;
    boolean isMentor = false, isMentee = false;
    if (loggedIn) {
      User user = getCurrentUser();
      currentUser = getUser(user.getUserId());
      isMentor = currentUser.getUserType() == UserType.MENTOR;
      isMentee = !isMentor && currentUser.getUserType() == UserType.MENTEE;
    }
    context.put(ContextFields.CURRENT_USER, currentUser);
    context.put(ContextFields.IS_MENTOR, isMentor);
    context.put(ContextFields.IS_MENTEE, isMentee);
    return context;
  }

  public User getCurrentUser() {
    return userService.getCurrentUser();
  }

  public UserAccount getUser(String userID) {
    return (new Mentor.Builder())
        .name("Alice")
        .userID(userID)
        .email("alice@gmail.com")
        .dateOfBirth(new Date())
        .country(Country.AU)
        .language(Language.ES)
        .timezone(new TimeZoneInfo(TimeZone.getDefault()))
        .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
        .ethnicityOther("")
        .gender(Gender.WOMAN)
        .genderOther("")
        .firstGen(true)
        .lowIncome(true)
        .educationLevel(EducationLevel.BACHELORS)
        .educationLevelOther("")
        .description("hi im alice")
        .mentorType(MentorType.TUTOR)
        .visibility(true)
        .userType(UserType.MENTOR)
        .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
        .build();
  }

  public UserAccount getUser(long datastoreKey) {
    return (new Mentor.Builder())
        .name("Alice")
        .userID("321432")
        .email("alice@gmail.com")
        .dateOfBirth(new Date())
        .country(Country.AU)
        .language(Language.ES)
        .timezone(new TimeZoneInfo(TimeZone.getDefault()))
        .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
        .ethnicityOther("")
        .gender(Gender.WOMAN)
        .genderOther("")
        .firstGen(true)
        .lowIncome(true)
        .educationLevel(EducationLevel.BACHELORS)
        .educationLevelOther("")
        .description("hi im alice")
        .mentorType(MentorType.TUTOR)
        .visibility(true)
        .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
        .build();
  }

  public Mentee getMentee(String userID) {
    return (new Mentee.Builder())
        .name("Alice")
        .userID("321432")
        .email("alice@gmail.com")
        .dateOfBirth(new Date())
        .country(Country.AU)
        .language(Language.ES)
        .timezone(new TimeZoneInfo(TimeZone.getDefault()))
        .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
        .ethnicityOther("")
        .gender(Gender.WOMAN)
        .genderOther("")
        .firstGen(true)
        .lowIncome(true)
        .educationLevel(EducationLevel.BACHELORS)
        .educationLevelOther("")
        .description("hi im alice")
        .goal(Topic.COMPUTER_SCIENCE)
        .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
        .build();
  }

  public Mentee getMentee(long datastoreKey) {
    return (new Mentee.Builder())
        .name("Alice")
        .userID("321432")
        .email("alice@gmail.com")
        .dateOfBirth(new Date())
        .country(Country.AU)
        .language(Language.ES)
        .timezone(new TimeZoneInfo(TimeZone.getDefault()))
        .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
        .ethnicityOther("")
        .gender(Gender.WOMAN)
        .genderOther("")
        .firstGen(true)
        .lowIncome(true)
        .educationLevel(EducationLevel.BACHELORS)
        .educationLevelOther("")
        .description("hi im alice")
        .goal(Topic.COMPUTER_SCIENCE)
        .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
        .build();
  }

  public Mentor getMentor(String userID) {
    UserAccount user = getUser(userID);
    return user.getUserType() == UserType.MENTEE ? null : (Mentor) user;
  }

  public Mentor getMentor(long datastoreKey) {
    UserAccount user = getUser(datastoreKey);
    return user.getUserType() == UserType.MENTEE ? null : (Mentor) user;
  }

  public boolean createUser(UserAccount user) {
    return false;
  }

  public boolean updateUser(UserAccount user) {
    return false;
  }

  public Collection<Mentor> getRelatedMentors(Mentee mentee) {
    Collection<Mentor> mentors = new ArrayList<>(5);
    mentors.add(
        (new Mentor.Builder())
            .name("Alice")
            .userID("321432")
            .email("alice@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.WOMAN)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im alice")
            .mentorType(MentorType.TUTOR)
            .visibility(true)
            .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
            .build());
    mentors.add(
        (new Mentor.Builder())
            .name("Ethan")
            .userID("532345")
            .email("ethan@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.MAN)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im ethan")
            .mentorType(MentorType.TUTOR)
            .visibility(true)
            .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
            .build());
    mentors.add(
        (new Mentor.Builder())
            .name("Sam")
            .userID("539032")
            .email("sam@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.NONBINARY)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im sam")
            .mentorType(MentorType.TUTOR)
            .visibility(true)
            .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
            .build());
    return mentors;
  }

  public Collection<Mentee> getRelatedMentees(Mentor mentor) {
    Collection<Mentee> mentees = new ArrayList(5);
    mentees.add(
        (new Mentee.Builder())
            .name("Bradley")
            .userID("112454")
            .email("bradley@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.MAN)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im bradley")
            .goal(Topic.COMPUTER_SCIENCE)
            .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
            .build());
    mentees.add(
        (new Mentee.Builder())
            .name("Cooper")
            .userID("534153")
            .email("cooper@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.MAN)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im cooper")
            .goal(Topic.COMPUTER_SCIENCE)
            .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
            .build());
    mentees.add(
        (new Mentee.Builder())
            .name("Stacy")
            .userID("999999")
            .email("stacy@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.WOMAN)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im STACY")
            .goal(Topic.COMPUTER_SCIENCE)
            .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
            .build());
    return mentees;
  }

  public Collection<MentorshipRequest> getIncomingRequests(UserAccount user) {
    Collection<MentorshipRequest> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      MentorshipRequest request = new MentorshipRequest(i + 1, i + 2);
      request.setToUser(user);
      request.setFromUser(
          (new Mentee.Builder())
              .name("Stacy")
              .userID("999999")
              .email("stacy@gmail.com")
              .dateOfBirth(new Date())
              .country(Country.AU)
              .language(Language.ES)
              .timezone(new TimeZoneInfo(TimeZone.getDefault()))
              .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
              .ethnicityOther("")
              .gender(Gender.WOMAN)
              .genderOther("")
              .firstGen(true)
              .lowIncome(true)
              .educationLevel(EducationLevel.BACHELORS)
              .educationLevelOther("")
              .description("hi im STACY")
              .goal(Topic.COMPUTER_SCIENCE)
              .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
              .build());
      data.add(request);
    }
    return data;
  }

  public Collection<MentorshipRequest> getOutgoingRequests(UserAccount user) {
    Collection<MentorshipRequest> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      // data.add(new MentorshipRequest());
    }
    return data;
  }

  public boolean dislikeMentor(Mentee mentee, Mentor mentor) {
    return false;
  }

  public Collection<Mentor> getDislikedMentors(Mentee mentee) {
    Collection<Mentor> mentors = new ArrayList<>(5);
    mentors.add(
        (new Mentor.Builder())
            .name("Alice")
            .userID("321432")
            .email("alice@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.WOMAN)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im alice")
            .mentorType(MentorType.TUTOR)
            .visibility(true)
            .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
            .build());
    mentors.add(
        (new Mentor.Builder())
            .name("Ethan")
            .userID("532345")
            .email("ethan@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.MAN)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im ethan")
            .mentorType(MentorType.TUTOR)
            .visibility(true)
            .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
            .build());
    mentors.add(
        (new Mentor.Builder())
            .name("Sam")
            .userID("539032")
            .email("sam@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(new TimeZoneInfo(TimeZone.getDefault()))
            .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
            .ethnicityOther("")
            .gender(Gender.NONBINARY)
            .genderOther("")
            .firstGen(true)
            .lowIncome(true)
            .educationLevel(EducationLevel.BACHELORS)
            .educationLevelOther("")
            .description("hi im sam")
            .mentorType(MentorType.TUTOR)
            .visibility(true)
            .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
            .build());
    return mentors;
  }

  public boolean publishRequest(MentorshipRequest request) {
    return false;
  }

  public MentorshipRequest getMentorshipRequest(long requestKey) {
    return new MentorshipRequest(requestKey + 1234, requestKey - 1234);
  }

  public boolean deleteRequest(MentorshipRequest request) {
    return false;
  }

  // delete request object and create mentorMenteeRelation object
  public boolean approveRequest(MentorshipRequest request) {
    return false;
  }

  // delete request object
  public boolean denyRequest(MentorshipRequest request) {
    return false;
  }

  public boolean makeMentorMenteeRelation(long mentorKey, long menteeKey) {
    return false;
  }

  public Collection<MentorMenteeRelation> getMentorMenteeRelations(UserAccount user) {
    Collection<MentorMenteeRelation> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      MentorMenteeRelation mentorMenteeRelation = new MentorMenteeRelation(i + 1, i + 2);
      if (user.getUserType() == UserType.MENTOR) {
        mentorMenteeRelation.setMentor((Mentor) user);
        mentorMenteeRelation.setMentee(
            (new Mentee.Builder())
                .name("Stacy")
                .userID("999999")
                .email("stacy@gmail.com")
                .dateOfBirth(new Date())
                .country(Country.AU)
                .language(Language.ES)
                .timezone(new TimeZoneInfo(TimeZone.getDefault()))
                .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
                .ethnicityOther("")
                .gender(Gender.WOMAN)
                .genderOther("")
                .firstGen(true)
                .lowIncome(true)
                .educationLevel(EducationLevel.BACHELORS)
                .educationLevelOther("")
                .description("hi im STACY")
                .goal(Topic.COMPUTER_SCIENCE)
                .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
                .build());
      } else {
        mentorMenteeRelation.setMentee((Mentee) user);
        mentorMenteeRelation.setMentor(
            (new Mentor.Builder())
                .name("Sam")
                .userID("539032")
                .email("sam@gmail.com")
                .dateOfBirth(new Date())
                .country(Country.AU)
                .language(Language.ES)
                .timezone(new TimeZoneInfo(TimeZone.getDefault()))
                .ethnicityList(new ArrayList<Ethnicity>(Arrays.asList(Ethnicity.CAUCASIAN)))
                .ethnicityOther("")
                .gender(Gender.NONBINARY)
                .genderOther("")
                .firstGen(true)
                .lowIncome(true)
                .educationLevel(EducationLevel.BACHELORS)
                .educationLevelOther("")
                .description("hi im sam")
                .mentorType(MentorType.TUTOR)
                .visibility(true)
                .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
                .build());
      }
      data.add(mentorMenteeRelation);
    }
    return data;
  }
}
