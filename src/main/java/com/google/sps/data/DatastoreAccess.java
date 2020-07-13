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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.util.ContextFields;
import com.google.sps.util.ParameterConstants;
import com.google.sps.util.RandomObjects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DatastoreAccess implements DataAccess {

  private static boolean seeded = false;
  private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
  private static UserService userService = UserServiceFactory.getUserService();

  public DatastoreAccess() {
    if (!DatastoreAccess.seeded) {
      seedDatabase();
      DatastoreAccess.seeded = true;
    }
  }

  private void seedDatabase() {
    Collection<Mentee> someMentees = new ArrayList<>(50);
    Collection<Mentor> someMentors = new ArrayList<>(50);
    for (int i = 0; i < 250; i++) {
      Mentee mentee = RandomObjects.randomMentee();
      saveUser(mentee);
      if (i < 50) {
        someMentees.add(mentee);
      }
    }
    for (int i = 0; i < 250; i++) {
      Mentor mentor = RandomObjects.randomMentor();
      saveUser(mentor);
      if (i < 50) {
        someMentors.add(mentor);
      }
    }
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
      isMentor = currentUser.getUserType() == UserType.MENTOR;
      isMentee = !isMentor && currentUser.getUserType() == UserType.MENTEE;
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
    return user.getUserType() == UserType.MENTOR ? null : (Mentee) user;
  }

  public Mentee getMentee(long datastoreKey) {
    UserAccount user = getUser(datastoreKey);
    return user.getUserType() == UserType.MENTOR ? null : (Mentee) user;
  }

  public Mentor getMentor(String userID) {
    UserAccount user = getUser(userID);
    return user.getUserType() == UserType.MENTEE ? null : (Mentor) user;
  }

  public Mentor getMentor(long datastoreKey) {
    UserAccount user = getUser(datastoreKey);
    return user.getUserType() == UserType.MENTEE ? null : (Mentor) user;
  }

  public boolean createUser(UserAccount user) {
    if (getUser(user.getDatastoreKey()) == null) {
      return saveUser(user);
    }
    return false;
  }

  public boolean saveUser(UserAccount user) {
    datastoreService.put(user.convertToEntity());
    return true;
  }

  public Collection<Mentor> getRelatedMentors(Mentee mentee) {
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT)
            .setFilter(
                new Query.FilterPredicate(
                    ParameterConstants.USER_TYPE,
                    Query.FilterOperator.EQUAL,
                    UserType.MENTOR.ordinal()));
    PreparedQuery results = datastoreService.prepare(query);
    return StreamSupport.stream(
            results.asIterable(FetchOptions.Builder.withLimit(10)).spliterator(), false)
        .map(Mentor::new)
        .collect(Collectors.toList());
  }

  public Collection<Mentee> getRelatedMentees(Mentor mentor) {
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_USER_ACCOUNT)
            .setFilter(
                new Query.FilterPredicate(
                    ParameterConstants.USER_TYPE,
                    Query.FilterOperator.EQUAL,
                    UserType.MENTEE.ordinal()));
    PreparedQuery results = datastoreService.prepare(query);
    return StreamSupport.stream(
            results.asIterable(FetchOptions.Builder.withLimit(10)).spliterator(), false)
        .map(Mentee::new)
        .collect(Collectors.toList());
  }

  public Collection<MentorshipRequest> getIncomingRequests(UserAccount user) {
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
        saveUser(mentee);
        return true;
      }
    }
    return false;
  }

  public Collection<Mentor> getDislikedMentors(Mentee mentee) {
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

  // delete request object and create connection object
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
      return makeConnection(mentor.getDatastoreKey(), mentee.getDatastoreKey());
    }
    return false;
  }

  // delete request object
  public boolean denyRequest(MentorshipRequest request) {
    return deleteRequest(request);
  }

  public boolean makeConnection(long mentorKey, long menteeKey) {
    if (getMentor(mentorKey) != null && getMentee(menteeKey) != null) {
      Connection connection = new Connection(mentorKey, menteeKey);
      datastoreService.put(connection.convertToEntity());
    }
    return false;
  }

  public Collection<Connection> getConnections(UserAccount user) {
    Query query =
        new Query(ParameterConstants.ENTITY_TYPE_CONNECTION)
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
    Collection<Connection> connections =
        StreamSupport.stream(results.asIterable().spliterator(), false)
            .map(Connection::new)
            .collect(Collectors.toList());
    connections.forEach(
        connection -> {
          if (user.getUserType() == UserType.MENTOR) {
            connection.setMentor((Mentor) user);
            connection.setMentee(getMentee(connection.getMenteeKey()));
          } else if (user.getUserType() == UserType.MENTEE) {
            connection.setMentee((Mentee) user);
            connection.setMentor(getMentor(connection.getMentorKey()));
          }
        });
    return connections;
  }
}
