package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import static java.lang.Math.toIntExact;
import java.util.Collection;

public class Mentee extends UserAccount {

  private static final String GOAL = "goal";
  private static final String DESIRED_MEETING_FREQUENCY = "desiredMeetingFrequency";
  private static final String DISLIKED_MENTOR_KEYS = "dislikedMentorKeys";

  private Topic goal;
  private MeetingFrequency desiredMeetingFrequency;
  private Collection<Long> dislikedMentorKeys;

  private Mentee(Builder builder) {
    super(builder);
    this.goal = builder.goal;
    this.desiredMeetingFrequency = builder.desiredMeetingFrequency;
    this.dislikedMentorKeys = builder.dislikedMentorKeys;
  }

  public Mentee(Entity entity) {
    super(entity);
    this.goal = Topic.values()[toIntExact((long) entity.getProperty(GOAL))];
    this.desiredMeetingFrequency =
        MeetingFrequency.values()[toIntExact((long) entity.getProperty(DESIRED_MEETING_FREQUENCY))];
    this.dislikedMentorKeys = (Collection<Long>) entity.getProperty(DISLIKED_MENTOR_KEYS);
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(GOAL, goal.ordinal());
    entity.setProperty(DESIRED_MEETING_FREQUENCY, desiredMeetingFrequency.ordinal());
    entity.setProperty(DISLIKED_MENTOR_KEYS, this.dislikedMentorKeys);
    return entity;
  }

  public Topic getGoal() {
    return goal;
  }

  public MeetingFrequency getDesiredMeetingFrequency() {
    return desiredMeetingFrequency;
  }

  public Collection<Long> getDislikedMentorKeys() {
    return this.dislikedMentorKeys;
  }

  public static class Builder extends UserAccount.Builder<Builder> {
    private Topic goal;
    private MeetingFrequency desiredMeetingFrequency;
    private Collection<Long> dislikedMentorKeys;

    public Builder() {}

    public Builder goal(Topic goal) {
      this.goal = goal;
      return this;
    }

    public Builder desiredMeetingFrequency(MeetingFrequency desiredMeetingFrequency) {
      this.desiredMeetingFrequency = desiredMeetingFrequency;
      return this;
    }

    public Builder dislikedMentorKeys(Collection<Long> dislikedMentorKeys) {
      this.dislikedMentorKeys = dislikedMentorKeys;
      return this;
    }

    public Mentee build() {
      return new Mentee(this);
    }
  }
}
