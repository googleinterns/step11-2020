// Copyright 2020 Google LLC
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

import com.google.appengine.api.datastore.Entity;
import com.google.sps.util.ParameterConstants;
import com.google.sps.util.ServletUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class represents a Mentee user and their mentee-specific data. Other data is held within the
 * super class, UserAccount.
 *
 * @author guptamudit
 * @author tquintanilla
 * @author sylviaziyuz
 * @version 1.0
 */
public class Mentee extends UserAccount implements DatastoreEntity {

  private Topic goal;
  private MeetingFrequency desiredMeetingFrequency;
  private Set<Long> dislikedMentorKeys;
  private SortedSet<Long> servedMentorKeys;
  private String encodedCursor;
  private Long lastRequestedMentorKey;
  private Long lastDislikedMentorKey;

  private MentorType desiredMentorType;

  private Mentee(Builder builder) {
    super(builder);
    this.goal = builder.goal;
    this.desiredMeetingFrequency = builder.desiredMeetingFrequency;
    this.dislikedMentorKeys = builder.dislikedMentorKeys;
    this.desiredMentorType = builder.desiredMentorType;
    this.servedMentorKeys = builder.servedMentorKeys;
    this.lastRequestedMentorKey = builder.lastRequestedMentorKey;
    this.lastDislikedMentorKey = builder.lastDislikedMentorKey;
    this.encodedCursor = encodedCursor;
    this.sanitizeValues();
  }

  public Mentee(Entity entity) {
    super(entity);
    this.goal = Topic.valueOf((String) entity.getProperty(ParameterConstants.MENTEE_GOAL));
    this.desiredMeetingFrequency =
        MeetingFrequency.valueOf(
            (String) entity.getProperty(ParameterConstants.MENTEE_DESIRED_MEETING_FREQUENCY));
    this.dislikedMentorKeys =
        getDislikedSetFromProperty(
            (Collection) entity.getProperty(ParameterConstants.MENTEE_DISLIKED_MENTOR_KEYS));
    this.desiredMentorType =
        MentorType.valueOf(
            (String) entity.getProperty(ParameterConstants.MENTEE_DESIRED_MENTOR_TYPE));
    this.servedMentorKeys =
        getServedSetFromProperty(
            (Collection) entity.getProperty(ParameterConstants.MENTEE_SERVED_MENTOR_KEYS));
    this.lastRequestedMentorKey =
        getLongFromProperty(
            entity.getProperty(ParameterConstants.MENTEE_LAST_REQUESTED_MENTOR_KEY));
    this.lastDislikedMentorKey =
        getLongFromProperty(entity.getProperty(ParameterConstants.MENTEE_LAST_DISLIKED_MENTOR_KEY));

    this.encodedCursor =
        getStringFromProperty(entity.getProperty(ParameterConstants.ENCODED_CURSOR));
    this.sanitizeValues();
  }

  @Override
  protected void sanitizeValues() {
    super.sanitizeValues();
    if (this.dislikedMentorKeys == null) {
      this.dislikedMentorKeys = new HashSet<>();
    }
    if (this.encodedCursor == null) {
      this.encodedCursor = "";
    }
    if (this.servedMentorKeys == null) {
      this.servedMentorKeys = new TreeSet<>();
    }
    if (this.lastRequestedMentorKey == null) {
      this.lastRequestedMentorKey = new Long(0);
    }
    if (this.lastDislikedMentorKey == null) {
      this.lastDislikedMentorKey = new Long(0);
    }
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(ParameterConstants.MENTEE_GOAL, goal.name());
    entity.setProperty(
        ParameterConstants.MENTEE_DESIRED_MEETING_FREQUENCY, desiredMeetingFrequency.name());
    entity.setProperty(ParameterConstants.MENTEE_DISLIKED_MENTOR_KEYS, this.dislikedMentorKeys);
    entity.setProperty(ParameterConstants.MENTEE_DESIRED_MENTOR_TYPE, desiredMentorType.name());
    entity.setProperty(ParameterConstants.MENTEE_SERVED_MENTOR_KEYS, this.servedMentorKeys);
    entity.setProperty(
        ParameterConstants.MENTEE_LAST_DISLIKED_MENTOR_KEY, this.lastDislikedMentorKey);
    entity.setProperty(
        ParameterConstants.MENTEE_LAST_REQUESTED_MENTOR_KEY, this.lastRequestedMentorKey);
    entity.setProperty(ParameterConstants.ENCODED_CURSOR, this.encodedCursor);
    return entity;
  }

  /**
   * converts the list retrieved from datastore to a list of usable long values
   *
   * @param dislikedMentorKeyList the list of objects from datastore
   * @return the list of long values
   */
  private static Set<Long> getDislikedSetFromProperty(Collection<Object> dislikedMentorKeyList) {
    return dislikedMentorKeyList == null
        ? new HashSet<Long>()
        : (Set<Long>)
            dislikedMentorKeyList.stream().map(key -> (long) key).collect(Collectors.toSet());
  }

  /**
   * @param servedMentorKeyList the list of served mentor key objects from datastore
   * @return the sorted set of long values
   */
  private static SortedSet<Long> getServedSetFromProperty(Collection<Object> servedMentorKeyList) {
    return servedMentorKeyList == null
        ? new TreeSet<Long>()
        : (SortedSet<Long>)
            servedMentorKeyList.stream()
                .map(key -> (long) key)
                .collect(Collectors.toCollection(TreeSet::new));
  }

