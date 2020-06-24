package com.google.sps.data;

public class MentorToMenteeRequest extends MentorshipRequest {
  private long toUserKey;
  private long fromUserKey;
  private Mentor toUser;
  private Mentee fromUser;

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

  public Mentor getToUser() {
    return toUser;
  }

  public Mentee getFromUser() {
    return fromUser;
  }
}
