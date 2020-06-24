package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public class Mentor extends UserAccount {

  private boolean visibility;
  private Collection<Topic> focusList;
  private MentorType mentorType;

  public Mentor(Map<String, Object> accountData) {
    super(accountData);
    this.visibility = accountData.get("visibility");
    this.focusList = accountData.get("focusList");
    this.mentorType = accountData.get("mentorType");
  }

  public Mentor(Entity entity) {
    super(entity);
    this.visibility = (boolean) entity.getProperty("visibility");
    this.focusList = getFocusListFromProperty(entity.getProperty("focusList"));
    this.mentorType = MentorType.values()[(int) entity.getProperty("mentorType")];
  }

  public String getVisibility() {
    return visibility;
  }

  public String getFocusList() {
    return focusList;
  }

  public String getMentorType() {
    return mentorType;
  }
}
