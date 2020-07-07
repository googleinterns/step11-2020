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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DatastoreAccess implements DataAccess {

  private static boolean seeded = false;
  private static Random rnd = new Random();
  private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
  private static UserService userService = UserServiceFactory.getUserService();

  public DatastoreAccess() {
    if (!DatastoreAccess.seeded) {
      seed_db();
      DatastoreAccess.seeded = true;
    }
  }

  private static String randomNumber(int digCount) {
    StringBuilder sb = new StringBuilder(digCount);
    for (int i = 0; i < digCount; i++) sb.append((char) ('0' + rnd.nextInt(10)));
    return sb.toString();
  }

  private static String randomLetters(int targetStringLength) {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'

    String generatedString =
        rnd.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

    return generatedString;
  }

  private static <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
    int x = rnd.nextInt(enumClass.getEnumConstants().length);
    return enumClass.getEnumConstants()[x];
  }

  private void seed_db() {
    Collection<Mentee> someMentees = new ArrayList<>(50);
    Collection<Mentor> someMentors = new ArrayList<>(50);
    for (int i = 0; i < 250; i++) {
      Mentee mentee =
          (new Mentee.Builder())
              .name(randomLetters(10))
              .userID(randomNumber(21))
              .email(randomLetters(7) + "@gmail.com")
              .dateOfBirth(new Date())
              .country(randomEnum(Country.class))
              .language(randomEnum(Language.class))
              .timezone(TimeZone.getTimeZone(TimeZone.getAvailableIDs()[rnd.nextInt(500)]))
              .ethnicity(randomEnum(Ethnicity.class))
              .ethnicityOther(randomLetters(10))
              .gender(randomEnum(Gender.class))
              .genderOther(randomLetters(10))
              .firstGen(rnd.nextBoolean())
              .lowIncome(rnd.nextBoolean())
              .educationLevel(randomEnum(EducationLevel.class))
              .educationLevelOther(randomLetters(10))
              .description(randomLetters(30))
              .userType(UserType.MENTEE)
              .goal(randomEnum(Topic.class))
              .desiredMeetingFrequency(randomEnum(MeetingFrequency.class))
              .dislikedMentorKeys(Collections.emptySet())
              .build();
      saveUser(mentee);
      if (i < 50) {
        someMentees.add(mentee);
      }
    }
    for (int i = 0; i < 250; i++) {
      Mentor mentor =
          (new Mentor.Builder())
              .name(randomLetters(10))
              .userID(randomNumber(21))
              .email(randomLetters(7) + "@gmail.com")
              .dateOfBirth(new Date())
              .country(randomEnum(Country.class))
              .language(randomEnum(Language.class))
              .timezone(TimeZone.getTimeZone(TimeZone.getAvailableIDs()[rnd.nextInt(500)]))
              .ethnicity(randomEnum(Ethnicity.class))
              .ethnicityOther(randomLetters(10))
              .gender(randomEnum(Gender.class))
              .genderOther(randomLetters(10))
              .firstGen(rnd.nextBoolean())
              .lowIncome(rnd.nextBoolean())
              .educationLevel(randomEnum(EducationLevel.class))
              .educationLevelOther(randomLetters(10))
              .description(randomLetters(30))
              .userType(UserType.MENTOR)
              .visibility(rnd.nextBoolean())
              .focusList(Arrays.asList())
              .mentorType(randomEnum(MentorType.class))
              .build();
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
        new Query(UserAccount.ENTITY_TYPE)
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
          datastoreService.get(KeyFactory.createKey(UserAccount.ENTITY_TYPE, datastoreKey)));
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
        new Query(UserAccount.ENTITY_TYPE)
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
        new Query(UserAccount.ENTITY_TYPE)
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
        new Query(MentorshipRequest.ENTITY_TYPE)
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
        new Query(MentorshipRequest.ENTITY_TYPE)
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
                .map(longKey -> KeyFactory.createKey(UserAccount.ENTITY_TYPE, longKey))
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
          datastoreService.get(KeyFactory.createKey(MentorshipRequest.ENTITY_TYPE, requestKey)));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  public boolean deleteRequest(MentorshipRequest request) {
    if (getMentorshipRequest(request.getDatastoreKey()) != null) {
      datastoreService.delete(
          KeyFactory.createKey(MentorshipRequest.ENTITY_TYPE, request.getDatastoreKey()));
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
        new Query(Connection.ENTITY_TYPE)
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
