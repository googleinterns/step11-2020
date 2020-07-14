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

package com.google.step.data;

import static java.lang.Math.toIntExact;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.step.util.ParameterConstants;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class UserAccount implements DatastoreEntity {
  private long datastoreKey;
  private boolean keyInitialized;
  private String userID;
  private String email;
  private String name;
  private Date dateOfBirth;
  private Country country;
  private Language language;
  private TimeZoneInfo timezone;
  private Collection<Ethnicity> ethnicityList;
  private String ethnicityOther;
  private Gender gender;
  private String genderOther;
  private boolean firstGen;
  private boolean lowIncome;
  private EducationLevel educationLevel;
  private String educationLevelOther;
  private String description;
  private UserType userType;

  public UserAccount(
      String userID,
      String email,
      String name,
      Date dateOfBirth,
      Country country,
      Language language,
      TimeZoneInfo timezone,
      Collection<Ethnicity> ethnicityList,
      String ethnicityOther,
      Gender gender,
      String genderOther,
      boolean firstGen,
      boolean lowIncome,
      EducationLevel educationLevel,
      String educationLevelOther,
      String description,
      UserType userType) {
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
    this.userType = userType;
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
        builder.userType);
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
    this.country =
        Country.values()[toIntExact((long) entity.getProperty(ParameterConstants.COUNTRY))];
    this.language =
        Language.values()[toIntExact((long) entity.getProperty(ParameterConstants.LANGUAGE))];
    this.timezone =
        new TimeZoneInfo(
            TimeZone.getTimeZone((String) entity.getProperty(ParameterConstants.TIMEZONE)));
    this.ethnicityList =
        getEthnicityListFromProperty((Collection) entity.getProperty(ParameterConstants.ETHNICITY));
    this.ethnicityOther = (String) entity.getProperty(ParameterConstants.ETHNICITY_OTHER);
    this.gender = Gender.values()[toIntExact((long) entity.getProperty(ParameterConstants.GENDER))];
    this.genderOther = (String) entity.getProperty(ParameterConstants.GENDER_OTHER);
    this.firstGen = (boolean) entity.getProperty(ParameterConstants.FIRST_GEN);
    this.lowIncome = (boolean) entity.getProperty(ParameterConstants.LOW_INCOME);
    this.educationLevel =
        EducationLevel.values()[
            toIntExact((long) entity.getProperty(ParameterConstants.EDUCATION_LEVEL))];
    this.educationLevelOther =
        (String) entity.getProperty(ParameterConstants.EDUCATION_LEVEL_OTHER);
    this.description = (String) entity.getProperty(ParameterConstants.DESCRIPTION);
    this.userType =
        UserType.values()[toIntExact((long) entity.getProperty(ParameterConstants.USER_TYPE))];
  }

  public static UserAccount fromEntity(Entity entity) {
    return entity == null
        ? null
        : UserType.values()[toIntExact((long) (entity.getProperty(ParameterConstants.USER_TYPE)))]
                == UserType.MENTEE
            ? new Mentee(entity)
            : new Mentor(entity);
  }

  public Entity convertToEntity() {
    Key key = KeyFactory.createKey(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT, this.datastoreKey);
    Entity entity = new Entity(key);
    entity.setProperty(ParameterConstants.USER_ID, this.userID);
    entity.setProperty(ParameterConstants.EMAIL, this.email);
    entity.setProperty(ParameterConstants.NAME, this.name);
    entity.setProperty(ParameterConstants.DATE_OF_BIRTH, this.dateOfBirth);
    entity.setProperty(ParameterConstants.COUNTRY, this.country.ordinal());
    entity.setProperty(ParameterConstants.LANGUAGE, this.language.ordinal());
    entity.setProperty(ParameterConstants.TIMEZONE, this.timezone.getID());
    entity.setProperty(
        ParameterConstants.ETHNICITY,
        ethnicityList.stream().map(ethnicity -> ethnicity.ordinal()).collect(Collectors.toList()));
    entity.setProperty(ParameterConstants.ETHNICITY_OTHER, this.ethnicityOther);
    entity.setProperty(ParameterConstants.GENDER, this.gender.ordinal());
    entity.setProperty(ParameterConstants.GENDER_OTHER, this.genderOther);
    entity.setProperty(ParameterConstants.FIRST_GEN, this.firstGen);
    entity.setProperty(ParameterConstants.LOW_INCOME, this.lowIncome);
    entity.setProperty(ParameterConstants.EDUCATION_LEVEL, this.educationLevel.ordinal());
    entity.setProperty(ParameterConstants.EDUCATION_LEVEL_OTHER, this.educationLevelOther);
    entity.setProperty(ParameterConstants.DESCRIPTION, this.description);
    entity.setProperty(ParameterConstants.USER_TYPE, this.userType.ordinal());
    return entity;
  }

  public long getDatastoreKey() {
    return datastoreKey;
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

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public Country getCountry() {
    return country;
  }

  public Language getLanguage() {
    return language;
  }

  public TimeZoneInfo getTimezone() {
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

  public UserType getUserType() {
    return userType;
  }

  private static Collection<Ethnicity> getEthnicityListFromProperty(
      Collection<Object> ethnicityEnumIndexList) {
    return (Collection<Ethnicity>)
        ethnicityEnumIndexList.stream()
            .map(index -> Ethnicity.values()[toIntExact((long) index)])
            .collect(Collectors.toList());
  }

  protected abstract static class Builder<T extends Builder<T>> {
    private static long datastoreKey;
    private static boolean keyInitialized = false;
    private static String userID;
    private static String email;
    private static String name;
    private static Date dateOfBirth;
    private static Country country;
    private static Language language;
    private static TimeZoneInfo timezone;
    private static Collection<Ethnicity> ethnicityList;
    private static String ethnicityOther;
    private static Gender gender;
    private static String genderOther;
    private static boolean firstGen;
    private static boolean lowIncome;
    private static EducationLevel educationLevel;
    private static String educationLevelOther;
    private static String description;
    private static UserType userType;

    public Builder() {}

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

    public T timezone(TimeZoneInfo timezone) {
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

    public T userType(UserType userType) {
      this.userType = userType;
      return (T) this;
    }

    public UserAccount build() {
      return new UserAccount(this);
    }
  }
}
