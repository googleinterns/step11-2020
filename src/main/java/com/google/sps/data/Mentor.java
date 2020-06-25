package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import java.util.Collection;
import java.util.stream.Collectors;

public class Mentor extends UserAccount {

  private static final String VISIBILITY = "visibility";
  private static final String FOCUS_LIST = "focusList";
  private static final String MENTOR_TYPE = "mentorType";

  private boolean visibility;
  private Collection<Topic> focusList;
  private MentorType mentorType;

  private Mentor(Builder builder) {
    super(builder);
    this.visibility = builder.visibility;
    this.focusList = builder.focusList;
    this.mentorType = builder.mentorType;
  }

  public Mentor(Entity entity) {
    super(entity);
    this.visibility = (boolean) entity.getProperty(VISIBILITY);
    this.focusList = getFocusListFromProperty((Collection) entity.getProperty(FOCUS_LIST));
    this.mentorType = MentorType.values()[(int) entity.getProperty(MENTOR_TYPE)];
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty(VISIBILITY, visibility);
    entity.setProperty(
        FOCUS_LIST, focusList.stream().map(focus -> focus.ordinal()).collect(Collectors.toList()));
    entity.setProperty(MENTOR_TYPE, mentorType.ordinal());
    return entity;
  }

  private static Collection<Topic> getFocusListFromProperty(Collection<Object> focusEnumIndexList) {
    return (Collection<Topic>)
        focusEnumIndexList.stream()
            .map(index -> Topic.values()[(int) index])
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
