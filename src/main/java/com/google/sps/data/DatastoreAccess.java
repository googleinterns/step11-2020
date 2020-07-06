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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
              .dislikedMentorKeys(Arrays.asList())
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

  public User getCurrentUser() {
    return userService.getCurrentUser();
  }

  public UserAccount getUser(String userID) {
    Query query =
        new Query(UserAccount.ENTITY_TYPE)
            .setFilter(
                new Query.FilterPredicate(UserAccount.USER_ID, Query.FilterOperator.EQUAL, userID));
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

  public void saveUser(UserAccount user) {
    datastoreService.put(user.convertToEntity());
  }

  public Collection<Mentor> getRelatedMentors(Mentee mentee) {
    Query query =
        new Query(UserAccount.ENTITY_TYPE)
            .setFilter(
                new Query.FilterPredicate(
                    UserAccount.USER_TYPE, Query.FilterOperator.EQUAL, UserType.MENTOR.ordinal()));
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
                    UserAccount.USER_TYPE, Query.FilterOperator.EQUAL, UserType.MENTEE.ordinal()));
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
                    MentorshipRequest.TO_USER_KEY, Query.FilterOperator.EQUAL, user.getDatastoreKey()));
    PreparedQuery results = datastoreService.prepare(query);
    Collection<MentorshipRequest> requests = StreamSupport.stream(
            results.asIterable().spliterator(), false)
        .map(MentorshipRequest::new)
        .collect(Collectors.toList());
    requests.forEach(request  -> {
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
                    MentorshipRequest.FROM_USER_KEY, Query.FilterOperator.EQUAL, user.getDatastoreKey()));
    PreparedQuery results = datastoreService.prepare(query);
    Collection<MentorshipRequest> requests = StreamSupport.stream(
            results.asIterable().spliterator(), false)
        .map(MentorshipRequest::new)
        .collect(Collectors.toList());
    requests.forEach(request  -> {
      request.setFromUser(user);
      request.setToUser(getUser(request.getFromUserKey()));
    });
    return requests;
  }

  public void dislikeMentor(Mentee mentee, Mentor mentor) {
    mentee.dislikedMentorKeys.add(mentor.getDatastoreKey());
    saveUser(mentee);
  }

  public Collection<Mentor> getDislikedMentors(Mentee mentee) {
    return datastore.get(mentee.getDislikedMentorKeys().stream().map(longKey -> KeyFactory.createKey(UserAccount.ENTITY_TYPE, longKey)).collect(Collectors.toList())).values().stream().map(Mentor::new).collect(Collectors.toList());
  }

  public void publishRequest(MentorshipRequest request) {}

  public MentorshipRequest getMentorshipRequest(long requestKey) {
    return new MentorshipRequest(requestKey + 1234, requestKey - 1234);
  }

  public void deleteRequest(MentorshipRequest request) {}

  // delete request object and create connection object
  public void approveRequest(MentorshipRequest request) {}

  // delete request object
  public void denyRequest(MentorshipRequest request) {}

  public void makeConnection(long mentorKey, long menteeKey) {}

  public Collection<Connection> getConnections(UserAccount user) {
    Collection<Connection> data = new ArrayList(5);
    for (int i = 0; i < 5; i++) {
      // data.add(new Connection());
    }
    return data;
  }
}
