package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import java.util.Date;

class UserAccount {

  private Key datastoreKey;
  private String userID;
  private String email;
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private Country country;
  private Language language;
  private TimeZone timeZone;
  private Ethnicity ethnicity;
  private String ethnicityOther;
  private Gender gender;
  private String genderOther;
  private boolean firstGen;
  private boolean lowIncome;
  private EducationLevel educationLevel;
  private String educationLevelOther;
  private String description;

  public UserAccount(String nickname, String userID) {
    this.nickname = nickname;
    this.userID = userID;
  }

  public UserAccount(Entity entity) {
    this.firstName = (String) entity.getProperty("firstName");
    this.userID = (String) entity.getProperty("userID");
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getUserID() {
    return userID;
  }

  public String getEmail() {
    return email;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public Country getCountry() {
    return country;
  }

  public Language getLanguage() {
    return language;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public Ethnicity getEthnicity() {
    return ethnicity;
  }

  public String getEthnicityOther() {
    return ethnicityOther;
  }

  public Gender getGender() {
    return gender;
  }

  public String getGenderOther() {
    return genderOther;
  }

  public boolean isFirstGen() {
    return firstGen;
  }

  public boolean isLowIncome() {
    return lowIncome;
  }

  public EducationLevel getEducationLevel() {
    return educationLevel;
  }

  public String getEducationLevelOther() {
    return educationLevelOther;
  }

  public String getDescription() {
    return description;
  }

  public Key getDataStoreKey() {
    return datastoreKey;
  }
}
