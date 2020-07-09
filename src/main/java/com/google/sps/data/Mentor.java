package com.google.sps.data;

import static java.lang.Math.toIntExact;

import com.google.appengine.api.datastore.Entity;
import com.google.sps.util.ParameterConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Mentor extends UserAccount {
  private boolean visibility;
  private Collection<Topic> focusList;
  private MentorType mentorType;

  private Mentor(Builder builder) {
    super(builder);
    this.visibility = builder.visibility;
    this.focusList = builder.focusList;
    this.mentorType = builder.mentorType;
    this.validate();
  }

  public Mentor(Entity entity) {
    super(entity);
    this.visibility = (boolean) entity.getProperty(ParameterConstants.MENTOR_VISIBILITY);
    this.focusList =
        getFocusListFromProperty(
            (Collection) entity.getProperty(ParameterConstants.MENTOR_FOCUS_LIST));
    this.mentorType =
        MentorType.values()[toIntExact((long) entity.getProperty(ParameterConstants.MENTOR_TYPE))];
    this.validate();
  }

  @Override
  protected void validate() {
    super.validate();
    if (this.focusList == null) {
      this.focusList = new ArrayList<>();
    }
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(ParameterConstants.MENTOR_VISIBILITY, visibility);
    entity.setProperty(
        ParameterConstants.MENTOR_FOCUS_LIST,
        focusList.stream().map(focus -> focus.ordinal()).collect(Collectors.toList()));
    entity.setProperty(ParameterConstants.MENTOR_TYPE, mentorType.ordinal());
    return entity;
  }

  private static Collection<Topic> getFocusListFromProperty(Collection<Object> focusEnumIndexList) {
    return focusEnumIndexList == null
        ? new ArrayList<Topic>()
        : (Collection<Topic>)
            focusEnumIndexList.stream()
                .map(index -> Topic.values()[toIntExact((long) index)])
                .collect(Collectors.toList());
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

  public static class Builder extends UserAccount.Builder<Builder> {
    private boolean visibility;
    private Collection<Topic> focusList;
    private MentorType mentorType;

    public Builder() {}

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
