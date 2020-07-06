package com.google.sps.data;

import static java.lang.Math.toIntExact;

import com.google.appengine.api.datastore.Entity;
import com.google.sps.util.ParameterConstants;
import java.util.Set;

public class Mentee extends UserAccount {

  private Topic goal;
  private MeetingFrequency desiredMeetingFrequency;
  private Set<Long> dislikedMentorKeys;

  private Mentee(Builder builder) {
    super(builder);
    this.goal = builder.goal;
    this.desiredMeetingFrequency = builder.desiredMeetingFrequency;
    this.dislikedMentorKeys = builder.dislikedMentorKeys;
  }

  public Mentee(Entity entity) {
    super(entity);
    this.goal =
        Topic.values()[toIntExact((long) entity.getProperty(ParameterConstants.MENTEE_GOAL))];
    this.desiredMeetingFrequency =
        MeetingFrequency.values()[
            toIntExact(
                (long) entity.getProperty(ParameterConstants.MENTEE_DESIRED_MEETING_FREQUENCY))];
    this.dislikedMentorKeys =
        (Set<Long>) entity.getProperty(ParameterConstants.MENTEE_DISLIKED_MENTOR_KEYS);
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(ParameterConstants.MENTEE_GOAL, goal.ordinal());
    entity.setProperty(
        ParameterConstants.MENTEE_DESIRED_MEETING_FREQUENCY, desiredMeetingFrequency.ordinal());
    entity.setProperty(ParameterConstants.MENTEE_DISLIKED_MENTOR_KEYS, this.dislikedMentorKeys);
    return entity;
  }

  public Topic getGoal() {
    return goal;
  }

  public MeetingFrequency getDesiredMeetingFrequency() {
    return desiredMeetingFrequency;
  }

  public Set<Long> getDislikedMentorKeys() {
    return this.dislikedMentorKeys;
  }

  public static class Builder extends UserAccount.Builder<Builder> {
    private Topic goal;
    private MeetingFrequency desiredMeetingFrequency;
    private Set<Long> dislikedMentorKeys;

    public Builder() {}

    public Builder goal(Topic goal) {
      this.goal = goal;
      return this;
    }

    public Builder desiredMeetingFrequency(MeetingFrequency desiredMeetingFrequency) {
      this.desiredMeetingFrequency = desiredMeetingFrequency;
      return this;
    }

    public Builder dislikedMentorKeys(Set<Long> dislikedMentorKeys) {
      this.dislikedMentorKeys = dislikedMentorKeys;
      return this;
    }

    public Mentee build() {
      return new Mentee(this);
    }
  }
}
