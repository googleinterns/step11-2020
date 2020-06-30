package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import java.util.Collection;

public class Mentee extends UserAccount {

  private static final String GOAL = "goal";
  private static final String DESIRED_MEETING_FREQUENCY = "desiredMeetingFrequency";
  private static final String YESSES = "yesses";
  private static final String MAYBES = "maybes";
  private static final String NOS = "nos";

  private Topic goal;
  private MeetingFrequency desiredMeetingFrequency;
  private Collection<Long> yesses;
  private Collection<Long> maybes;
  private Collection<Long> nos;

  private Mentee(Builder builder) {
    super(builder);
    this.goal = builder.goal;
    this.desiredMeetingFrequency = builder.desiredMeetingFrequency;
    this.yesses = builder.yesses;
    this.maybes = builder.maybes;
    this.nos = builder.nos;
  }

  public Mentee(Entity entity) {
    super(entity);
    this.goal = Topic.values()[(int) entity.getProperty(GOAL)];
    this.desiredMeetingFrequency =
        MeetingFrequency.values()[(int) entity.getProperty(DESIRED_MEETING_FREQUENCY)];
    this.yesses = (Collection<Long>) entity.getProperty(YESSES);
    this.maybes = (Collection<Long>) entity.getProperty(MAYBES);
    this.nos = (Collection<Long>) entity.getProperty(NOS);
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(GOAL, goal.ordinal());
    entity.setProperty(DESIRED_MEETING_FREQUENCY, desiredMeetingFrequency.ordinal());
    entity.setProperty(YESSES, this.yesses);
    entity.setProperty(MAYBES, this.maybes);
    entity.setProperty(NOS, this.nos);
    return entity;
  }

  public Topic getGoal() {
    return goal;
  }

  public MeetingFrequency getDesiredMeetingFrequency() {
    return desiredMeetingFrequency;
  }

  public Collection<Long> getYesses() {
    return this.yesses;
  }

  public Collection<Long> getMaybes() {
    return this.maybes;
  }

  public Collection<Long> getNos() {
    return this.nos;
  }

  public static class Builder extends UserAccount.Builder<Builder> {
    private Topic goal;
    private MeetingFrequency desiredMeetingFrequency;
    private Collection<Long> yesses;
    private Collection<Long> maybes;
    private Collection<Long> nos;

    public Builder() {}

    public Builder goal(Topic goal) {
      this.goal = goal;
      return this;
    }

    public Builder desiredMeetingFrequency(MeetingFrequency desiredMeetingFrequency) {
      this.desiredMeetingFrequency = desiredMeetingFrequency;
      return this;
    }

    public Builder yesses(Collection<Long> yesses) {
      this.yesses = yesses;
      return this;
    }

    public Builder maybes(Collection<Long> maybes) {
      this.maybes = maybes;
      return this;
    }

    public Builder nos(Collection<Long> nos) {
      this.nos = nos;
      return this;
    }

    public Mentee build() {
      return new Mentee(this);
    }
  }
}
