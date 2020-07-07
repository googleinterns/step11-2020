package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.sps.util.ParameterConstants;

public class Connection {

  private long mentorKey;
  private long menteeKey;
  private Mentor mentor;
  private Mentee mentee;

  public Connection(long mentorKey, long menteeKey) {
    this.mentorKey = mentorKey;
    this.menteeKey = menteeKey;
  }

  public Connection(Entity entity) {
    this.mentorKey = (long) entity.getProperty(ParameterConstants.MENTOR_KEY);
    this.menteeKey = (long) entity.getProperty(ParameterConstants.MENTEE_KEY);
  }

  public Entity convertToEntity() {
    Entity entity = new Entity(ParameterConstants.ENTITY_TYPE_CONNECTION);
    entity.setProperty(ParameterConstants.MENTOR_KEY, mentorKey);
    entity.setProperty(ParameterConstants.MENTEE_KEY, menteeKey);
    return entity;
  }

  public void setMentor(Mentor mentor) {
    this.mentor = mentor;
  }

  public void setMentee(Mentee mentee) {
    this.mentee = mentee;
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
