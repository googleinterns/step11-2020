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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.sps.util.ParameterConstants;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * This class represents a generic user and all their related data. UserAccount can only be
 * instantiated as a subclass (Mentee or Mentor).
 *
 * @author guptamudit
 * @author tquintanilla
 * @author sylviaziyuz
 * @version 1.0
 */
public abstract class UserAccount implements DatastoreEntity {
  private long datastoreKey;
  private long age;
  private boolean keyInitialized;
  private String userID;
  private String email;
  private String name;
  private Date dateOfBirth;
  private Country country;
  private Language language;
  private TimeZone timezone;
  private Collection<Ethnicity> ethnicityList;
  private String ethnicityOther;
  private Gender gender;
  private String genderOther;
  private boolean firstGen;
  private boolean lowIncome;
  private EducationLevel educationLevel;
  private String educationLevelOther;
  private String description;
  private String profilePicBlobKey;
  private UserType userType;
  private boolean isFakeUser;

  public UserAccount(
      String userID,
      String email,
      String name,
      Date dateOfBirth,
      Country country,
      Language language,
      TimeZone timezone,
      Collection<Ethnicity> ethnicityList,
      String ethnicityOther,
      Gender gender,
      String genderOther,
      boolean firstGen,
      boolean lowIncome,
      EducationLevel educationLevel,
      String educationLevelOther,
      String description,
      String profilePicBlobKey,
      UserType userType,
      boolean isFakeUser) {
    this.userID = userID;
    this.email = email;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.country = country;
    this.language = language;
    this.timezone = timezone;
    this.ethnicityList = ethnicityList;
    this.ethnicityOther = ethnicityOther;
    this.gender = gender;
    this.genderOther = genderOther;
    this.firstGen = firstGen;
    this.lowIncome = lowIncome;
    this.educationLevel = educationLevel;
    this.educationLevelOther = educationLevelOther;
    this.description = description;
    this.profilePicBlobKey = profilePicBlobKey;
    this.userType = userType;
    this.isFakeUser = isFakeUser;
  }

  protected UserAccount(Builder<?> builder) {
    this(
        builder.userID,
        builder.email,
        builder.name,
        builder.dateOfBirth,
        builder.country,
        builder.language,
        builder.timezone,
        builder.ethnicityList,
        builder.ethnicityOther,
        builder.gender,
        builder.genderOther,
        builder.firstGen,
        builder.lowIncome,
        builder.educationLevel,
        builder.educationLevelOther,
        builder.description,
        builder.profilePicBlobKey,
        builder.userType,
        builder.isFakeUser);
    this.keyInitialized = builder.keyInitialized;
    if (builder.keyInitialized) {
      this.datastoreKey = builder.datastoreKey;
    } else {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      KeyRange keyRange = datastore.allocateIds(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT, 1);
      this.datastoreKey = keyRange.getStart().getId();
    }
  }

  public UserAccount(Entity entity) {
    this.datastoreKey = entity.getKey().getId();
    this.keyInitialized = true;
    this.userID = (String) entity.getProperty(ParameterConstants.USER_ID);
    this.email = (String) entity.getProperty(ParameterConstants.EMAIL);
    this.name = (String) entity.getProperty(ParameterConstants.NAME);
    this.dateOfBirth = (Date) entity.getProperty(ParameterConstants.DATE_OF_BIRTH);
    this.country = Country.valueOf((String) entity.getProperty(ParameterConstants.COUNTRY));
    this.language = Language.valueOf((String) entity.getProperty(ParameterConstants.LANGUAGE));
    this.timezone = TimeZone.valueOf((String) entity.getProperty(ParameterConstants.TIMEZONE));
    this.ethnicityList =
        getEthnicityListFromProperty((Collection) entity.getProperty(ParameterConstants.ETHNICITY));
    this.ethnicityOther = (String) entity.getProperty(ParameterConstants.ETHNICITY_OTHER);
    this.gender = Gender.valueOf((String) entity.getProperty(ParameterConstants.GENDER));
    this.genderOther = (String) entity.getProperty(ParameterConstants.GENDER_OTHER);
    this.firstGen = (boolean) entity.getProperty(ParameterConstants.FIRST_GEN);
    this.lowIncome = (boolean) entity.getProperty(ParameterConstants.LOW_INCOME);
    this.educationLevel =
        EducationLevel.valueOf((String) entity.getProperty(ParameterConstants.EDUCATION_LEVEL));
    this.educationLevelOther =
        (String) entity.getProperty(ParameterConstants.EDUCATION_LEVEL_OTHER);
    this.description = (String) entity.getProperty(ParameterConstants.DESCRIPTION);
    this.profilePicBlobKey = (String) entity.getProperty(ParameterConstants.PROFILE_PIC_BLOB_KEY);
    this.userType = UserType.valueOf((String) entity.getProperty(ParameterConstants.USER_TYPE));
    this.isFakeUser = (boolean) entity.getProperty(ParameterConstants.IS_FAKE_USER);
  }

