package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
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
  }

  public Mentor(Entity entity) {
    super(entity);
    this.visibility = (boolean) entity.getProperty("visibility");
    this.focusList = getFocusListFromProperty((Collection) entity.getProperty("focusList"));
    this.mentorType = MentorType.values()[(int) entity.getProperty("mentorType")];
  }

  public Entity convertToEntity() {
    Entity entity = super.convertToEntity();
    entity.setProperty("visibility", visibility);
    entity.setProperty(
        "focusList", focusList.stream().map(focus -> focus.ordinal()).collect(Collectors.toList()));
    entity.setProperty("mentorType", mentorType.ordinal());
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
