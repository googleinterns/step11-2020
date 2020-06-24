package com.google.sps.data;

public class MentorshipRequest {
  public static final String ENTITY_TYPE = "MentorshipRequest";

  private long toUserKey;
  private long fromUserKey;
  private UserAccount toUser;
  private UserAccount fromUser;

  public MentorshipRequest(long toUserKey, long fromUserKey) {
    this.toUserKey = toUserKey;
    this.fromUserKey = fromUserKey;
  }

	public long getToUserKey() {
		return toUserKey;
	}

	public long getFromUserKey() {
		return fromUserKey;
	}

	public UserAccount getToUser() {
		return toUser;
	}

	public UserAccount getFromUser() {
		return fromUser;
	}
}