  public static UserAccount fromEntity(Entity entity) {
    return entity == null
        ? null
        : UserType.valueOf((String) entity.getProperty(ParameterConstants.USER_TYPE))
                == UserType.MENTEE
            ? new Mentee(entity)
            : new Mentor(entity);
  }

  /** This method ensures that all class fields are properly initialized. If they aren't, fix it. */
  protected void sanitizeValues() {
    if (this.ethnicityList == null) {
      this.ethnicityList = new ArrayList<>();
    }
    updateAge();
  }

  public Entity convertToEntity() {
    if (!this.keyInitialized && this.datastoreKey == 0) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      KeyRange keyRange = datastore.allocateIds(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT, 1);
      this.datastoreKey = keyRange.getStart().getId();
    }
    Key key = KeyFactory.createKey(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT, this.datastoreKey);
    Entity entity = new Entity(key);
    entity.setProperty(ParameterConstants.USER_ID, this.userID);
    entity.setProperty(ParameterConstants.EMAIL, this.email);
    entity.setProperty(ParameterConstants.NAME, this.name);
    entity.setProperty(ParameterConstants.DATE_OF_BIRTH, this.dateOfBirth);
    entity.setProperty(ParameterConstants.COUNTRY, this.country.name());
    entity.setProperty(ParameterConstants.LANGUAGE, this.language.name());
    entity.setProperty(ParameterConstants.TIMEZONE, this.timezone.name());
    entity.setProperty(
        ParameterConstants.ETHNICITY,
        ethnicityList.stream().map(ethnicity -> ethnicity.name()).collect(Collectors.toList()));
    entity.setProperty(ParameterConstants.ETHNICITY_OTHER, this.ethnicityOther);
    entity.setProperty(ParameterConstants.GENDER, this.gender.name());
    entity.setProperty(ParameterConstants.GENDER_OTHER, this.genderOther);
    entity.setProperty(ParameterConstants.FIRST_GEN, this.firstGen);
    entity.setProperty(ParameterConstants.LOW_INCOME, this.lowIncome);
    entity.setProperty(ParameterConstants.EDUCATION_LEVEL, this.educationLevel.name());
    entity.setProperty(ParameterConstants.EDUCATION_LEVEL_OTHER, this.educationLevelOther);
    entity.setProperty(ParameterConstants.DESCRIPTION, this.description);
    entity.setProperty(ParameterConstants.PROFILE_PIC_BLOB_KEY, this.profilePicBlobKey);
    entity.setProperty(ParameterConstants.USER_TYPE, this.userType.name());
    entity.setProperty(ParameterConstants.IS_FAKE_USER, this.isFakeUser);
    return entity;
  }

  /**
   * Converts the list retrieved from datastore to a list of usable Ethnicity objects
   *
   * @param ethnicityEnumIndexList the list off objects from datastore
   * @return the list of ethnicity objects
   */
  private static Collection<Ethnicity> getEthnicityListFromProperty(
      Collection<Object> ethnicityEnumIndexList) {
    return ethnicityEnumIndexList == null
        ? new ArrayList<Ethnicity>()
        : (Collection<Ethnicity>)
            ethnicityEnumIndexList.stream()
                .map(name -> Ethnicity.valueOf((String) name))
                .collect(Collectors.toList());
  }

  /**
   * This copies all the profile related fields from one user to another
   *
   * @param contentUser the UserAccount from which to copy data into this UserAccount
   */
  public void copyProfileData(UserAccount contentUser) {
    this.userID = contentUser.userID;
    this.email = contentUser.email;
    this.name = contentUser.name;
    this.dateOfBirth = contentUser.dateOfBirth;
    this.country = contentUser.country;
    this.language = contentUser.language;
    this.timezone = contentUser.timezone;
    this.ethnicityList = contentUser.ethnicityList;
    this.ethnicityOther = contentUser.ethnicityOther;
    this.gender = contentUser.gender;
    this.genderOther = contentUser.genderOther;
    this.firstGen = contentUser.firstGen;
    this.lowIncome = contentUser.lowIncome;
    this.educationLevel = contentUser.educationLevel;
    this.educationLevelOther = contentUser.educationLevelOther;
    this.description = contentUser.description;
    if (contentUser.profilePicBlobKey != null && contentUser.profilePicBlobKey != "") {
      this.profilePicBlobKey = contentUser.profilePicBlobKey;
    }
    this.userType = contentUser.userType;
    this.isFakeUser = contentUser.isFakeUser;
  }

  public boolean looselyEquals(UserAccount other) {
    return this.userID.equals(other.userID)
        && this.email.equals(other.email)
        && this.name.equals(other.name)
        && this.dateOfBirth.equals(other.dateOfBirth)
        && this.country == other.country
        && this.language == other.language
        && this.timezone == other.timezone
        && this.ethnicityList.equals(other.ethnicityList)
        && this.ethnicityOther.equals(other.ethnicityOther)
        && this.gender == other.gender
        && this.genderOther.equals(other.genderOther)
        && this.firstGen == other.firstGen
        && this.lowIncome == other.lowIncome
        && this.educationLevel == other.educationLevel
        && this.educationLevelOther.equals(other.educationLevelOther)
        && this.description.equals(other.description)
        && this.userType == other.userType;
  }

  public long getDatastoreKey() {
    return datastoreKey;
  }

  public boolean isKeyInitialized() {
    return keyInitialized;
  }

  public String getUserID() {
    return userID;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public TimeZone getTimezone() {
    return timezone;
  }

  public Collection<Ethnicity> getEthnicityList() {
    return ethnicityList;
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

  public String getProfilePicBlobKey() {
    return profilePicBlobKey;
  }

  public UserType getUserType() {
    return userType;
  }

  public boolean getIsFakeUser() {
    return isFakeUser;
  }

  private void updateAge() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateOfBirth);
    LocalDate birthdayLocal =
        LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DATE));
    LocalDate today = LocalDate.now();
    if (today != null && dateOfBirth != null) {
      this.age = Period.between(birthdayLocal, today).getYears();
    }
  }

  public Long getAge() {
    return age;
  }

  public abstract static class Builder<T extends Builder<T>> {
    private static long datastoreKey;
    private static boolean keyInitialized = false;
    private static String userID;
    private static String email;
    private static String name;
    private static Date dateOfBirth;
    private static Country country;
    private static Language language;
    private static TimeZone timezone;
    private static Collection<Ethnicity> ethnicityList;
    private static String ethnicityOther;
    private static Gender gender;
    private static String genderOther;
    private static boolean firstGen;
    private static boolean lowIncome;
    private static EducationLevel educationLevel;
    private static String educationLevelOther;
    private static String description;
    private static String profilePicBlobKey;
    private static UserType userType;
    private static boolean isFakeUser;

    protected Builder() {}

    public T datastoreKey(long datastoreKey) {
      this.datastoreKey = datastoreKey;
      this.keyInitialized = true;
      return (T) this;
    }

    public T userID(String userID) {
      this.userID = userID;
      return (T) this;
    }

    public T email(String email) {
      this.email = email;
      return (T) this;
    }

    public T name(String name) {
      this.name = name;
      return (T) this;
    }

    public T dateOfBirth(Date dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
      return (T) this;
    }

    public T country(Country country) {
      this.country = country;
      return (T) this;
    }

    public T language(Language language) {
      this.language = language;
      return (T) this;
    }

    public T timezone(TimeZone timezone) {
      this.timezone = timezone;
      return (T) this;
    }

    public T ethnicityList(Collection<Ethnicity> ethnicityList) {
      this.ethnicityList = ethnicityList;
      return (T) this;
    }

    public T ethnicityOther(String ethnicityOther) {
      this.ethnicityOther = ethnicityOther;
      return (T) this;
    }

    public T gender(Gender gender) {
      this.gender = gender;
      return (T) this;
    }

    public T genderOther(String genderOther) {
      this.genderOther = genderOther;
      return (T) this;
    }

    public T firstGen(boolean firstGen) {
      this.firstGen = firstGen;
      return (T) this;
    }

    public T lowIncome(boolean lowIncome) {
      this.lowIncome = lowIncome;
      return (T) this;
    }

    public T educationLevel(EducationLevel educationLevel) {
      this.educationLevel = educationLevel;
      return (T) this;
    }

    public T educationLevelOther(String educationLevelOther) {
      this.educationLevelOther = educationLevelOther;
      return (T) this;
    }

    public T description(String description) {
      this.description = description;
      return (T) this;
    }

    public T profilePicBlobKey(String profilePicBlobKey) {
      this.profilePicBlobKey = profilePicBlobKey;
      return (T) this;
    }

    public T userType(UserType userType) {
      this.userType = userType;
      return (T) this;
    }

    public T isFakeUser(boolean isFakeUser) {
      this.isFakeUser = isFakeUser;
      return (T) this;
    }
  }
}
