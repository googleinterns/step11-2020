package com.google.sps.data;

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.Collection;

public class DummyDataAccess implements DataAccess {

  public UserAccount getUser(String userID) {
    return new Mentor(
        "Bob", userID, "05/23/1985", "medicine", "Master's in Biomedical Engineering");
  }

  public UserAccount getUser(Key datastoreKey) {
    return new Mentor(
        "Bob",
        "" + datastoreKey.getId(),
        "05/23/1985",
        "medicine",
        "Master's in Biomedical Engineering");
  }

  public Collection<Mentor> getRelatedMentors(Mentee mentee) {
    Collection<Mentor> relatedMentors = new ArrayList<>(5);
    relatedMentors.add(
        new Mentor("Alice", "12345", "03/17/2001", "technology", "Master's in Computer Science"));
    relatedMentors.add(
        new Mentor("Bob", "23456", "05/23/1985", "medicine", "Master's in Biomedical Engineering"));
    relatedMentors.add(
        new Mentor("Charlie", "34567", "07/27/1960", "mental health", "PhD in Psychology"));
    relatedMentors.add(
        new Mentor("Dave", "45678", "11/08/1992", "arts and crafts", "Bachelor of Arts"));
    relatedMentors.add(
        new Mentor("Edward", "56789", "09/31/1972", "personal branding", "Highschool Graduate"));
    return relatedMentors;
  }

  public Collection<Mentee> getRelatedMentees(Mentor mentor) {
    Collection<Mentee> mentees = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      mentees.add(new Mentee());
    }
    return mentees;
  }

  public Collection<MenteeToMentorRequest> getIncomingRequests(Mentor mentor) {
    Collection<MenteeToMentorRequest> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      data.add(new MenteeToMentorRequest());
    }
    return data;
  }

  public Collection<MentorToMenteeRequest> getIncomingRequests(Mentee mentee) {
    Collection<MentorToMenteeRequest> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      data.add(new MentorToMenteeRequest());
    }
    return data;
  }

  public Collection<MentorToMenteeRequest> getOutgoingRequests(Mentor mentor) {
    Collection<MentorToMenteeRequest> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      data.add(new MentorToMenteeRequest());
    }
    return data;
  }

  public Collection<MenteeToMentorRequest> getOutgoingRequests(Mentee mentee) {
    Collection<MenteeToMentorRequest> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      data.add(new MenteeToMentorRequest());
    }
    return data;
  }

  public void saveUser(UserAccount user) {}

  public Collection<Connection> getConnections(UserAccount user) {
    Collection<Connection> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      data.add(new Connection());
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
