package com.google.sps.data;

public enum Topic {
  COMPUTER_SCIENCE("Computer Science"),
  PHYSICS("Physics"),
  ART("Art"),
  MEDICINE("Medicine"),
  MUSIC("Music"),
  MATH("Math"),
  LANGUAGE("Language"),
  BIOLOGY("Biology"),
  OTHER("Other");

  private String title;

  private Topic(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
