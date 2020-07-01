package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyRange;

public class MentorshipRequest {
  public static final String ENTITY_TYPE = "MentorshipRequest";

  private static final String TO_USER_KEY = "toUserKey";
  private static final String FROM_USER_KEY = "fromUserKey";

  private long datastoreKey;
  private long toUserKey;
  private long fromUserKey;
  private UserAccount toUser;
  private UserAccount fromUser;

  public MentorshipRequest(long toUserKey, long fromUserKey) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    KeyRange keyRange = datastore.allocateIds(ENTITY_TYPE, 1);
    this.datastoreKey = keyRange.getStart().getId();
    this.toUserKey = toUserKey;
    this.fromUserKey = fromUserKey;
  }

  public MentorshipRequest(Entity entity) {
    this.datastoreKey = entity.getKey().getId();
    this.toUserKey = (long) entity.getProperty(TO_USER_KEY);
    this.fromUserKey = (long) entity.getProperty(FROM_USER_KEY);
  }

  public Entity convertToEntity() {
    Entity entity = new Entity(ENTITY_TYPE);
    entity.setProperty(TO_USER_KEY, toUserKey);
    entity.setProperty(FROM_USER_KEY, fromUserKey);
    return entity;
  }

  public void setToUser(UserAccount toUser) {
    this.toUser = toUser;
  }

  public void setFromUser(UserAccount fromUser) {
    this.fromUser = fromUser;
  }

  public long getDatastoreKey() {
    return this.datastoreKey;
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
