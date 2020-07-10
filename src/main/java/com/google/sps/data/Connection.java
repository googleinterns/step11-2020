package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.sps.util.ParameterConstants;

public class Connection implements DatastoreEntity {
  private long datastoreKey;
  private long mentorKey;
  private long menteeKey;
  private Mentor mentor;
  private Mentee mentee;

  private Connection(long datastoreKey, long mentorKey, long menteeKey) {
    this.datastoreKey = datastoreKey;
    this.mentorKey = mentorKey;
    this.menteeKey = menteeKey;
  }

  public Connection(long mentorKey, long menteeKey) {
    this(
        DatastoreServiceFactory.getDatastoreService()
            .allocateIds(ParameterConstants.ENTITY_TYPE_CONNECTION, 1)
            .getStart()
            .getId(),
        mentorKey,
        menteeKey);
  }

  public Connection(Entity entity) {
    this(
        entity.getKey().getId(),
        (long) entity.getProperty(ParameterConstants.MENTOR_KEY),
        (long) entity.getProperty(ParameterConstants.MENTEE_KEY));
  }

  public Entity convertToEntity() {
    Key key = KeyFactory.createKey(ParameterConstants.ENTITY_TYPE_CONNECTION, this.datastoreKey);
    Entity entity = new Entity(key);
    entity.setProperty(ParameterConstants.MENTOR_KEY, mentorKey);
    entity.setProperty(ParameterConstants.MENTOR_KEY, menteeKey);
    return entity;
  }

  public void setMentor(Mentor mentor) {
    this.mentor = mentor;
  }

  public void setMentee(Mentee mentee) {
    this.mentee = mentee;
  }

  public long getDatastoreKey() {
    return datastoreKey;
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
