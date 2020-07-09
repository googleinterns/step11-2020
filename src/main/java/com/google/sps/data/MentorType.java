package com.google.sps.data;

/** Represents a mentors's style of mentorship
 */
public enum MentorType {
  TUTOR("Tutor"),
  COLLEGE("College Advisor"),
  CAREER("Career Advisor");

  private String title;

  private MentorType(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