  /**
   * Safe wrapper for getting long value from an entity
   *
   * @param longProperty the datastore object for the long value
   * @return the numeric value in Long
   */
  private static Long getLongFromProperty(Object longProperty) {
    return longProperty == null ? 0 : (Long) longProperty;
  }

  /**
   * Safe wrapper for getting string value from an entity
   *
   * @param stringProperty the datastore object for the string
   * @return string literal
   */
  private static String getStringFromProperty(Object stringProperty) {
    return stringProperty == null ? "" : (String) stringProperty;
  }
  /**
   * adds a mentor's key to the list of keys for mentors that the mentee does not want to work with
   * updates lastDislikedMentorKey for recommendation ranking
   *
   * @param mentor the mentor that the mentee doesn't want to work with
   * @return boolean of whether or not the mentor was added (false if already disliked)
   */
  public boolean dislikeMentor(Mentor mentor) {
    lastDislikedMentorKey = mentor.getDatastoreKey();
    servedMentorKeys.remove(mentor.getDatastoreKey());
    return dislikedMentorKeys.add(mentor.getDatastoreKey());
  }

  /**
   * adds a mentor's key to the requestedMentorKeys to avoid recommending mentors already requested
   * updates lastRequestedMentorKey for recommendation ranking
   *
   * @param mentor the mentor that the mentee requested
   * @return boolean of whether or not the mentor was added
   */
  public boolean requestMentor(Mentor mentor) {
    lastRequestedMentorKey = mentor.getDatastoreKey();
    return servedMentorKeys.remove(mentor.getDatastoreKey());
  }

  public boolean saveServedMentorKey(Long servedMentorKey) {
    return servedMentorKeys.add(servedMentorKey);
  }

  @Override
  public void copyProfileData(UserAccount contentUser) {
    super.copyProfileData(contentUser);
    if (contentUser instanceof Mentee) {
      Mentee contentMentee = (Mentee) contentUser;
      this.goal = contentMentee.goal;
      this.desiredMeetingFrequency = contentMentee.desiredMeetingFrequency;
      this.desiredMentorType = contentMentee.desiredMentorType;
    }
  }

  protected void copyKeyData(Mentee oldMentee) {
    this.dislikedMentorKeys = oldMentee.getDislikedMentorKeys();
    this.servedMentorKeys = oldMentee.getServedMentorKeys();
    this.encodedCursor = oldMentee.getEncodedCursor();
    this.lastRequestedMentorKey = oldMentee.getLastRequestedMentorKey();
    this.lastDislikedMentorKey = oldMentee.getLastDislikedMentorKey();
  }

  @Override
  public boolean looselyEquals(UserAccount other) {
    return super.looselyEquals(other)
        && this.goal == ((Mentee) other).goal
        && this.desiredMentorType == ((Mentee) other).desiredMentorType
        && this.desiredMeetingFrequency == ((Mentee) other).desiredMeetingFrequency
        && this.dislikedMentorKeys.equals(((Mentee) other).dislikedMentorKeys);
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

  public SortedSet<Long> getServedMentorKeys() {
    return this.servedMentorKeys;
  }

  public Long getLastRequestedMentorKey() {
    return lastRequestedMentorKey;
  }

  public Long getLastDislikedMentorKey() {
    return lastDislikedMentorKey;
  }

  public String getEncodedCursor() {
    return encodedCursor;
  }

  public boolean setEncodedCursor(String newCursor) {
    encodedCursor = newCursor;
    return (encodedCursor == newCursor);
  }

  public int similarityWithMentor(Mentor mentor) {
    int result = 0;
    if (mentor.getFocusList().contains(goal)) result += ServletUtils.SIMILARITY_SCORE_HIGH;
    if (mentor.getEducationLevel().ordinal() > super.getEducationLevel().ordinal())
      result += ServletUtils.SIMILARITY_SCORE_HIGH;
    if (mentor.getGender() == super.getGender()) result += ServletUtils.SIMILARITY_SCORE_HIGH;
    if (mentor.getTimezone() == super.getTimezone() || mentor.getCountry() == super.getCountry())
      result += ServletUtils.SIMILARITY_SCORE_LOW;
    if (mentor.isFirstGen() == super.isFirstGen()) result += ServletUtils.SIMILARITY_SCORE_LOW;
    return result;
  }

  public static class Builder extends UserAccount.Builder<Builder> {
    private Topic goal;
    private MeetingFrequency desiredMeetingFrequency;
    private Set<Long> dislikedMentorKeys;
    private MentorType desiredMentorType;
    private SortedSet<Long> servedMentorKeys;
    private Long lastDislikedMentorKey;
    private Long lastRequestedMentorKey;
    public String encodedCursor;

    public static Builder newBuilder() {
      return new Builder();
    }

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

    public Builder servedMentorKeys(SortedSet<Long> servedMentorKeys) {
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

    public Builder encodedCursor(String encodedCursor) {
      this.encodedCursor = encodedCursor;
      return this;
    }

    public Mentee build() {
      return new Mentee(this);
    }
  }
}
