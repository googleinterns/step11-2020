package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public class Mentee extends UserAccount {

  private Topic goal;
  private MeetingFrequency desiredMeetingFrequency;

  private Mentee(Builder builder) {
    super(builder);
    this.goal = builder.goal;
    this.desiredMeetingFrequency = builder.desiredMeetingFrequency;
  }

  public Mentee(Entity entity) {
    super(entity);
    this.goal = Topic.values()[(int) entity.getProperty("goal")];
    this.desiredMeetingFrequency =
        MeetingFrequency.values()[(int) entity.getProperty("desiredMeetingFrequency")];
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty("goal", goal.ordinal());
    entity.setProperty("desiredMeetingFrequency", desiredMeetingFrequency.ordinal());
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
