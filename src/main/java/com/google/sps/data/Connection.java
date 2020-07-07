package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.sps.util.ParameterConstants;

public class Connection {
  public static final String ENTITY_TYPE = "Connection";

  private long datastoreKey;
  private long mentorKey;
  private long menteeKey;
  private Mentor mentor;
  private Mentee mentee;

  public Connection(long mentorKey, long menteeKey) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    KeyRange keyRange = datastore.allocateIds(ENTITY_TYPE, 1);
    this.datastoreKey = keyRange.getStart().getId();
    this.mentorKey = mentorKey;
    this.menteeKey = menteeKey;
  }

  public Connection(Entity entity) {
    this.datastoreKey = entity.getKey().getId();
    this.mentorKey = (long) entity.getProperty(ParameterConstants.MENTOR_KEY);
    this.menteeKey = (long) entity.getProperty(ParameterConstants.MENTEE_KEY);
  }

  public Entity convertToEntity() {
    Key key = KeyFactory.createKey(ENTITY_TYPE, this.datastoreKey);
    Entity entity = new Entity(key);
    entity.setProperty("mentorKey", mentorKey);
    entity.setProperty("menteeKey", menteeKey);
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
