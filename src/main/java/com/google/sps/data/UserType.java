package com.google.sps.data;

public enum UserType {
  MENTOR("Mentor"),
  MENTEE("Mentee");

  private String title;

  private UserType(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
