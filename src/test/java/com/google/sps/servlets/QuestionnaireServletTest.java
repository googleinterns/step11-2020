import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.servlets.QuestionnaireServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.Date;
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
import com.google.sps.util.ContextFields;
import com.google.sps.util.ErrorMessages;
import com.google.sps.util.ParameterConstants;
import com.google.sps.util.ResourceConstants;
import com.google.sps.util.ServletUtils;
import com.google.sps.util.URLPatterns;

/**
 * TO RUN PROJECT WITHOUT TESTS RUN COMMAND: mvn package appengine:run -DskipTests Basic structure
 * of servlet tests using LocalServiceTestHelper for intiailizing services (dodges no api in thread
 * error), for more help with unit testing visit:
 * https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
@RunWith(JUnit4.class)
public final class QuestionnaireServletTest {
  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  private DatastoreAccess dataAccess;
  @InjectMocks private QuestionnaireServlet servlet;
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
              new LocalUserServiceTestConfig(), new LocalDatastoreServiceTestConfig())
          .setEnvAttributes(Map.of("com.google.appengine.api.users.UserService.user_id_key", "101"))
          .setEnvEmail("mudito@example.com")
          .setEnvAuthDomain("gmail.com")
          .setEnvIsLoggedIn(true);
  private Mentor defaultMentor;
  private Mentee defaultMentee;
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    helper.setUp();
    dataAccess = new DatastoreAccess();
    defaultMentor = (new Mentor.Builder())
        .name("Mudito Mentor")
        .userID("101")
        .email("mudito.mentor@example.com")
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

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void correctNameInText() throws Exception {
    when(request.getParameter("name")).thenReturn("jake");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    verify(request).getParameter("name");
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("jake"));
  }

  @Test
  public void defaultNameInText() throws Exception {
    when(request.getParameter("name")).thenReturn("");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    verify(request).getParameter("name");
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("John Doe"));
  }

  @Test
  public void requestParamLoadsRespectiveTemplate() throws Exception {
    servlet.init();
    when(request.getParameter("formType")).thenReturn("mentor");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doGet(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("id=\"formType\" value=mentor"));
  }

  @Test
  public void existingParamLoadsRespectiveTemplate() throws Exception {
    servlet.init();

    dataAccess.createUser(defaultMentor);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doGet(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("id=\"formType\" value=mentor"));
  }

  @Test
  public void checkExistingValueIsSelected() throws Exception {
    servlet.init();

    dataAccess.createUser(defaultMentor);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doGet(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("value=\"INDIAN\" checked"));
    Assert.assertFalse(stringWriter.toString().contains("value=\"CAUCASIAN\" checked"));
    Assert.assertTrue(stringWriter.toString().contains("value=\"HIGHSCHOOL\" selected"));
    Assert.assertFalse(stringWriter.toString().contains("value=\"NONE\" selected"));
    Assert.assertTrue(stringWriter.toString().contains("value=\"Mudito Mentor\""));
  }

  @Test
  public void updatedDoMethodBasedOnExistingUser() throws Exception {
    servlet.init();

    dataAccess.createUser(defaultMentor);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doGet(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("method=\"PUT\""));
  }

  @Test
  public void otherEthnicityStringInputProperlyStored() throws Exception {
    when(request.getParameter("ethnicity")).thenReturn("OTHER");
    when(request.getParameter("ethnicityOther")).thenReturn("Tunisian");
    UserService userService = UserServiceFactory.getUserService();
    when(dataAccess.getCurrentUser()).thenReturn(new User("foo@gmail.com", "gmail.com", "123"));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    verify(request).getParameter("ethnicityOther");
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Tunisian"));
  }

  @Test
  public void otherGenderStringInputProperlyStored() throws Exception {
    when(request.getParameter("gender")).thenReturn("OTHER");
    when(request.getParameter("genderOther")).thenReturn("omeganonbinary");
    UserService userService = UserServiceFactory.getUserService();
    when(dataAccess.getCurrentUser()).thenReturn(new User("foo@gmail.com", "gmail.com", "123"));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("omeganonbinary"));
  }

  @Test
  public void otherEthnicityStringIsBlank() throws Exception {
    when(request.getParameter("ethnicity")).thenReturn("CAUCASIAN");
    when(request.getParameter("ethnicityOther")).thenReturn("Tunisian");
    UserService userService = UserServiceFactory.getUserService();
    when(dataAccess.getCurrentUser()).thenReturn(new User("foo@gmail.com", "gmail.com", "123"));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    writer.flush();
    Assert.assertFalse(stringWriter.toString().contains("Tunisian"));
  }

  @Test
  public void otherGenderStringIsBlank() throws Exception {
    when(request.getParameter("gender")).thenReturn("NONBINARY");
    when(request.getParameter("genderOther")).thenReturn("omeganonbinary");
    UserService userService = UserServiceFactory.getUserService();
    when(dataAccess.getCurrentUser()).thenReturn(new User("foo@gmail.com", "gmail.com", "123"));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    writer.flush();
    Assert.assertFalse(stringWriter.toString().contains("omeganonbinary"));
  }
}
