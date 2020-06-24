package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

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

  public Connection(Entity entity) {
    this.mentorKey = (long) entity.getProperty("mentorKey");
    this.menteeKey = (long) entity.getProperty("menteeKey");
  }

  public Entity convertToEntity() {
    Entity entity = new Entity(ENTITY_TYPE);
    entity.setProperty("mentorKey", mentorKey);
    entity.setProperty("menteeKey", menteeKey);
    return entity;
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
