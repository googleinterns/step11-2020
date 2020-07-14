// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import static java.lang.Math.toIntExact;

import com.google.appengine.api.datastore.Entity;
import com.google.sps.util.ParameterConstants;
import java.util.ArrayList;
import java.util.Set;

public class Mentee extends UserAccount implements DatastoreEntity {

  private Topic goal;
  private MeetingFrequency desiredMeetingFrequency;
  private Set<Long> dislikedMentorKeys;
  private Set<Long> requestedMentorKeys;
  private ArrayList<Long> servedMentorKeys;
  private Long lastRequestedMentorKey;
  private Long lastDislikedMentorKey;

  private MentorType desiredMentorType;

  private Mentee(Builder builder) {
    super(builder);
    this.goal = builder.goal;
    this.desiredMeetingFrequency = builder.desiredMeetingFrequency;
    this.dislikedMentorKeys = builder.dislikedMentorKeys;
    this.desiredMentorType = builder.desiredMentorType;

    this.requestedMentorKeys = builder.requestedMentorKeys;
    this.servedMentorKeys = builder.servedMentorKeys;
    this.lastRequestedMentorKey = builder.lastRequestedMentorKey;
    this.lastDislikedMentorKey = builder.lastDislikedMentorKey;
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
    this.desiredMentorType =
        MentorType.values()[toIntExact((long) entity.getProperty(ParameterConstants.MENTOR_TYPE))];
    this.requestedMentorKeys =
        (Set<Long>) entity.getProperty(ParameterConstants.MENTEE_REQUESTED_MENTOR_KEYS);
    this.servedMentorKeys =
        (ArrayList<Long>) entity.getProperty(ParameterConstants.MENTEE_SERVED_MENTOR_KEYS);
    this.lastRequestedMentorKey =
        (long) entity.getProperty(ParameterConstants.MENTEE_LAST_REQUESTED_MENTOR_KEY);
    this.lastDislikedMentorKey =
        (long) entity.getProperty(ParameterConstants.MENTEE_LAST_DISLIKED_MENTOR_KEY);
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(ParameterConstants.MENTEE_GOAL, goal.ordinal());
    entity.setProperty(
        ParameterConstants.MENTEE_DESIRED_MEETING_FREQUENCY, desiredMeetingFrequency.ordinal());
    entity.setProperty(ParameterConstants.MENTEE_DISLIKED_MENTOR_KEYS, this.dislikedMentorKeys);
    entity.setProperty(ParameterConstants.MENTOR_TYPE, desiredMentorType.ordinal());
    entity.setProperty(ParameterConstants.MENTEE_REQUESTED_MENTOR_KEYS, this.requestedMentorKeys);
    entity.setProperty(ParameterConstants.MENTEE_SERVED_MENTOR_KEYS, this.servedMentorKeys);
    entity.setProperty(
        ParameterConstants.MENTEE_LAST_DISLIKED_MENTOR_KEY, this.lastDislikedMentorKey);
    entity.setProperty(
        ParameterConstants.MENTEE_LAST_REQUESTED_MENTOR_KEY, this.lastRequestedMentorKey);
    return entity;
  }

  public boolean dislikeMentor(Mentor mentor) {
    return dislikedMentorKeys.add(mentor.getDatastoreKey());
  }

  public Topic getGoal() {
    return goal;
  }

  public MentorType getDesiredMentorType() {
    return desiredMentorType;
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
    private MentorType desiredMentorType;
    private Set<Long> requestedMentorKeys;
    private ArrayList<Long> servedMentorKeys;
    private Long lastDislikedMentorKey;
    private Long lastRequestedMentorKey;

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

    public Builder desiredMentorType(MentorType desiredMentorType) {
      this.desiredMentorType = desiredMentorType;
      return this;
    }

    public Builder requestedMentorKeys(Set<Long> requestedMentorKeys) {
      this.requestedMentorKeys = requestedMentorKeys;
      return this;
    }

    public Builder servedMentorKeys(ArrayList<Long> servedMentorKeys) {
      this.servedMentorKeys = servedMentorKeys;
      return this;
    }

    public Builder lastRequestedMentorKey(Long lastRequestedMentorKey) {
      this.lastRequestedMentorKey = lastRequestedMentorKey;
      return this;
    }

    public Builder lastDislikedMentorKey(Long lastDislikedMentorKey) {
      this.lastDislikedMentorKey = lastDislikedMentorKey;
      return this;
    }

    public Mentee build() {
      return new Mentee(this);
    }
  }
}
