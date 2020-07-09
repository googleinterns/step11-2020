package com.google.sps.data;

/** Represents a user's gender
 */
public enum Gender {
  UNSPECIFIED(""),
  AGENDER("Agender"),
  ANDROGYNOUS("Androgynous"),
  BIGENDER("Bigender"),
  DEMIGIRL("Demigirl"),
  DEMIGUY("Demiguy"),
  FEMININE("Feminine"),
  FEMME("Femme"),
  GENDERQUEER("Genderqueer"),
  GENDERFLUID("Genderfluid"),
  INTERSEX("Intersex"),
  MAN("Man"),
  MASCULINE("Masculine"),
  NEUTROIS("Neutrois"),
  NONBINARY("Nonbinary"),
  PANGENDER("Pangender"),
  THIRD_GENDER("Third Gender"),
  WOMAN("Woman"),
  OTHER("Other");

  private String title;

  private Gender(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
