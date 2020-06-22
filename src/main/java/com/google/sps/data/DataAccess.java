package com.google.sps.data;

import com.google.appengine.api.datastore.Key;
import java.util.Collection;

public abstract interface DataAccess {

  public abstract UserAccount getUser(String userID);

  public abstract UserAccount getUser(Key datastoreKey);

  public abstract Collection<Mentor> getRelatedMentors(Mentee mentee);

  public abstract Collection<Mentee> getRelatedMentees(Mentor mentor);

  public abstract Collection<MenteeToMentorRequest> getIncomingRequests(Mentor mentor);

  public abstract Collection<MentorToMenteeRequest> getIncomingRequests(Mentee mentee);

  public abstract Collection<MentorToMenteeRequest> getOutgoingRequests(Mentor mentor);

  public abstract Collection<MenteeToMentorRequest> getOutgoingRequests(Mentee mentee);

  public abstract void saveUser(UserAccount user);

  public abstract Collection<Connection> getConnections(UserAccount user);

  public abstract void publishRequest(MentorshipRequest request);

  public abstract void deleteRequest(MentorshipRequest request);

  // delete request object and create connection object
  public abstract void approveRequest(MentorshipRequest request);

  // delete request object
  public abstract void denyRequest(MentorshipRequest request);
}
