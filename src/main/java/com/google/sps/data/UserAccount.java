package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.Date;

class UserAccount {
  public static final String ENTITY_TYPE = "UserAccount";

  private long datastoreKey;
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
    this.datastoreKey = datastoreKey;
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
    this.userID = (String) entity.getProperty("userID");
    this.email = (String) entity.getProperty("email");
    this.name = (String) entity.getProperty("name");
    this.dateOfBirth = (Date) entity.getProperty("dateOfBirth");
    this.country = Country.values()[(int) entity.getProperty("country")];
    this.language = Language.values()[(int) entity.getProperty("language")];
    this.timezone = TimeZone.values()[(int) entity.getProperty("timezone")];
    this.ethnicity = Ethnicity.values()[(int) entity.getProperty("ethnicity")];
    this.ethnicityOther = (String) entity.getProperty("ethnicityOther");
    this.gender = Gender.values()[(int) entity.getProperty("gender")];
    this.genderOther = (String) entity.getProperty("genderOther");
    this.firstGen = (boolean) entity.getProperty("firstGen");
    this.lowIncome = (boolean) entity.getProperty("lowIncome");
    this.educationLevel = EducationLevel.values()[(int) entity.getProperty("educationLevel")];
    this.educationLevelOther = (String) entity.getProperty("educationLevelOther");
    this.description = (String) entity.getProperty("description");
    this.userType = UserType.values()[(int) entity.getProperty("userType")];
  }

  public Entity convertToEntity() {
    Key key = KeyFactory.createKey(ENTITY_TYPE, this.datastoreKey);
    Entity entity = new Entity(key);
    entity.setProperty("userID", this.userID);
    entity.setProperty("email", this.email);
    entity.setProperty("name", this.name);
    entity.setProperty("dateOfBirth", this.dateOfBirth);
    entity.setProperty("country", this.country.ordinal());
    entity.setProperty("language", this.language.ordinal());
    entity.setProperty("timezone", this.timezone.ordinal());
    entity.setProperty("ethnicity", this.ethnicity);
    entity.setProperty("ethnicityOther", this.ethnicityOther);
    entity.setProperty("gender", this.gender);
    entity.setProperty("genderOther", this.genderOther);
    entity.setProperty("firstGen", this.firstGen);
    entity.setProperty("lowIncome", this.lowIncome);
    entity.setProperty("educationLevel", this.educationLevel.ordinal());
    entity.setProperty("educationLevelOther", this.educationLevelOther);
    entity.setProperty("description", this.description);
    entity.setProperty("userType", this.userType.ordinal());
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
