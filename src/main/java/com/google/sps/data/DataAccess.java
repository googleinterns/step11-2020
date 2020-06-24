package com.google.sps.data;

import com.google.appengine.api.datastore.Key;
import java.util.Collection;

interface DataAccess {

  UserAccount getUser(String userID);

  UserAccount getUser(Key datastoreKey);

  Collection<Mentor> getRelatedMentors(Mentee mentee);

  Collection<Mentee> getRelatedMentees(Mentor mentor);

  Collection<MenteeToMentorRequest> getIncomingRequests(Mentor mentor);

  Collection<MentorToMenteeRequest> getIncomingRequests(Mentee mentee);

  Collection<MentorToMenteeRequest> getOutgoingRequests(Mentor mentor);

  Collection<MenteeToMentorRequest> getOutgoingRequests(Mentee mentee);

  void saveUser(UserAccount user);

  Collection<Connection> getConnections(UserAccount user);

  void publishRequest(MentorshipRequest request);

  void deleteRequest(MentorshipRequest request);

  // delete request object and create connection object
  void approveRequest(MentorshipRequest request);

  // delete request object
  void denyRequest(MentorshipRequest request);
}
