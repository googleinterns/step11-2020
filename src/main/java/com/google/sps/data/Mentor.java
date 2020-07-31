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
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This class represents a Mentor user and their mentor-specific data. Other data is held within the
 * super class, UserAccount.
 *
 * @author guptamudit
 * @author sylviaziyuz
 * @author tquintanilla
 * @version 1.0
 */
public class Mentor extends UserAccount implements DatastoreEntity {
  private boolean visibility;
  private Collection<Topic> focusList;
  private MentorType mentorType;

  private Mentor(Builder builder) {
    super(builder);
    this.visibility = builder.visibility;
    this.focusList = builder.focusList;
    this.mentorType = builder.mentorType;
    this.sanitizeValues();
  }

  public Mentor(Entity entity) {
    super(entity);
    this.visibility = (boolean) entity.getProperty(ParameterConstants.MENTOR_VISIBILITY);
    this.focusList =
        getFocusListFromProperty(
            (Collection) entity.getProperty(ParameterConstants.MENTOR_FOCUS_LIST));
    this.mentorType =
        MentorType.valueOf((String) entity.getProperty(ParameterConstants.MENTOR_TYPE));
    this.sanitizeValues();
  }

  @Override
  protected void sanitizeValues() {
    super.sanitizeValues();
    if (this.focusList == null) {
      this.focusList = new ArrayList<>();
    }
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(ParameterConstants.MENTOR_VISIBILITY, visibility);
    entity.setProperty(
        ParameterConstants.MENTOR_FOCUS_LIST,
        focusList.stream().map(focus -> focus.name()).collect(Collectors.toList()));
    entity.setProperty(ParameterConstants.MENTOR_TYPE, mentorType.name());
    return entity;
  }

  /**
   * converts the list retrieved from datastore to a list of usable Topic objects
   *
   * @param focusEnumIndexList the list off objects from datastore
   * @return the list of topic objects
   */
  private static Collection<Topic> getFocusListFromProperty(Collection<Object> focusEnumIndexList) {
    return focusEnumIndexList == null
        ? new ArrayList<Topic>()
        : (Collection<Topic>)
            focusEnumIndexList.stream()
                .map(name -> Topic.valueOf((String) name))
                .collect(Collectors.toList());
  }

  @Override
  public void copyProfileData(UserAccount contentUser) {
    super.copyProfileData(contentUser);
    if (contentUser instanceof Mentor) {
      Mentor contentMentor = (Mentor) contentUser;
      this.visibility = contentMentor.visibility;
      this.focusList = contentMentor.focusList;
      this.mentorType = contentMentor.mentorType;
    }
  }

  @Override
  public boolean looselyEquals(UserAccount other) {
    return super.looselyEquals(other)
        && this.visibility == ((Mentor) other).visibility
        && this.focusList.equals(((Mentor) other).focusList)
        && this.mentorType == ((Mentor) other).mentorType;
  }

  public boolean getVisibility() {
    return visibility;
  }

  public Collection<Topic> getFocusList() {
    return focusList;
  }

  public MentorType getMentorType() {
    return mentorType;
  }

  public int similarity(Mentor other) {
    int result = 0;
    if (other.getTimezone() == super.getTimezone() || other.getCountry() == super.getCountry())
      result += ServletUtils.SIMILARITY_SCORE_HIGH;
    if (other.getGender() == super.getGender()) result += ServletUtils.SIMILARITY_SCORE_HIGH;
    if (other.isFirstGen() == super.isFirstGen()) result += ServletUtils.SIMILARITY_SCORE_HIGH;
    if (other.isLowIncome() == super.isLowIncome()) result += ServletUtils.SIMILARITY_SCORE_LOW;
    if (other.getEducationLevel().ordinal() == super.getEducationLevel().ordinal())
      result += ServletUtils.SIMILARITY_SCORE_LOW;
    return result;
  }

  public static class Builder extends UserAccount.Builder<Builder> {
    private boolean visibility;
    private Collection<Topic> focusList;
    private MentorType mentorType;

    public static Builder newBuilder() {
      return new Builder();
    }

    public Builder visibility(boolean visibility) {
      this.visibility = visibility;
      return this;
    }

    public Builder focusList(Collection<Topic> focusList) {
      this.focusList = focusList;
      return this;
    }

    public Builder mentorType(MentorType mentorType) {
      this.mentorType = mentorType;
      return this;
    }

    public Mentor build() {
      return new Mentor(this);
    }
  }
}
