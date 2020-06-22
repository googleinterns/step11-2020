package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public class Mentor extends UserAccount {

  public static final String ENTITY_TYPE = "Mentor";

  private String dateOfBirth;
  private String focus;
  private String educationLevel;

  public Mentor(
      String nickname, String userID, String dateOfBirth, String focus, String educationLevel) {
    super(nickname, userID);
    this.dateOfBirth = dateOfBirth;
    this.focus = focus;
    this.educationLevel = educationLevel;
  }

  public Mentor(Entity entity) {
    super(entity);
    this.dateOfBirth = (String) entity.getProperty("dateOfBirth");
    this.focus = (String) entity.getProperty("focus");
    this.educationLevel = (String) entity.getProperty("educationLevel");
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public String getfocus() {
    return focus;
  }

  public String getEducationLevel() {
    return educationLevel;
  }
}
