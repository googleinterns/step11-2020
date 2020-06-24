package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import java.util.Date;

class UserAccount {
  public static final String ENTITY_TYPE = "UserAccount";

  private Key datastoreKey;
  private String userID;
  private String email;
  private String name;
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
  private UserType userType;

  public UserAccount(Map<String, Object> accountData) {
    this.userID = accountData.get("userID");
    this.email = accountData.get("email");
    this.name = accountData.get("name");
    this.dateOfBirth = accountData.get("dateOfBirth");
    this.country = accountData.get("country");
    this.language = accountData.get("language");
    this.timeZone = accountData.get("timeZone");
    this.ethnicity = accountData.get("ethnicity");
    this.ethnicityOther = accountData.get("ethnicityOther");
    this.gender = accountData.get("gender");
    this.genderOther = accountData.get("genderOther");
    this.firstGen = accountData.get("firstGen");
    this.lowIncome = accountData.get("lowIncome");
    this.educationLevel = accountData.get("educationLevel");
    this.educationLevelOther = accountData.get("educationLevelOther");
    this.description = accountData.get("description");
    this.userType = accountData.get("userType");
  }

  public UserAccount(Entity entity) {
    this.userID = (String) entity.getProperty("userID");
    this.email =(String) entity.getProperty("email");
    this.name = (String) entity.getProperty("name");
    this.dateOfBirth = (Date) entity.getProperty("dateOfBirth");
    this.country = Country.values()[(int) entity.getProperty("country")];
    this.language = Language.values()[(int) entity.getProperty("language")];
    this.timeZone = TimeZone.values()[(int) entity.getProperty("timeZone")];
    this.ethnicity = Ethnicity.values()[(int) entity.getProperty("ethnicity")];
    this.ethnicityOther = (String) entity.getProperty("ethnicityOther");
    this.gender = Gender.values()[(int) entity.getProperty("gender")];
    this.genderOther = (String) entity.getProperty("genderOther");
    this.firstGen = (boolean) entity.getProperty("firstGen");
    this.lowIncome = (boolean) entity.getProperty("lowIncome");
    this.educationLevel = EducationLevel.values()[(int) entity.getProperty("educationLevel")];
    this.educationLevelOther = (String) entity.getProperty("educationLevelOther");
    this.description = (String) entity.getProperty(("description");
    this.userType = UserType.values()[(int) entity.getProperty("userType")];
  }

  public String getName() {
    return name;
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

  public UserType getUserType() {
    return userType;
  }

	public static String getENTITY_TYPE() {
		return ENTITY_TYPE;
	}

	public Key getDatastoreKey() {
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

	public UserType getUserType() {
		return userType;
	}
}
