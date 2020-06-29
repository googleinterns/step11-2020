package com.google.sps.data;

import com.google.appengine.api.datastore.Key;
import java.util.Collection;

interface DataAccess {

  UserAccount getUser(String userID);

  UserAccount getUser(Key datastoreKey);

  UserAccount getCurrentUser();

  Collection<Mentor> getRelatedMentors(Mentee mentee);

  Collection<Mentee> getRelatedMentees(Mentor mentor);

  Collection<MentorshipRequest> getIncomingRequests(UserAccount user);

  Collection<MentorshipRequest> getOutgoingRequests(UserAccount user);

  void saveUser(UserAccount user);

  Collection<Connection> getConnections(UserAccount user);

  void publishRequest(MentorshipRequest request);

  void deleteRequest(MentorshipRequest request);

  // delete request object and create connection object
  void approveRequest(MentorshipRequest request);

  // delete request object
  void denyRequest(MentorshipRequest request);

}
