package com.google.sps.data;

public enum Topic {
  COMPUTER_SCIENCE("Computer Science"),
  OTHER("Other");

  private String title;

  private Topic(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
