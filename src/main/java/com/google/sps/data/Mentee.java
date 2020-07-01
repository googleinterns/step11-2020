package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import static java.lang.Math.toIntExact;

public class Mentee extends UserAccount {

  private static final String GOAL = "goal";
  private static final String DESIRED_MEETING_FREQUENCY = "desiredMeetingFrequency";

  private Topic goal;
  private MeetingFrequency desiredMeetingFrequency;

  private Mentee(Builder builder) {
    super(builder);
    this.goal = builder.goal;
    this.desiredMeetingFrequency = builder.desiredMeetingFrequency;
  }

  public Mentee(Entity entity) {
    super(entity);
    this.goal = Topic.values()[toIntExact((long) entity.getProperty(GOAL))];
    this.desiredMeetingFrequency =
        MeetingFrequency.values()[toIntExact((long) entity.getProperty(DESIRED_MEETING_FREQUENCY))];
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(GOAL, goal.ordinal());
    entity.setProperty(DESIRED_MEETING_FREQUENCY, desiredMeetingFrequency.ordinal());
    return entity;
  }

  public Topic getGoal() {
    return goal;
  }

  public MeetingFrequency getDesiredMeetingFrequency() {
    return desiredMeetingFrequency;
  }

  public static class Builder extends UserAccount.Builder<Builder> {
    private Topic goal;
    private MeetingFrequency desiredMeetingFrequency;

    public Builder() {}

    public Builder goal(Topic goal) {
      this.goal = goal;
      return this;
    }

    public Builder desiredMeetingFrequency(MeetingFrequency desiredMeetingFrequency) {
      this.desiredMeetingFrequency = desiredMeetingFrequency;
      return this;
    }

    public Mentee build() {
      return new Mentee(this);
    }
  }
}
