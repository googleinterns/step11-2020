package com.google.sps.data;

public class Connection {
  private Mentor mentor;
  private Mentee mentee;

  public Connection(Mentor mentor, Mentee mentee) {
    this.mentor = mentor;
    this.mentee = mentee;
  }

  public Mentor getMentor() {
    return mentor;
  }

  public Mentee getMentee() {
    return mentee;
  }
}
