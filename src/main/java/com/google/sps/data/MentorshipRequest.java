// Copyright 2020 Google LLC
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

package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.sps.util.ParameterConstants;

/**
 * This class represents a request for mentorship between two users. Mentees can send these to
 * mentors. Mentors can then approve/deny the request.
 *
 * @author guptamudit
 * @author tquintanilla
 * @version 1.0
 */
public class MentorshipRequest implements DatastoreEntity {

  private long datastoreKey;
  private long toUserKey;
  private long fromUserKey;
  private UserAccount toUser;
  private UserAccount fromUser;

  public MentorshipRequest(long datastoreKey, long toUserKey, long fromUserKey) {
    this.datastoreKey = datastoreKey;
    this.toUserKey = toUserKey;
    this.fromUserKey = fromUserKey;
  }

  public MentorshipRequest(long toUserKey, long fromUserKey) {
    this(
        DatastoreServiceFactory.getDatastoreService()
            .allocateIds(ParameterConstants.ENTITY_TYPE_MENTORSHIP_REQUEST, 1)
            .getStart()
            .getId(),
        toUserKey,
        fromUserKey);
  }

  public MentorshipRequest(Entity entity) {
    this(
        entity.getKey().getId(),
        (long) entity.getProperty(ParameterConstants.TO_USER_KEY),
        (long) entity.getProperty(ParameterConstants.FROM_USER_KEY));
  }

  public Entity convertToEntity() {
    Key key =
        KeyFactory.createKey(ParameterConstants.ENTITY_TYPE_MENTORSHIP_REQUEST, this.datastoreKey);
    Entity entity = new Entity(key);
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
