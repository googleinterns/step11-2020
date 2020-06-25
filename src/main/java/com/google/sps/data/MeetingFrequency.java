package com.google.sps.data;

public enum MeetingFrequency {
  DAILY("Daily"),
  TWICE_WEEKLY("Twice a week"),
  WEEKLY("Weekly"),
  TWICE_MONTHLY("Twice a month"),
  MONTHLY("Monthly"),
  WHEN_NEEDED("When needed");

  private String title;

  private MeetingFrequency(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
