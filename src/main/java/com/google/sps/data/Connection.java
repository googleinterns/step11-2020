package com.google.sps.data;

import java.util.Date;

public class Connection {
  public static final String ENTITY_TYPE = "Connection";

  private long mentorKey;
  private long menteeKey;
  private Mentor mentor;
  private Mentee mentee;


  public Connection(long mentorKey, long menteeKey) {
    this.mentorKey = mentorKey;
    this.menteeKey = menteeKey;
  }

	public long getMentorKey() {
		return mentorKey;
	}

	public long getMenteeKey() {
		return menteeKey;
	}

	public Mentor getMentor() {
		return mentor;
	}

	public Mentee getMentee() {
		return mentee;
	}
}
