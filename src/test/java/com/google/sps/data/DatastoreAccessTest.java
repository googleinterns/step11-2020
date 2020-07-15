import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.Country;
import com.google.sps.data.DataAccess;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.EducationLevel;
import com.google.sps.data.Ethnicity;
import com.google.sps.data.Gender;
import com.google.sps.data.Language;
import com.google.sps.data.MeetingFrequency;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MentorType;
import com.google.sps.data.TimeZoneInfo;
import com.google.sps.data.Topic;
import com.google.sps.data.UserAccount;
import com.google.sps.data.UserType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

/**
 * TO RUN PROJECT WITHOUT TESTS RUN COMMAND: mvn package appengine:run -DskipTests Basic structure
 * of servlet tests using LocalServiceTestHelper for intiailizing services (dodges no api in thread
 * error), for more help with unit testing visit:
 * https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
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

  public void setUpDefaultEntities() {
    Entity defaultUserAccountEntity = new Entity("UserAccount");
    defaultUserAccountEntity.setProperty("userID", "101");
    defaultUserAccountEntity.setProperty("email", "mudito@example.com");
    defaultUserAccountEntity.setProperty("name", "Mudito Gupta");
    defaultUserAccountEntity.setProperty("dateOfBirth", new Date(984787200000L));
    defaultUserAccountEntity.setProperty("country", Country.US.ordinal());
    defaultUserAccountEntity.setProperty("language", Language.EN.ordinal());
    defaultUserAccountEntity.setProperty("timezone", "GMT");
    defaultUserAccountEntity.setProperty("ethnicity", Arrays.asList(Ethnicity.INDIAN.ordinal()));
    defaultUserAccountEntity.setProperty("ethnicityOther", "");
    defaultUserAccountEntity.setProperty("gender", Gender.MAN.ordinal());
    defaultUserAccountEntity.setProperty("genderOther", "");
    defaultUserAccountEntity.setProperty("firstGen", false);
    defaultUserAccountEntity.setProperty("lowIncome", false);
    defaultUserAccountEntity.setProperty("educationLevel", EducationLevel.HIGHSCHOOL.ordinal());
    defaultUserAccountEntity.setProperty("educationLevelOther", "");
    defaultUserAccountEntity.setProperty("description", "I am very cool.");

    defaultMenteeEntity = new Entity("UserAccount");
    defaultMenteeEntity.setPropertiesFrom(defaultUserAccountEntity);
    defaultMenteeEntity.setProperty("userType", UserType.MENTEE.ordinal());
    defaultMenteeEntity.setProperty("goal", Topic.COMPUTER_SCIENCE.ordinal());
    defaultMenteeEntity.setProperty("desiredMeetingFrequency", MeetingFrequency.WEEKLY.ordinal());
    defaultMenteeEntity.setProperty("dislikedMentorKeys", Arrays.asList());
    defaultMenteeEntity.setProperty("mentorType", MentorType.CAREER.ordinal());

    defaultMentorEntity = new Entity("UserAccount");
    defaultMentorEntity.setPropertiesFrom(defaultUserAccountEntity);
    defaultMentorEntity.setProperty("userType", UserType.MENTOR.ordinal());
    defaultMentorEntity.setProperty("visibility", true);
    defaultMentorEntity.setProperty("focusList", Arrays.asList(Topic.COMPUTER_SCIENCE.ordinal()));
    defaultMentorEntity.setProperty("mentorType", MentorType.CAREER.ordinal());
  }

  public void setUpDefaultObjects() {
    defaultMentee = (new Mentee.Builder())
        .name("Mudito Gupta")
        .userID("101")
        .email("mudito@example.com")
        .dateOfBirth(new Date(984787200000L))
        .country(Country.US)
        .language(Language.EN)
        .timezone(new TimeZoneInfo(TimeZone.getTimeZone("GMT")))
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

    defaultMentor = (new Mentor.Builder())
        .name("Mudito Gupta")
        .userID("101")
        .email("mudito@example.com")
        .dateOfBirth(new Date(984787200000L))
        .country(Country.US)
        .language(Language.EN)
        .timezone(new TimeZoneInfo(TimeZone.getTimeZone("GMT")))
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
    MockitoAnnotations.initMocks(this);
    helper.setUp();
    dataAccess = new DatastoreAccess();

    setUpDefaultEntities();

    setUpDefaultObjects();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }



  @Test
  public void singleSeedingTest() throws Exception {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    Collection<Entity> entities = Arrays.asList(defaultMenteeEntity, defaultMentorEntity);
    boolean seeded = dataAccess.seed_db(entities);
    assertTrue(seeded);
    assertEquals(
        entities.size(), ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
  }



  @Test
  public void getCurrentUserTest() {
    User user = dataAccess.getCurrentUser();
    assertEquals(user.getEmail(), "mudito@example.com");
    assertEquals(user.getUserId(), "101");
  }
  @Test
  public void getCurrentUserLoggedOutTest() {
    helper.setEnvIsLoggedIn(false);
    User user = dataAccess.getCurrentUser();
    assertNull(user);
  }



  @Test
  public void getUserByUserIdTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMenteeEntity);
    final String testUserID = "1234";
    entity.setProperty("userID", testUserID);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    UserAccount user = dataAccess.getUser(testUserID);
    assertNotNull(user);
    assertEquals(user.getUserID(), testUserID);
  }
  @Test
  public void getUserByUserIdNonexistentTest() {
    final String testUserID = "1234";
    UserAccount user = dataAccess.getUser(testUserID);
    assertNull(user);
  }

  @Test
  public void getUserByDatastoreKeyTest() {
    final long testKeyId = 1234;
    Key key = KeyFactory.createKey("UserAccount", testKeyId);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMenteeEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    UserAccount user = dataAccess.getUser(testKeyId);
    assertNotNull(user);
    assertEquals(user.getDatastoreKey(), testKeyId);
  }
  @Test
  public void getUserByDatastoreKeyNonexistentTest() {
    final long testKeyId = 1234;
    UserAccount user = dataAccess.getUser(testKeyId);
    assertNull(user);
  }



  @Test
  public void getMenteeByUserIdTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMenteeEntity);
    final String testUserID = "1234";
    entity.setProperty("userID", testUserID);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentee mentee = dataAccess.getMentee(testUserID);
    assertNotNull(mentee);
    assertEquals(mentee.getUserID(), testUserID);
  }
  @Test
  public void getMenteeByUserIdNonexistentTest() {
    final String testUserID = "1234";
    Mentee mentee = dataAccess.getMentee(testUserID);
    assertNull(mentee);
  }
  @Test
  public void getMenteeByUserIdButItsAMentorTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMentorEntity);
    final String testUserID = "1234";
    entity.setProperty("userID", testUserID);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentee mentee = dataAccess.getMentee(testUserID);
    assertNull(mentee);
  }

  @Test
  public void getMenteeByDatastoreKeyTest() {
    final long testKeyId = 1234;
    Key key = KeyFactory.createKey("UserAccount", testKeyId);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMenteeEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentee mentee = dataAccess.getMentee(testKeyId);
    assertNotNull(mentee);
    assertEquals(mentee.getDatastoreKey(), testKeyId);
  }
  @Test
  public void getMenteeByDatastoreKeyNonexistentTest() {
    final long testKeyId = 1234;
    Mentee mentee = dataAccess.getMentee(testKeyId);
    assertNull(mentee);
  }
  @Test
  public void getMenteeByDatastoreKeyButItsAMentorTest() {
    final long testKeyId = 1234;
    Key key = KeyFactory.createKey("UserAccount", testKeyId);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMentorEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentee mentee = dataAccess.getMentee(testKeyId);
    assertNull(mentee);
  }



  @Test
  public void getMentorByUserIdTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMentorEntity);
    final String testUserID = "1234";
    entity.setProperty("userID", testUserID);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentor mentor = dataAccess.getMentor(testUserID);
    assertNotNull(mentor);
    assertEquals(mentor.getUserID(), testUserID);
  }
  @Test
  public void getMentorByUserIdNonexistentTest() {
    final String testUserID = "1234";
    Mentor mentor = dataAccess.getMentor(testUserID);
    assertNull(mentor);
  }
  @Test
  public void getMentorByUserIdButItsAMenteeTest() {
    Entity entity = new Entity("UserAccount");
    entity.setPropertiesFrom(defaultMenteeEntity);
    final String testUserID = "1234";
    entity.setProperty("userID", testUserID);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentor mentor = dataAccess.getMentor(testUserID);
    assertNull(mentor);
  }

  @Test
  public void getMentorByDatastoreKeyTest() {
    final long testKeyId = 1234;
    Key key = KeyFactory.createKey("UserAccount", testKeyId);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMentorEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentor mentor = dataAccess.getMentor(testKeyId);
    assertNotNull(mentor);
    assertEquals(mentor.getDatastoreKey(), testKeyId);
  }
  @Test
  public void getMentorByDatastoreKeyNonexistentTest() {
    final long testKeyId = 1234;
    Mentor mentor = dataAccess.getMentor(testKeyId);
    assertNull(mentor);
  }
  @Test
  public void getMentorByDatastoreKeyButItsAMenteeTest() {
    final long testKeyId = 1234;
    Key key = KeyFactory.createKey("UserAccount", testKeyId);
    Entity entity = new Entity(key);
    entity.setPropertiesFrom(defaultMenteeEntity);
    DatastoreServiceFactory.getDatastoreService().put(entity);
    Mentor mentor = dataAccess.getMentor(testKeyId);
    assertNull(mentor);
  }



  @Test
  public void createUserEmptyDatabaseTest() {
    boolean saved = dataAccess.createUser(defaultMentee);
    assertTrue(saved);
  }
  @Test
  public void createUserAlreadyInDatabaseTest() {
    assertTrue(dataAccess.createUser(defaultMentee));
    assertFalse(dataAccess.createUser(defaultMentee));
  }
  @Test
  public void createUserMultipleTest() {
    assertTrue(dataAccess.createUser(defaultMentee));
    assertTrue(dataAccess.createUser(defaultMentor));
  }



}
