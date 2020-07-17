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

package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.util.ContextFields;
import com.google.sps.util.ParameterConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This class provides access to data from the datastore service provided by google appengine.
 *
 * @author guptamudit
 * @version 1.0
 */
public class DatastoreAccess implements DataAccess {

  private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
  private static UserService userService = UserServiceFactory.getUserService();

  public boolean seed_db(Collection<Entity> entities) {
    datastoreService.put(entities);
    return true;
  }

  public Map<String, Object> getDefaultRenderingContext(String currentURL) {
    Map<String, Object> context = new HashMap<String, Object>();
    context.put(ContextFields.URL, currentURL);
    boolean loggedIn = userService.isUserLoggedIn();
    context.put(ContextFields.IS_LOGGED_IN, loggedIn);
    UserAccount currentUser = null;
    boolean isMentor = false, isMentee = false;
    if (loggedIn) {
      User user = getCurrentUser();
      currentUser = getUser(user.getUserId());
      isMentor = currentUser == null ? false : currentUser.getUserType() == UserType.MENTOR;
      isMentee =
          currentUser == null ? false : !isMentor && currentUser.getUserType() == UserType.MENTEE;
    }
    context.put(ContextFields.CURRENT_USER, currentUser);
    context.put(ContextFields.IS_MENTOR, isMentor);
    context.put(ContextFields.IS_MENTEE, isMentee);
    return context;
  }

  public User getCurrentUser() {
    return userService.getCurrentUser();
  }

