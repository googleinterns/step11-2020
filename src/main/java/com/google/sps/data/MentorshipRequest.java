package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

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

  public MentorshipRequest(Entity entity) {
    this.toUserKey = (long) entity.getProperty("toUserKey");
    this.fromUserKey = (long) entity.getProperty("fromUserKey");
  }

  public Entity convertToEntity() {
    Entity entity = new Entity(ENTITY_TYPE);
    entity.setProperty("toUserKey", toUserKey);
    entity.setProperty("fromUserKey", fromUserKey);
    return entity;
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
