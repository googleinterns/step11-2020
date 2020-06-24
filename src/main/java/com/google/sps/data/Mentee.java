package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public class Mentee {

  private Topic goal;
  private MeetingFrequency desiredMeetingFrequency;

  public Mentee(Map<String, Object> accountData) {
    super(accountData);
    this.goal = accountData.get("goal");
    this.desiredMeetingFrequency = accountData.get("desiredMeetingFrequency");
  }

  public Mentee(Entity entity) {
    super(entity);
    this.goal = Topic.values()[(int) accountData.get("goal")];
    this.desiredMeetingFrequency = MeetingFrequency.values()[(int) accountData.get("desiredMeetingFrequency")];
  }

  public Topic getGoal() {
    return goal;
  }

  public MeetingFrequency getDesiredMeetingFrequency() {
    return desiredMeetingFrequency;
  }
}
