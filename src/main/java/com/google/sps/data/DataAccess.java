package com.google.sps.data;

import com.google.appengine.api.users.User;
import java.util.Collection;
import java.util.Map;

/** Provides an interface for interacting with data
 */
public interface DataAccess {

  Map<String, Object> getDefaultRenderingContext(String currentURL);

  User getCurrentUser();

  UserAccount getUser(String userID);

  UserAccount getUser(long datastoreKey);

  Mentee getMentee(String userID);

  Mentee getMentee(long datastoreKey);

  Mentor getMentor(String userID);

  Mentor getMentor(long datastoreKey);

  boolean createUser(UserAccount user);

  boolean saveUser(UserAccount user);

  Collection<Mentor> getRelatedMentors(Mentee mentee);

  Collection<Mentee> getRelatedMentees(Mentor mentor);

  Collection<MentorshipRequest> getIncomingRequests(UserAccount user);

  Collection<MentorshipRequest> getOutgoingRequests(UserAccount user);

  boolean dislikeMentor(Mentee mentee, Mentor mentor);

  Collection<Mentor> getDislikedMentors(Mentee mentee);

  boolean publishRequest(MentorshipRequest request);

  MentorshipRequest getMentorshipRequest(long requestKey);

  boolean deleteRequest(MentorshipRequest request);

  // delete request object and create connection object
  boolean approveRequest(MentorshipRequest request);

  // delete request object
  boolean denyRequest(MentorshipRequest request);

  boolean makeConnection(long mentorKey, long menteeKey);

  Collection<Connection> getConnections(UserAccount user);
}