  public UserAccount getUser(String userID) {
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT)
            .setFilter(
                new Query.FilterPredicate(
                    ParameterConstants.USER_ID, Query.FilterOperator.EQUAL, userID));
    PreparedQuery result = datastoreService.prepare(query);
    Entity userEntity = result.asSingleEntity();
    return UserAccount.fromEntity(userEntity);
  }

  public UserAccount getUser(long datastoreKey) {
    try {
      return UserAccount.fromEntity(
          datastoreService.get(
              KeyFactory.createKey(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT, datastoreKey)));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  public Mentee getMentee(String userID) {
    UserAccount user = getUser(userID);
    return user == null || user.getUserType() == UserType.MENTOR ? null : (Mentee) user;
  }

  public Mentee getMentee(long datastoreKey) {
    UserAccount user = getUser(datastoreKey);
    return user == null || user.getUserType() == UserType.MENTOR ? null : (Mentee) user;
  }

  public Mentor getMentor(String userID) {
    UserAccount user = getUser(userID);
    return user == null || user.getUserType() == UserType.MENTEE ? null : (Mentor) user;
  }

  public Mentor getMentor(long datastoreKey) {
    UserAccount user = getUser(datastoreKey);
    return user == null || user.getUserType() == UserType.MENTEE ? null : (Mentor) user;
  }

  public boolean createUser(UserAccount user) {
    if (getUser(user.getDatastoreKey()) == null) {
      datastoreService.put(user.convertToEntity());
      return true;
    }
    return false;
  }

  public boolean updateUser(UserAccount user) {
    UserAccount oldUser =
        user.isKeyInitialized() ? getUser(user.getDatastoreKey()) : getUser(user.getUserID());
    if (oldUser != null) {
      if (user.isKeyInitialized()) {
        datastoreService.put(user.convertToEntity());
      } else {
        Entity newUserEntity =
            new Entity(
                KeyFactory.createKey(
                    ParameterConstants.ENTITY_TYPE_USER_ACCOUNT, oldUser.getDatastoreKey()));
        newUserEntity.setPropertiesFrom(user.convertToEntity());
        datastoreService.put(newUserEntity);
      }
      return true;
    }
    return false;
  }

  public Collection<Mentor> getRelatedMentors(Mentee mentee) {
    if (getMentee(mentee.getDatastoreKey()) == null && getMentee(mentee.getUserID()) == null) {
      return new ArrayList<Mentor>();
    }
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT)
            .setFilter(
                new Query.FilterPredicate(
                    ParameterConstants.USER_TYPE,
                    Query.FilterOperator.EQUAL,
                    UserType.MENTOR.ordinal()));
    PreparedQuery results = datastoreService.prepare(query);
    return StreamSupport.stream(results.asIterable().spliterator(), false)
        .map(Mentor::new)
        .collect(Collectors.toList());
  }

  public Collection<MentorshipRequest> getIncomingRequests(UserAccount user) {
    if (getUser(user.getDatastoreKey()) == null && getUser(user.getUserID()) == null) {
      return new ArrayList<MentorshipRequest>();
    }
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_MENTORSHIP_REQUEST)
            .setFilter(
                new Query.FilterPredicate(
                    ParameterConstants.TO_USER_KEY,
                    Query.FilterOperator.EQUAL,
                    user.getDatastoreKey()));
    PreparedQuery results = datastoreService.prepare(query);
    Collection<MentorshipRequest> requests =
        StreamSupport.stream(results.asIterable().spliterator(), false)
            .map(MentorshipRequest::new)
            .collect(Collectors.toList());
    requests.forEach(
        request -> {
          request.setToUser(user);
          request.setFromUser(getUser(request.getFromUserKey()));
        });
    return requests;
  }

  public Collection<MentorshipRequest> getOutgoingRequests(UserAccount user) {
    if (getUser(user.getDatastoreKey()) == null && getUser(user.getUserID()) == null) {
      return new ArrayList<MentorshipRequest>();
    }
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_MENTORSHIP_REQUEST)
            .setFilter(
                new Query.FilterPredicate(
                    ParameterConstants.FROM_USER_KEY,
                    Query.FilterOperator.EQUAL,
                    user.getDatastoreKey()));
    PreparedQuery results = datastoreService.prepare(query);
    Collection<MentorshipRequest> requests =
        StreamSupport.stream(results.asIterable().spliterator(), false)
            .map(MentorshipRequest::new)
            .collect(Collectors.toList());
    requests.forEach(
        request -> {
          request.setFromUser(user);
          request.setToUser(getUser(request.getFromUserKey()));
        });
    return requests;
  }

  public boolean dislikeMentor(Mentee mentee, Mentor mentor) {
    if (getMentee(mentee.getDatastoreKey()) != null
        && getMentor(mentor.getDatastoreKey()) != null) {
      if (mentee.dislikeMentor(mentor)) {
        updateUser(mentee);
        return true;
      }
    }
    return false;
  }

  public Collection<Mentor> getDislikedMentors(Mentee mentee) {
    if (getMentee(mentee.getDatastoreKey()) == null && getMentee(mentee.getUserID()) == null) {
      return new ArrayList<Mentor>();
    }
    return datastoreService
        .get(
            mentee.getDislikedMentorKeys().stream()
                .map(
                    longKey ->
                        KeyFactory.createKey(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT, longKey))
                .collect(Collectors.toList()))
        .values()
        .stream()
        .map(Mentor::new)
        .collect(Collectors.toList());
  }

  public boolean publishRequest(MentorshipRequest request) {
    if (getMentorshipRequest(request.getDatastoreKey()) == null) {
      UserAccount toUser = getUser(request.getToUserKey());
      UserAccount fromUser = getUser(request.getFromUserKey());
      if (toUser != null && fromUser != null && toUser.getUserType() != fromUser.getUserType()) {
        datastoreService.put(request.convertToEntity());
        return true;
      }
    }
    return false;
  }

  public MentorshipRequest getMentorshipRequest(long requestKey) {
    try {
      return new MentorshipRequest(
          datastoreService.get(
              KeyFactory.createKey(ParameterConstants.ENTITY_TYPE_MENTORSHIP_REQUEST, requestKey)));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  public boolean deleteRequest(MentorshipRequest request) {
    if (getMentorshipRequest(request.getDatastoreKey()) != null) {
      datastoreService.delete(
          KeyFactory.createKey(
              ParameterConstants.ENTITY_TYPE_MENTORSHIP_REQUEST, request.getDatastoreKey()));
      return true;
    }
    return false;
  }

  // delete request object and create mentorMenteeRelation object
  public boolean approveRequest(MentorshipRequest request) {
    if (deleteRequest(request)) {
      UserAccount toUser = request.getToUser();
      if (toUser == null) {
        toUser = getUser(request.getToUserKey());
      }
      UserAccount fromUser = request.getFromUser();
      if (fromUser == null) {
        fromUser = getUser(request.getFromUserKey());
      }
      UserAccount mentor = toUser.getUserType() == UserType.MENTOR ? toUser : fromUser;
      UserAccount mentee = toUser.getUserType() == UserType.MENTEE ? toUser : fromUser;
      return makeMentorMenteeRelation(mentor.getDatastoreKey(), mentee.getDatastoreKey());
    }
    return false;
  }

  // delete request object
  public boolean denyRequest(MentorshipRequest request) {
    return deleteRequest(request);
  }

  public boolean makeMentorMenteeRelation(long mentorKey, long menteeKey) {
    if (getMentor(mentorKey) != null && getMentee(menteeKey) != null) {
      MentorMenteeRelation mentorMenteeRelation = new MentorMenteeRelation(mentorKey, menteeKey);
      datastoreService.put(mentorMenteeRelation.convertToEntity());
    }
    return false;
  }

  public Collection<MentorMenteeRelation> getMentorMenteeRelations(UserAccount user) {
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_MENTOR_MENTEE_RELATION)
            .setFilter(
                Query.CompositeFilterOperator.or(
                    new Query.FilterPredicate(
                        ParameterConstants.MENTOR_KEY,
                        Query.FilterOperator.EQUAL,
                        user.getDatastoreKey()),
                    new Query.FilterPredicate(
                        ParameterConstants.MENTEE_KEY,
                        Query.FilterOperator.EQUAL,
                        user.getDatastoreKey())));
    PreparedQuery results = datastoreService.prepare(query);
    Collection<MentorMenteeRelation> mentorMenteeRelations =
        StreamSupport.stream(results.asIterable().spliterator(), false)
            .map(MentorMenteeRelation::new)
            .collect(Collectors.toList());
    mentorMenteeRelations.forEach(
        mentorMenteeRelation -> {
          if (user.getUserType() == UserType.MENTOR) {
            mentorMenteeRelation.setMentor((Mentor) user);
            mentorMenteeRelation.setMentee(getMentee(mentorMenteeRelation.getMenteeKey()));
          } else if (user.getUserType() == UserType.MENTEE) {
            mentorMenteeRelation.setMentee((Mentee) user);
            mentorMenteeRelation.setMentor(getMentor(mentorMenteeRelation.getMentorKey()));
          }
        });
    return mentorMenteeRelations;
  }
}
