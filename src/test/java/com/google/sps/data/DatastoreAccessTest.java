import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.Country;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.EducationLevel;
import com.google.sps.data.Ethnicity;
import com.google.sps.data.Gender;
import com.google.sps.data.Language;
import com.google.sps.data.MeetingFrequency;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MentorMenteeRelation;
import com.google.sps.data.MentorType;
import com.google.sps.data.MentorshipRequest;
import com.google.sps.data.TimeZone;
import com.google.sps.data.Topic;
import com.google.sps.data.UserAccount;
import com.google.sps.data.UserType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This test class uses JUnit to test all the DataStore queries in the DatastoreAccess class. To run
 * the project without tests, use: mvn package appengine:run -DskipTests. The LocalServiceTestHelper
 * is used to simulate a testing environment for the AppEngien APIs.
 *
 * @param JUnit4.class makes the tests run under the JUnit 4 framework
 * @author guptamudit
 * @version 1.0
 */
@RunWith(JUnit4.class)
public final class DatastoreAccessTest {
  private DatastoreAccess dataAccess;
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
              new LocalUserServiceTestConfig(), new LocalDatastoreServiceTestConfig())
          .setEnvAttributes(Map.of("com.google.appengine.api.users.UserService.user_id_key", "101"))
          .setEnvEmail("mudito@example.com")
          .setEnvAuthDomain("gmail.com")
          .setEnvIsLoggedIn(true);
  private Mentee defaultMentee;
  private Mentor defaultMentor;
  private Entity defaultMenteeEntity;
  private Entity defaultMentorEntity;

  private void setUpDefaultEntities() {
    Entity defaultUserAccountEntity = new Entity("UserAccount");
    defaultUserAccountEntity.setProperty("dateOfBirth", new Date(984787200000L));
    defaultUserAccountEntity.setProperty("country", (long) Country.US.ordinal());
    defaultUserAccountEntity.setProperty("language", (long) Language.EN.ordinal());
    defaultUserAccountEntity.setProperty("timezone", (long) TimeZone.GMT.ordinal());
    defaultUserAccountEntity.setProperty(
        "ethnicity", Arrays.asList((long) Ethnicity.INDIAN.ordinal()));
    defaultUserAccountEntity.setProperty("ethnicityOther", "");
    defaultUserAccountEntity.setProperty("gender", (long) Gender.MAN.ordinal());
    defaultUserAccountEntity.setProperty("genderOther", "");
    defaultUserAccountEntity.setProperty("firstGen", false);
    defaultUserAccountEntity.setProperty("lowIncome", false);
    defaultUserAccountEntity.setProperty(
        "educationLevel", (long) EducationLevel.HIGHSCHOOL.ordinal());
    defaultUserAccountEntity.setProperty("educationLevelOther", "");
    defaultUserAccountEntity.setProperty("description", "I am very cool.");

    defaultMenteeEntity = new Entity("UserAccount");
    defaultMenteeEntity.setPropertiesFrom(defaultUserAccountEntity);
    defaultMenteeEntity.setProperty("userID", "101");
    defaultMenteeEntity.setProperty("email", "mudito.mentee@example.com");
    defaultMenteeEntity.setProperty("name", "Mudito Mentee");
    defaultMenteeEntity.setProperty("userType", (long) UserType.MENTEE.ordinal());
    defaultMenteeEntity.setProperty("goal", (long) Topic.COMPUTER_SCIENCE.ordinal());
    defaultMenteeEntity.setProperty(
        "desiredMeetingFrequency", (long) MeetingFrequency.WEEKLY.ordinal());
    defaultMenteeEntity.setProperty("dislikedMentorKeys", Arrays.asList());
    defaultMenteeEntity.setProperty("desiredMentorType", (long) MentorType.CAREER.ordinal());

    defaultMentorEntity = new Entity("UserAccount");
    defaultMentorEntity.setPropertiesFrom(defaultUserAccountEntity);
    defaultMentorEntity.setProperty("userID", "102");
    defaultMentorEntity.setProperty("email", "mudito.mentor@example.com");
    defaultMentorEntity.setProperty("name", "Mudito Mentor");
    defaultMentorEntity.setProperty("userType", (long) UserType.MENTOR.ordinal());
    defaultMentorEntity.setProperty("visibility", true);
    defaultMentorEntity.setProperty(
        "focusList", Arrays.asList((long) Topic.COMPUTER_SCIENCE.ordinal()));
    defaultMentorEntity.setProperty("mentorType", (long) MentorType.CAREER.ordinal());
  }

  private void setUpDefaultObjects() {
    defaultMentee =
        (Mentee.Builder.newBuilder())
            .name("Mudito Mentee")
            .userID("101")
            .email("mudito.mentee@example.com")
            .dateOfBirth(new Date(984787200000L))
            .country(Country.US)
            .language(Language.EN)
            .timezone(TimeZone.GMT)
            .ethnicityList((Arrays.asList(Ethnicity.INDIAN)))
            .ethnicityOther("")
            .gender(Gender.MAN)
            .genderOther("")
            .firstGen(false)
            .lowIncome(false)
            .educationLevel(EducationLevel.HIGHSCHOOL)
            .educationLevelOther("")
            .description("I am very cool.")
            .userType(UserType.MENTEE)
            .goal(Topic.COMPUTER_SCIENCE)
            .desiredMeetingFrequency(MeetingFrequency.WEEKLY)
            .dislikedMentorKeys(Collections.emptySet())
            .desiredMentorType(MentorType.CAREER)
            .build();

    defaultMentor =
        (Mentor.Builder.newBuilder())
            .name("Mudito Mentor")
            .userID("102")
            .email("mudito.mentor@example.com")
            .dateOfBirth(new Date(984787200000L))
            .country(Country.US)
            .language(Language.EN)
            .timezone(TimeZone.GMT)
            .ethnicityList((Arrays.asList(Ethnicity.INDIAN)))
            .ethnicityOther("")
            .gender(Gender.MAN)
            .genderOther("")
            .firstGen(false)
            .lowIncome(false)
            .educationLevel(EducationLevel.HIGHSCHOOL)
            .educationLevelOther("")
            .description("I am very cool.")
            .mentorType(MentorType.TUTOR)
            .visibility(true)
            .userType(UserType.MENTOR)
            .focusList(new ArrayList<Topic>(Arrays.asList(Topic.COMPUTER_SCIENCE)))
            .build();
  }

  @Before
  public void setUp() {
    helper.setUp();
    dataAccess = new DatastoreAccess();
    setUpDefaultEntities();
    setUpDefaultObjects();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  private void assertUserEqualsEntity(UserAccount user, Entity entity) {
    assertEquals(user.getDatastoreKey(), entity.getKey().getId());
    assertEquals(user.getUserID(), entity.getProperty("userID"));
    assertEquals(user.getEmail(), entity.getProperty("email"));
    assertEquals(user.getName(), entity.getProperty("name"));
    assertEquals(user.getDateOfBirth(), entity.getProperty("dateOfBirth"));
    assertEquals((long) user.getCountry().ordinal(), entity.getProperty("country"));
    assertEquals((long) user.getLanguage().ordinal(), entity.getProperty("language"));
    assertEquals((long) user.getTimezone().ordinal(), entity.getProperty("timezone"));
    assertEquals(
        user.getEthnicityList().stream()
            .map(ethnicity -> ethnicity.ordinal())
            .collect(Collectors.toList())
            .toString(),
        (entity.getProperty("ethnicity") == null
                ? new ArrayList()
                : entity.getProperty("ethnicity"))
            .toString());
    assertEquals(user.getEthnicityOther(), entity.getProperty("ethnicityOther"));
    assertEquals((long) user.getGender().ordinal(), entity.getProperty("gender"));
    assertEquals(user.getGenderOther(), entity.getProperty("genderOther"));
    assertEquals(user.isFirstGen(), entity.getProperty("firstGen"));
    assertEquals(user.isLowIncome(), entity.getProperty("lowIncome"));
    assertEquals((long) user.getEducationLevel().ordinal(), entity.getProperty("educationLevel"));
    assertEquals(user.getEducationLevelOther(), entity.getProperty("educationLevelOther"));
    assertEquals(user.getDescription(), entity.getProperty("description"));
  }

  private void assertMenteeEqualsEntity(Mentee mentee, Entity entity) {
    assertUserEqualsEntity(mentee, entity);
    assertEquals((long) mentee.getUserType().ordinal(), entity.getProperty("userType"));
    assertEquals((long) mentee.getGoal().ordinal(), entity.getProperty("goal"));
    assertEquals(
        (long) mentee.getDesiredMeetingFrequency().ordinal(),
        entity.getProperty("desiredMeetingFrequency"));
    assertEquals(
        mentee.getDislikedMentorKeys(),
        entity.getProperty("dislikedMentorKeys") == null
            ? new HashSet()
            : new HashSet((Collection) entity.getProperty("dislikedMentorKeys")));
    assertEquals(
        (long) mentee.getDesiredMentorType().ordinal(), entity.getProperty("desiredMentorType"));
  }

  private void assertMentorEqualsEntity(Mentor mentor, Entity entity) {
    assertUserEqualsEntity(mentor, entity);
    assertEquals((long) mentor.getUserType().ordinal(), entity.getProperty("userType"));
    assertEquals(mentor.getVisibility(), entity.getProperty("visibility"));
    assertEquals(
        mentor.getFocusList().stream()
            .map(topic -> topic.ordinal())
            .collect(Collectors.toList())
            .toString(),
        (entity.getProperty("focusList") == null
                ? new ArrayList()
                : entity.getProperty("focusList"))
            .toString());
    assertEquals((long) mentor.getMentorType().ordinal(), entity.getProperty("mentorType"));
  }

  @Test
  public void singleSeedingTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    Collection<Entity> entities = Arrays.asList(defaultMenteeEntity, defaultMentorEntity);
    assertTrue(dataAccess.seed_db(entities));
    assertEquals(
        entities.size(), ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
  }

  @Test
  public void getCurrentUserTest() {
    User user = dataAccess.getCurrentUser();
    assertNotNull(user);
    assertEquals(user.getEmail(), "mudito@example.com");
    assertEquals(user.getUserId(), "101");
  }

  @Test
  public void getCurrentUserLoggedOutTest() {
    helper.setEnvIsLoggedIn(false);
    assertNull(dataAccess.getCurrentUser());
  }

  @Test
  public void getUserByUserIdTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMenteeEntity);
    entity.setProperty("userID", "1234");
    DatastoreServiceFactory.getDatastoreService().put(entity);
    UserAccount user = dataAccess.getUser("1234");
    assertNotNull(user);
    assertEquals(user.getUserID(), "1234");
  }

  @Test
  public void getUserByUserIdNonexistentTest() {
    assertNull(dataAccess.getUser("1234"));
  }

  @Test
  public void getUserByDatastoreKeyTest() {
    Key key = KeyFactory.createKey("UserAccount", 1234);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMenteeEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    UserAccount user = dataAccess.getUser(1234);
    assertNotNull(user);
    assertEquals(user.getDatastoreKey(), 1234);
  }

  @Test
  public void getUserByDatastoreKeyNonexistentTest() {
    assertNull(dataAccess.getUser(1234));
  }

  @Test
  public void getMenteeByUserIdTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMenteeEntity);
    entity.setProperty("userID", "1234");
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentee mentee = dataAccess.getMentee("1234");
    assertNotNull(mentee);
    assertEquals(mentee.getUserID(), "1234");
  }

  @Test
  public void getMenteeByUserIdNonexistentTest() {
    assertNull(dataAccess.getMentee("1234"));
  }

  @Test
  public void getMenteeByUserIdButItsAMentorTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMentorEntity);
    entity.setProperty("userID", "1234");
    DatastoreServiceFactory.getDatastoreService().put(entity);
    assertNull(dataAccess.getMentee("1234"));
  }

  @Test
  public void getMenteeByDatastoreKeyTest() {
    Key key = KeyFactory.createKey("UserAccount", 1234);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMenteeEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentee mentee = dataAccess.getMentee(1234);
    assertNotNull(mentee);
    assertEquals(mentee.getDatastoreKey(), 1234);
  }

  @Test
  public void getMenteeByDatastoreKeyNonexistentTest() {
    assertNull(dataAccess.getMentee(1234));
  }

  @Test
  public void getMenteeByDatastoreKeyButItsAMentorTest() {
    Key key = KeyFactory.createKey("UserAccount", 1234);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMentorEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    assertNull(dataAccess.getMentee(1234));
  }

  @Test
  public void getMentorByUserIdTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMentorEntity);
    entity.setProperty("userID", "1234");
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentor mentor = dataAccess.getMentor("1234");
    assertNotNull(mentor);
    assertEquals(mentor.getUserID(), "1234");
  }

  @Test
  public void getMentorByUserIdNonexistentTest() {
    assertNull(dataAccess.getMentor("1234"));
  }

  @Test
  public void getMentorByUserIdButItsAMenteeTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMenteeEntity);
    entity.setProperty("userID", "1234");
    DatastoreServiceFactory.getDatastoreService().put(entity);
    assertNull(dataAccess.getMentor("1234"));
  }

  @Test
  public void getMentorByDatastoreKeyTest() {
    Key key = KeyFactory.createKey("UserAccount", 1234);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMentorEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentor mentor = dataAccess.getMentor(1234);
    assertNotNull(mentor);
    assertEquals(mentor.getDatastoreKey(), 1234);
  }

  @Test
  public void getMentorByDatastoreKeyNonexistentTest() {
    assertNull(dataAccess.getMentor(1234));
  }

  @Test
  public void getMentorByDatastoreKeyButItsAMenteeTest() {
    Key key = KeyFactory.createKey("UserAccount", 1234);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMenteeEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    assertNull(dataAccess.getMentor(1234));
  }

  @Test
  public void getDefaultRenderingContextLoggedInNoAccountTest() {
    Map<String, Object> context = dataAccess.getDefaultRenderingContext("ilerjbfleq");
    assertEquals("ilerjbfleq", context.get("url"));
    assertTrue((boolean) context.get("isLoggedIn"));
    UserAccount user = (UserAccount) context.get("currentUser");
    assertNull(user);
    assertFalse((boolean) context.get("isMentee"));
    assertFalse((boolean) context.get("isMentor"));
  }

  @Test
  public void getDefaultRenderingContextLoggedOutTest() {
    helper.setEnvIsLoggedIn(false);
    Map<String, Object> context = dataAccess.getDefaultRenderingContext("ilerjbfleq");
    assertEquals("ilerjbfleq", context.get("url"));
    assertFalse((boolean) context.get("isLoggedIn"));
    assertNull(context.get("currentUser"));
  }

  @Test
  public void getDefaultRenderingContextLoggedInIsMenteeTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    ds.put(defaultMenteeEntity);
    Map<String, Object> context = dataAccess.getDefaultRenderingContext("");
    assertTrue((boolean) context.get("isLoggedIn"));
    UserAccount user = (UserAccount) context.get("currentUser");
    assertNotNull(user);
    assertTrue(defaultMentee.looselyEquals(user));
    assertTrue((boolean) context.get("isMentee"));
    assertFalse((boolean) context.get("isMentor"));
  }

  @Test
  public void getDefaultRenderingContextLoggedInIsMentorTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    defaultMentorEntity.setProperty("userID", "101");
    ds.put(defaultMentorEntity);
    Map<String, Object> context = dataAccess.getDefaultRenderingContext("");
    assertTrue((boolean) context.get("isLoggedIn"));
    UserAccount user = (UserAccount) context.get("currentUser");
    assertNotNull(user);
    assertTrue(new Mentor(defaultMentorEntity).looselyEquals(user));
    assertTrue((boolean) context.get("isMentor"));
    assertFalse((boolean) context.get("isMentee"));
  }

  @Test
  public void createUserEmptyDatabaseTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertTrue(dataAccess.createUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertMenteeEqualsEntity(defaultMentee, ds.prepare(new Query("UserAccount")).asSingleEntity());
  }

  @Test
  public void createUserAlreadyInDatabaseTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertTrue(dataAccess.createUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertMenteeEqualsEntity(defaultMentee, ds.prepare(new Query("UserAccount")).asSingleEntity());
    assertFalse(dataAccess.createUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertMenteeEqualsEntity(defaultMentee, ds.prepare(new Query("UserAccount")).asSingleEntity());
  }

  @Test
  public void createUserMultipleTest() throws EntityNotFoundException {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertTrue(dataAccess.createUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertMenteeEqualsEntity(defaultMentee, ds.prepare(new Query("UserAccount")).asSingleEntity());
    assertTrue(dataAccess.createUser(defaultMentor));
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertMentorEqualsEntity(
        defaultMentor,
        ds.get(KeyFactory.createKey("UserAccount", defaultMentor.getDatastoreKey())));
  }

  @Test
  public void updateUserEmptyDatabaseTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.updateUser(defaultMentee));
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
  }

  @Test
  public void updateUserInDatabaseTest() throws EntityNotFoundException {
    final long menteeKey = defaultMentee.getDatastoreKey();
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    try {
      Entity entity = ds.get(KeyFactory.createKey("UserAccount", menteeKey));
      fail(
          "Datastore should not include user with key:"
              + menteeKey
              + " but "
              + entity
              + " was found");
    } catch (EntityNotFoundException expectedException) {
    }
    assertTrue(dataAccess.createUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    defaultMentee.setName("Thomas");
    assertTrue(dataAccess.updateUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertNotNull(ds.get(KeyFactory.createKey("UserAccount", menteeKey)));
  }

  @Test
  public void updateNonexistentUserWhileOthersPresentTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertTrue(dataAccess.createUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.updateUser(defaultMentor));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
  }

  @Test
  public void deleteUserEmptyDatabaseTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.deleteUser(defaultMentee));
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
  }

  @Test
  public void deleteUserInDatabaseTest() throws EntityNotFoundException {
    final long menteeKey = defaultMentee.getDatastoreKey();
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    try {
      Entity entity = ds.get(KeyFactory.createKey("UserAccount", menteeKey));
      fail(
          "Datastore should not include user with key:"
              + menteeKey
              + " but "
              + entity
              + " was found");
    } catch (EntityNotFoundException expectedException) {
    }
    assertTrue(dataAccess.createUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertTrue(dataAccess.deleteUser(defaultMentee));
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    try {
      Entity entity = ds.get(KeyFactory.createKey("UserAccount", menteeKey));
      fail(
          "Datastore should not include user with key:"
              + menteeKey
              + " but "
              + entity
              + " was found");
    } catch (EntityNotFoundException expectedException) {
    }
  }

  @Test
  public void deleteNonexistentUserWhileOthersPresentTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertTrue(dataAccess.createUser(defaultMentee));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.deleteUser(defaultMentor));
    assertEquals(1, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
  }

  @Test
  public void getRelatedMentorsForNonexistentMenteeTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity mentorEntity2 = new Entity("UserAccount");
    mentorEntity2.setPropertiesFrom(defaultMentorEntity);
    mentorEntity2.setProperty("userID", "202");
    ds.put(Arrays.asList(mentorEntity1, mentorEntity2));
    Collection<Mentor> mentors = dataAccess.getRelatedMentors(defaultMentee);
    assertTrue(mentors.isEmpty());
  }

  @Test
  public void getRelatedMentorsForSimilarMenteeTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity mentorEntity2 = new Entity("UserAccount");
    mentorEntity2.setPropertiesFrom(defaultMentorEntity);
    mentorEntity2.setProperty("userID", "202");
    Entity mentorEntity3 = new Entity("UserAccount");
    mentorEntity3.setPropertiesFrom(defaultMentorEntity);
    mentorEntity3.setProperty("userID", "203");
    Entity mentorEntity4 = new Entity("UserAccount");
    mentorEntity4.setPropertiesFrom(defaultMentorEntity);
    mentorEntity4.setProperty("userID", "204");
    Entity mentorEntity5 = new Entity("UserAccount");
    mentorEntity5.setPropertiesFrom(defaultMentorEntity);
    mentorEntity5.setProperty("userID", "205");
    ds.put(
        Arrays.asList(mentorEntity1, mentorEntity2, mentorEntity3, mentorEntity4, mentorEntity5));
    ds.put(defaultMenteeEntity);
    Collection<Mentor> mentors = dataAccess.getRelatedMentors(defaultMentee);
    assertEquals(5, mentors.size());
    assertEquals(
        new HashSet<String>(Arrays.asList("201", "202", "203", "204", "205")),
        mentors.stream().map(mentor -> mentor.getUserID()).collect(Collectors.toSet()));
  }

  @Test
  public void getIncomingRequestsForNonexistentUserTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    Entity requestEntity = new Entity("MentorshipRequest");
    requestEntity.setProperty("toUserKey", mentorEntity1.getKey().getId());
    requestEntity.setProperty("fromUserKey", menteeEntity1.getKey().getId());
    ds.put(requestEntity);
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertEquals(0, dataAccess.getIncomingRequests(defaultMentee).size());
  }

  @Test
  public void getIncomingRequestsForUninvolvedUserTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(defaultMenteeEntity, mentorEntity1, menteeEntity1));
    Entity requestEntity = new Entity("MentorshipRequest");
    requestEntity.setProperty("toUserKey", mentorEntity1.getKey().getId());
    requestEntity.setProperty("fromUserKey", menteeEntity1.getKey().getId());
    ds.put(requestEntity);
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertEquals(0, dataAccess.getIncomingRequests(defaultMentee).size());
  }

  @Test
  public void getIncomingRequestsForInvolvedUserTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(defaultMenteeEntity, mentorEntity1, menteeEntity1));
    Entity requestEntity1 = new Entity("MentorshipRequest");
    requestEntity1.setProperty("toUserKey", mentorEntity1.getKey().getId());
    requestEntity1.setProperty("fromUserKey", menteeEntity1.getKey().getId());
    Entity requestEntity2 = new Entity("MentorshipRequest");
    requestEntity2.setProperty("toUserKey", mentorEntity1.getKey().getId());
    requestEntity2.setProperty("fromUserKey", defaultMenteeEntity.getKey().getId());
    ds.put(Arrays.asList(requestEntity1, requestEntity2));
    assertEquals(2, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    Mentor mentor = new Mentor(mentorEntity1);
    ArrayList<MentorshipRequest> requests = new ArrayList<>(dataAccess.getIncomingRequests(mentor));
    assertEquals(2, requests.size());
    assertEquals(mentorEntity1.getKey().getId(), requests.get(0).getToUserKey());
    assertEquals(menteeEntity1.getKey().getId(), requests.get(0).getFromUserKey());
    assertEquals(mentorEntity1.getKey().getId(), requests.get(1).getToUserKey());
    assertEquals(defaultMenteeEntity.getKey().getId(), requests.get(1).getFromUserKey());
  }

  @Test
  public void getOutgoingRequestsForNonexistentUserTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    Entity requestEntity = new Entity("MentorshipRequest");
    requestEntity.setProperty("toUserKey", mentorEntity1.getKey().getId());
    requestEntity.setProperty("fromUserKey", menteeEntity1.getKey().getId());
    ds.put(requestEntity);
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    Collection<MentorshipRequest> requests = dataAccess.getOutgoingRequests(defaultMentee);
    assertTrue(requests.isEmpty());
  }

  @Test
  public void getOutgoingRequestsForUninvolvedUserTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(defaultMenteeEntity, mentorEntity1, menteeEntity1));
    Entity requestEntity = new Entity("MentorshipRequest");
    requestEntity.setProperty("toUserKey", mentorEntity1.getKey().getId());
    requestEntity.setProperty("fromUserKey", menteeEntity1.getKey().getId());
    ds.put(requestEntity);
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    Collection<MentorshipRequest> requests = dataAccess.getOutgoingRequests(defaultMentee);
    assertTrue(requests.isEmpty());
  }

  @Test
  public void getOutgoingRequestsForInvolvedUserTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(defaultMentorEntity, mentorEntity1, menteeEntity1));
    Entity requestEntity1 = new Entity("MentorshipRequest");
    requestEntity1.setProperty("toUserKey", mentorEntity1.getKey().getId());
    requestEntity1.setProperty("fromUserKey", menteeEntity1.getKey().getId());
    Entity requestEntity2 = new Entity("MentorshipRequest");
    requestEntity2.setProperty("toUserKey", defaultMentorEntity.getKey().getId());
    requestEntity2.setProperty("fromUserKey", menteeEntity1.getKey().getId());
    ds.put(Arrays.asList(requestEntity1, requestEntity2));
    assertEquals(2, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    Mentee mentee = new Mentee(menteeEntity1);
    ArrayList<MentorshipRequest> requests = new ArrayList<>(dataAccess.getOutgoingRequests(mentee));
    assertEquals(2, requests.size());
    assertEquals(mentorEntity1.getKey().getId(), requests.get(0).getToUserKey());
    assertEquals(menteeEntity1.getKey().getId(), requests.get(0).getFromUserKey());
    assertEquals(defaultMentorEntity.getKey().getId(), requests.get(1).getToUserKey());
    assertEquals(menteeEntity1.getKey().getId(), requests.get(1).getFromUserKey());
  }

  @Test
  public void dislikeMentorNonexistentMenteeTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    Mentor mentor = new Mentor(mentorEntity1);
    assertFalse(dataAccess.dislikeMentor(defaultMentee, mentor));
  }

  @Test
  public void dislikeMentorNonexistentMentorTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    Mentee mentee = new Mentee(menteeEntity1);
    assertFalse(dataAccess.dislikeMentor(mentee, defaultMentor));
  }

  @Test
  public void dislikeMentorSameMentorTwiceTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    Mentee mentee = new Mentee(menteeEntity1);
    Mentor mentor = new Mentor(mentorEntity1);
    assertTrue(dataAccess.dislikeMentor(mentee, mentor));
    assertFalse(dataAccess.dislikeMentor(mentee, mentor));
    assertEquals(1, dataAccess.getMentee(mentee.getDatastoreKey()).getDislikedMentorKeys().size());
    assertEquals(
        mentor.getDatastoreKey(),
        new ArrayList(dataAccess.getMentee(mentee.getDatastoreKey()).getDislikedMentorKeys())
            .get(0));
  }

  @Test
  public void getDislikedMentorsShouldBeEmptyTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    ds.put(mentorEntity1);
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(menteeEntity1);
    Mentee mentee = new Mentee(menteeEntity1);
    Mentor mentor = new Mentor(mentorEntity1);
    ArrayList<Mentor> dislikedMentors = new ArrayList<>(dataAccess.getDislikedMentors(mentee));
    assertEquals(0, dislikedMentors.size());
  }

  @Test
  public void getDislikedMentorsValidMenteeTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    ds.put(mentorEntity1);
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    menteeEntity1.setProperty("dislikedMentorKeys", Arrays.asList(mentorEntity1.getKey().getId()));
    ds.put(menteeEntity1);
    Mentee mentee = new Mentee(menteeEntity1);
    Mentor mentor = new Mentor(mentorEntity1);
    ArrayList<Mentor> dislikedMentors = new ArrayList<>(dataAccess.getDislikedMentors(mentee));
    assertEquals(1, dislikedMentors.size());
    assertTrue(mentor.looselyEquals(dislikedMentors.get(0)));
  }

  @Test
  public void publishRequestNonexistentUsersTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    ds.delete(Arrays.asList(mentorEntity1.getKey(), menteeEntity1.getKey()));
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.publishRequest(request));
  }

  @Test
  public void publishRequestValidUsersTest() throws EntityNotFoundException {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertTrue(dataAccess.publishRequest(request));
    Entity createdRequestEntity =
        ds.get(KeyFactory.createKey("MentorshipRequest", request.getDatastoreKey()));
    assertTrue(
        new Mentee(menteeEntity1)
            .looselyEquals(
                new Mentee(
                    ds.get(
                        KeyFactory.createKey(
                            "UserAccount",
                            (long) createdRequestEntity.getProperty("fromUserKey"))))));
    assertTrue(
        new Mentor(mentorEntity1)
            .looselyEquals(
                new Mentor(
                    ds.get(
                        KeyFactory.createKey(
                            "UserAccount",
                            (long) createdRequestEntity.getProperty("toUserKey"))))));
  }

  @Test
  public void publishRequestPreexistingTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertTrue(dataAccess.publishRequest(request));
    assertFalse(dataAccess.publishRequest(request));
  }

  @Test
  public void getMentorshipRequestID0Test() {
    assertNull(dataAccess.getMentorshipRequest(0));
  }

  @Test
  public void getMentorshipRequestNonexistentRequestTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    MentorshipRequest createdRequest = dataAccess.getMentorshipRequest(request.getDatastoreKey());
    assertNull(createdRequest);
  }

  @Test
  public void getMentorshipRequestValidTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(request.convertToEntity());
    MentorshipRequest createdRequest = dataAccess.getMentorshipRequest(request.getDatastoreKey());
    assertNotNull(createdRequest);
    assertEquals(request.getToUserKey(), createdRequest.getToUserKey());
    assertEquals(request.getFromUserKey(), createdRequest.getFromUserKey());
  }

  @Test
  public void deleteRequestNonexistentRequestTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.deleteRequest(request));
  }

  @Test
  public void deleteRequestValidTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(request.convertToEntity());
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertTrue(dataAccess.deleteRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
  }

  @Test
  public void deleteRequestTwiceTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(request.convertToEntity());
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertTrue(dataAccess.deleteRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertFalse(dataAccess.deleteRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
  }

  @Test
  public void denyRequestNonexistentRequestTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.denyRequest(request));
  }

  @Test
  public void denyRequestValidTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(request.convertToEntity());
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertTrue(dataAccess.denyRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
  }

  @Test
  public void denyRequestTwiceTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(request.convertToEntity());
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertTrue(dataAccess.denyRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertFalse(dataAccess.denyRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
  }

  @Test
  public void approveRequestNonexistentRequestTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.approveRequest(request));
  }

  @Test
  public void approveRequestValidTest() throws EntityNotFoundException {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(request.convertToEntity());
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertTrue(dataAccess.approveRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    Entity createdRelation = ds.prepare(new Query("MentorMenteeRelation")).asSingleEntity();
    assertTrue(
        new Mentee(menteeEntity1)
            .looselyEquals(
                new Mentee(
                    ds.get(
                        KeyFactory.createKey(
                            "UserAccount", (long) createdRelation.getProperty("menteeKey"))))));
    assertTrue(
        new Mentor(mentorEntity1)
            .looselyEquals(
                new Mentor(
                    ds.get(
                        KeyFactory.createKey(
                            "UserAccount", (long) createdRelation.getProperty("mentorKey"))))));
  }

  @Test
  public void approveRequestTwiceTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorshipRequest request =
        new MentorshipRequest(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(request.convertToEntity());
    assertEquals(1, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertTrue(dataAccess.approveRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertFalse(dataAccess.approveRequest(request));
    assertEquals(0, ds.prepare(new Query("MentorshipRequest")).countEntities(withLimit(10)));
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
  }

  @Test
  public void makeMentorMenteeRelationNonexistentUsersTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    ds.delete(Arrays.asList(mentorEntity1.getKey(), menteeEntity1.getKey()));
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertEquals(0, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertFalse(
        dataAccess.makeMentorMenteeRelation(
            mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId()));
    assertEquals(0, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
  }

  @Test
  public void makeMentorMenteeRelationValidTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertEquals(0, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertTrue(
        dataAccess.makeMentorMenteeRelation(
            mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId()));
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
  }

  @Test
  public void makeMentorMenteeRelationTwiceTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertEquals(0, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertTrue(
        dataAccess.makeMentorMenteeRelation(
            mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId()));
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertFalse(
        dataAccess.makeMentorMenteeRelation(
            mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId()));
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
  }

  @Test
  public void getMentorMenteeRelationsNonexistentUserTest() {
    assertEquals(0, dataAccess.getMentorMenteeRelations(defaultMentee).size());
  }

  @Test
  public void getMentorMenteeRelationsValidTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertEquals(0, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertTrue(
        dataAccess.makeMentorMenteeRelation(
            mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId()));
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    ArrayList<MentorMenteeRelation> createdRelations =
        new ArrayList<>(dataAccess.getMentorMenteeRelations(new Mentee(menteeEntity1)));
    assertEquals(1, createdRelations.size());
    assertTrue(new Mentee(menteeEntity1).looselyEquals(createdRelations.get(0).getMentee()));
    assertTrue(new Mentor(mentorEntity1).looselyEquals(createdRelations.get(0).getMentor()));
  }

  @Test
  public void deleteMentorMenteeRelationNonexistentRequestTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorMenteeRelation relation =
        new MentorMenteeRelation(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    assertFalse(dataAccess.deleteMentorMenteeRelation(relation));
  }

  @Test
  public void deleteMentorMenteeRelationValidTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorMenteeRelation relation =
        new MentorMenteeRelation(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(relation.convertToEntity());
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertTrue(dataAccess.deleteMentorMenteeRelation(relation));
    assertEquals(0, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
  }

  @Test
  public void deleteMentorMenteeRelationTwiceTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity mentorEntity1 = new Entity("UserAccount");
    mentorEntity1.setPropertiesFrom(defaultMentorEntity);
    mentorEntity1.setProperty("userID", "201");
    Entity menteeEntity1 = new Entity("UserAccount");
    menteeEntity1.setPropertiesFrom(defaultMenteeEntity);
    menteeEntity1.setProperty("userID", "301");
    ds.put(Arrays.asList(mentorEntity1, menteeEntity1));
    MentorMenteeRelation relation =
        new MentorMenteeRelation(mentorEntity1.getKey().getId(), menteeEntity1.getKey().getId());
    assertEquals(2, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    ds.put(relation.convertToEntity());
    assertEquals(1, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertTrue(dataAccess.deleteMentorMenteeRelation(relation));
    assertEquals(0, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
    assertFalse(dataAccess.deleteMentorMenteeRelation(relation));
    assertEquals(0, ds.prepare(new Query("MentorMenteeRelation")).countEntities(withLimit(10)));
  }
}
