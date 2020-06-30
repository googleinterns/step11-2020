package com.google.sps.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import java.util.Collection;

interface DataAccess {

  User getCurrentUser();

  UserAccount getUser(String userID);

  UserAccount getUser(Key datastoreKey);

  Mentee getMentee(String userID);
  Mentee getMentee(Key datastoreKey);

  Mentor getMentor(String userID);
  Mentor getMentor(Key datastoreKey);

  Collection<Mentor> getRelatedMentors(Mentee mentee);

  Collection<Mentee> getRelatedMentees(Mentor mentor);

  Collection<MentorshipRequest> getIncomingRequests(UserAccount user);

  Collection<MentorshipRequest> getOutgoingRequests(UserAccount user);

  Collection<Mentor> getMentorsByMentorshipRequests(Collection<MentorshipRequest> requests);

  Collection<Mentee> getMenteesByMentorshipRequests(Collection<MentorshipRequest> requests);

  void saveUser(UserAccount user);

  Collection<Connection> getConnections(UserAccount user);

  MentorshipRequest getMentorshipRequest(long requestKey);

  void publishRequest(MentorshipRequest request);

  void deleteRequest(MentorshipRequest request);

  // delete request object and create connection object
  void approveRequest(MentorshipRequest request);

  // delete request object
  void denyRequest(MentorshipRequest request);
}
