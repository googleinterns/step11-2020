package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyRange;
import com.google.sps.util.ParameterConstants;

public class MentorshipRequest {

  private long datastoreKey;
  private long toUserKey;
  private long fromUserKey;
  private UserAccount toUser;
  private UserAccount fromUser;

  public MentorshipRequest(long toUserKey, long fromUserKey) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    KeyRange keyRange = datastore.allocateIds(ParameterConstants.ENTITY_TYPE_MENTORSHIP_REQUEST, 1);
    this.datastoreKey = keyRange.getStart().getId();
    this.toUserKey = toUserKey;
    this.fromUserKey = fromUserKey;
  }

  public MentorshipRequest(Entity entity) {
    this.datastoreKey = entity.getKey().getId();
    this.toUserKey = (long) entity.getProperty(ParameterConstants.TO_USER_KEY);
    this.fromUserKey = (long) entity.getProperty(ParameterConstants.FROM_USER_KEY);
  }

  public Entity convertToEntity() {
    Entity entity = new Entity(ParameterConstants.ENTITY_TYPE_MENTORSHIP_REQUEST);
    entity.setProperty(ParameterConstants.TO_USER_KEY, toUserKey);
    entity.setProperty(ParameterConstants.FROM_USER_KEY, fromUserKey);
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
