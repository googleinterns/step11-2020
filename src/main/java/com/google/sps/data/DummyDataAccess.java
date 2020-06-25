package com.google.sps.data;

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

public class DummyDataAccess implements DataAccess {

  public UserAccount getUser(String userID) {
    return (new Mentor.Builder())
        .name("Alice")
        .userID("321432")
        .email("alice@gmail.com")
        .dateOfBirth(new Date())
        .country(Country.AU)
        .language(Language.ES)
        .timezone(TimeZone.getDefault())
        .ethnicity(Ethnicity.CAUCASIAN)
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

  public UserAccount getUser(Key datastoreKey) {
    return (new Mentor.Builder())
        .name("Alice")
        .userID("321432")
        .email("alice@gmail.com")
        .dateOfBirth(new Date())
        .country(Country.AU)
        .language(Language.ES)
        .timezone(TimeZone.getDefault())
        .ethnicity(Ethnicity.CAUCASIAN)
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

  public Collection<Mentor> getRelatedMentors(Mentee mentee) {
    Collection<Mentor> relatedMentors = new ArrayList<>(5);
    relatedMentors.add(
        (new Mentor.Builder())
            .name("Alice")
            .userID("321432")
            .email("alice@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(TimeZone.getDefault())
            .ethnicity(Ethnicity.CAUCASIAN)
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
    relatedMentors.add(
        (new Mentor.Builder())
            .name("Ethan")
            .userID("532345")
            .email("ethan@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(TimeZone.getDefault())
            .ethnicity(Ethnicity.CAUCASIAN)
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
    relatedMentors.add(
        (new Mentor.Builder())
            .name("Sam")
            .userID("539032")
            .email("sam@gmail.com")
            .dateOfBirth(new Date())
            .country(Country.AU)
            .language(Language.ES)
            .timezone(TimeZone.getDefault())
            .ethnicity(Ethnicity.CAUCASIAN)
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
    return relatedMentors;
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
            .timezone(TimeZone.getDefault())
            .ethnicity(Ethnicity.CAUCASIAN)
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
            .timezone(TimeZone.getDefault())
            .ethnicity(Ethnicity.CAUCASIAN)
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
            .timezone(TimeZone.getDefault())
            .ethnicity(Ethnicity.CAUCASIAN)
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
      // data.add(new MentorshipRequest());
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

  public void saveUser(UserAccount user) {}

  public Collection<Connection> getConnections(UserAccount user) {
    Collection<Connection> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      // data.add(new Connection());
    }
    return data;
  }

  public void publishRequest(MentorshipRequest request) {}

  public void deleteRequest(MentorshipRequest request) {}

  // delete request object and create connection object
  public void approveRequest(MentorshipRequest request) {}

  // delete request object
  public void denyRequest(MentorshipRequest request) {}
}
