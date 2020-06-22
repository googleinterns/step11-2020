package com.google.sps.data;

import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MenteeToMentorRequest;
import com.google.sps.data.MentorToMenteeRequest;
import java.util.Collection;

public interface DataAccess {

  public static Collection<Mentor> getRelatedMentors(Mentee mentee);

  public static Collection<Mentee> getRelatedMentees(Mentor mentor);

  public static Collection<MenteeToMentorRequest> getIncomingRequests(Mentor mentor);

  public static Collection<MentorToMenteeRequest> getIncomingRequests(Mentee mentee);

}
