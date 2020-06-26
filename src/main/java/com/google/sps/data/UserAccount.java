package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.Date;
import java.util.TimeZone;

class UserAccount {
  public static final String ENTITY_TYPE = "UserAccount";

  private static final String USER_ID = "userID";
  private static final String EMAIL = "email";
  private static final String NAME = "name";
  private static final String DATE_OF_BIRTH = "dateOfBirth";
  private static final String COUNTRY = "country";
  private static final String LANGUAGE = "language";
  private static final String TIMEZONE = "timezone";
  private static final String ETHNICITY = "ethnicity";
  private static final String ETHNICITY_OTHER = "ethnicityOther";
  private static final String GENDER = "gender";
  private static final String GENDER_OTHER = "genderOther";
  private static final String FIRST_GEN = "firstGen";
  private static final String LOW_INCOME = "lowIncome";
  private static final String EDUCATION_LEVEL = "educationLevel";
  private static final String EDUCATION_LEVEL_OTHER = "educationLevelOther";
  private static final String DESCRIPTION = "description";
  private static final String USER_TYPE = "userType";

  private long datastoreKey;
  private boolean keyInitialized;
  private String userID;
  private String email;
  private String name;
  private Date dateOfBirth;
  private Country country;
  private Language language;
  private TimeZone timezone;
  private Ethnicity ethnicity;
  private String ethnicityOther;
  private Gender gender;
  private String genderOther;
  private boolean firstGen;
  private boolean lowIncome;
  private EducationLevel educationLevel;
  private String educationLevelOther;
  private String description;
  private UserType userType;

  private UserAccount(
      long datastoreKey,
      String userID,
      String email,
      String name,
      Date dateOfBirth,
      Country country,
      Language language,
      TimeZone timezone,
      Ethnicity ethnicity,
      String ethnicityOther,
      Gender gender,
      String genderOther,
      boolean firstGen,
      boolean lowIncome,
      EducationLevel educationLevel,
      String educationLevelOther,
      String description,
      UserType userType) {
    DatastireService datastore = DatastoreServiceFactory.getDatastoreService();
    KeyRange keyRange = datastore.allocateIDs(ENTITY_TYPE, 1);
    this.datastoreKey = keyRange.getStart().getId();
    this.userID = userID;
    this.email = email;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.country = country;
    this.language = language;
    this.timezone = timezone;
    this.ethnicity = ethnicity;
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
        builder.datastoreKey,
        builder.userID,
        builder.email,
        builder.name,
        builder.dateOfBirth,
        builder.country,
        builder.language,
        builder.timezone,
        builder.ethnicity,
        builder.ethnicityOther,
        builder.gender,
        builder.genderOther,
        builder.firstGen,
        builder.lowIncome,
        builder.educationLevel,
        builder.educationLevelOther,
        builder.description,
        builder.userType);
  }

  public UserAccount(Entity entity) {
    this.datastoreKey = entity.getKey().getId();
    this.userID = (String) entity.getProperty(USER_ID);
    this.email = (String) entity.getProperty(EMAIL);
    this.name = (String) entity.getProperty(NAME);
    this.dateOfBirth = (Date) entity.getProperty(DATE_OF_BIRTH);
    this.country = Country.values()[(int) entity.getProperty(COUNTRY)];
    this.language = Language.values()[(int) entity.getProperty(LANGUAGE)];
    this.timezone = TimeZone.getTimeZone((String) entity.getProperty(TIMEZONE));
    this.ethnicity = Ethnicity.values()[(int) entity.getProperty(ETHNICITY)];
    this.ethnicityOther = (String) entity.getProperty(ETHNICITY_OTHER);
    this.gender = Gender.values()[(int) entity.getProperty(GENDER)];
    this.genderOther = (String) entity.getProperty(GENDER_OTHER);
    this.firstGen = (boolean) entity.getProperty(FIRST_GEN);
    this.lowIncome = (boolean) entity.getProperty(LOW_INCOME);
    this.educationLevel = EducationLevel.values()[(int) entity.getProperty(EDUCATION_LEVEL)];
    this.educationLevelOther = (String) entity.getProperty(EDUCATION_LEVEL_OTHER);
    this.description = (String) entity.getProperty(DESCRIPTION);
    this.userType = UserType.values()[(int) entity.getProperty(USER_TYPE)];
  }

  public Entity convertToEntity() {
    Key key = KeyFactory.createKey(ENTITY_TYPE, this.datastoreKey);
    Entity entity = new Entity(key);
    entity.setProperty(USER_ID, this.userID);
    entity.setProperty(EMAIL, this.email);
    entity.setProperty(NAME, this.name);
    entity.setProperty(DATE_OF_BIRTH, this.dateOfBirth);
    entity.setProperty(COUNTRY, this.country.ordinal());
    entity.setProperty(LANGUAGE, this.language.ordinal());
    entity.setProperty(TIMEZONE, this.timezone.getID());
    entity.setProperty(ETHNICITY, this.ethnicity);
    entity.setProperty(ETHNICITY_OTHER, this.ethnicityOther);
    entity.setProperty(GENDER, this.gender);
    entity.setProperty(GENDER_OTHER, this.genderOther);
    entity.setProperty(FIRST_GEN, this.firstGen);
    entity.setProperty(LOW_INCOME, this.lowIncome);
    entity.setProperty(EDUCATION_LEVEL, this.educationLevel.ordinal());
    entity.setProperty(EDUCATION_LEVEL_OTHER, this.educationLevelOther);
    entity.setProperty(DESCRIPTION, this.description);
    entity.setProperty(USER_TYPE, this.userType.ordinal());
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

  public TimeZone getTimezone() {
    return timezone;
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

  public UserType getUserType() {
    return userType;
  }

  protected abstract static class Builder<T extends Builder<T>> {
    private static long datastoreKey;
    private static String userID;
    private static String email;
    private static String name;
    private static Date dateOfBirth;
    private static Country country;
    private static Language language;
    private static TimeZone timezone;
    private static Ethnicity ethnicity;
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
    ;

    public T datastoreKey(long datastoreKey) {
      this.datastoreKey = datastoreKey;
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

    public T ethnicity(Ethnicity ethnicity) {
      this.ethnicity = ethnicity;
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
