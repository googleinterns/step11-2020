package com.google.sps.data;

import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MenteeToMentorRequest;
import com.google.sps.data.MentorToMenteeRequest;
import java.util.Collection;
import java.util.ArrayList;

public interface DummyDataAccess {

  public static Collection<Mentor> getRelatedMentors(Mentee mentee) {
    ArrayList<Mentor> relatedMentors = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      
    }
  }

  public static Collection<Mentee> getRelatedMentees(Mentor mentor);

  public static Collection<MenteeToMentorRequest> getIncomingRequests(Mentor mentor);

  public static Collection<MentorToMenteeRequest> getIncomingRequests(Mentee mentee);

}
