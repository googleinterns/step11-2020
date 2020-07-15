import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.UserAccount;
import java.util.Arrays;
import java.util.Date;
import java.util.Collection;
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
  // @Mock private UserService userService;
  // @Mock private DatastoreService datastoreService;
  private DatastoreAccess dataAccess;
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
              new LocalUserServiceTestConfig(), new LocalDatastoreServiceTestConfig())
          .setEnvAttributes(Map.of("com.google.appengine.api.users.UserService.user_id_key", "101"))
          .setEnvEmail("mudito@example.com")
          .setEnvAuthDomain("gmail.com")
          .setEnvIsLoggedIn(true);
  private final Entity defaultMenteeEntity = new Entity("UserAccount");
  private final Entity defaultMentorEntity = new Entity("UserAccount");

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    helper.setUp();
    dataAccess = new DatastoreAccess();
    Entity defaultUserAccountEntity = new Entity("UserAccount");
    defaultUserAccountEntity.setProperty("userID", "101");
    defaultUserAccountEntity.setProperty("email", "mudito@example.com");
    defaultUserAccountEntity.setProperty("name", "Mudito Gupta");
    defaultUserAccountEntity.setProperty("dateOfBirth", new Date(984787200000));
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
    defaultMenteeEntity.setPropertiesFrom(defaultUserAccountEntity);
    defaultMenteeEntity.setProperty("userType", UserType.MENTEE.ordinal());
    defaultMenteeEntity.setProperty("goal", goal.ordinal());
    defaultMenteeEntity.setProperty(
        ParameterConstants."desiredMeetingFrequency", desiredMeetingFrequency.ordinal());
    defaultMenteeEntity.setProperty("dislikedMentorKeys", Arrays.asList());
    defaultMenteeEntity.setProperty("desiredMentorType", desiredMentorType.ordinal());
    defaultMentorEntity.setPropertiesFrom(defaultUserAccountEntity);
    defaultMentorEntity.setProperty("userType", UserType.MENTOR.ordinal());
    defaultMentorEntity.setProperty(ParameterConstants.MENTOR_VISIBILITY, visibility);
    defaultMentorEntity.setProperty(
        ParameterConstants.MENTOR_FOCUS_LIST,
        focusList.stream().map(focus -> focus.ordinal()).collect(Collectors.toList()));
    defaultMentorEntity.setProperty(ParameterConstants.MENTOR_TYPE, mentorType.ordinal());
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void singleSeedingTest() throws Exception {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("UserAccount")).countEntities(withLimit(10)));
    Entity entity = new Entity("UserAccount");
    Collection<Entity> entities = Arrays.asList(entity);
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
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity entity = new Entity("UserAccount");
    entity.setProperty("userID", "101");
    ds.put(entity);
    UserAccount user = dataAccess.getUser("101");
    assertNotNull(user);
    assertEquals(user.getUserID(), "101");
  }

  @Test
  public void getUserByUserIdNonexistentTest() {
    UserAccount user = dataAccess.getUser("101");
    assertNull(user);
  }
}
