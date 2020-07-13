// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.step.data;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.step.util.ParameterConstants;

public class MentorMenteeRelation implements DatastoreEntity {
  private long datastoreKey;
  private long mentorKey;
  private long menteeKey;
  private Mentor mentor;
  private Mentee mentee;

  private MentorMenteeRelation(long datastoreKey, long mentorKey, long menteeKey) {
    this.datastoreKey = datastoreKey;
    this.mentorKey = mentorKey;
    this.menteeKey = menteeKey;
  }

  public MentorMenteeRelation(long mentorKey, long menteeKey) {
    this(
        DatastoreServiceFactory.getDatastoreService()
            .allocateIds(ParameterConstants.ENTITY_TYPE_MENTOR_MENTEE_RELATION, 1)
            .getStart()
            .getId(),
        mentorKey,
        menteeKey);
  }

  public MentorMenteeRelation(Entity entity) {
    this(
        entity.getKey().getId(),
        (long) entity.getProperty(ParameterConstants.MENTOR_KEY),
        (long) entity.getProperty(ParameterConstants.MENTEE_KEY));
  }

  public Entity convertToEntity() {
    Key key =
        KeyFactory.createKey(
            ParameterConstants.ENTITY_TYPE_MENTOR_MENTEE_RELATION, this.datastoreKey);
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
