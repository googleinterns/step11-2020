import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.servlets.QuestionnaireServlet;
import com.google.sps.util.ContextFields;
import com.google.sps.util.ParameterConstants;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
  @Mock private DatastoreAccess dataAccess;
  @InjectMocks private QuestionnaireServlet servlet;
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
              new LocalUserServiceTestConfig().setOAuthUserId("foo").setOAuthEmail("foo@gmail.com"))
          .setEnvIsLoggedIn(true);

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void correctNameInText() throws Exception {
    when(request.getParameter(ParameterConstants.NAME)).thenReturn("jake");
    UserService userService = UserServiceFactory.getUserService();
    when(dataAccess.getCurrentUser()).thenReturn(new User("foo@gmail.com", "gmail.com", "123"));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    verify(request).getParameter(ParameterConstants.NAME);
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("jake"));
  }

  @Test
  public void defaultNameInText() throws Exception {
    when(request.getParameter(ParameterConstants.NAME)).thenReturn("");
    UserService userService = UserServiceFactory.getUserService();
    when(dataAccess.getCurrentUser()).thenReturn(new User("foo@gmail.com", "gmail.com", "123"));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    verify(request).getParameter(ParameterConstants.NAME);
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("John Doe"));
  }

  @Test
  public void mentorParamLoadsRespectiveTemplate() throws Exception {
    servlet.init();
    when(request.getParameter(ContextFields.FORM_TYPE)).thenReturn("mentor");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doGet(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("id=\"formType\" value=mentor"));
  }

  @Test
  public void otherStringInputProperlyStored() throws Exception {
    when(request.getParameter(ParameterConstants.ETHNICITY)).thenReturn("OTHER");
    when(request.getParameter(ParameterConstants.ETHNICITY_OTHER)).thenReturn("Tunisian");
    UserService userService = UserServiceFactory.getUserService();
    when(dataAccess.getCurrentUser()).thenReturn(new User("foo@gmail.com", "gmail.com", "123"));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    verify(request).getParameter(ParameterConstants.ETHNICITY_OTHER);
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Tunisian"));
  }

  @Test
  public void otherStringIsBlank() throws Exception {
    when(request.getParameter(ParameterConstants.ETHNICITY)).thenReturn("CAUCASIAN");
    when(request.getParameter(ParameterConstants.ETHNICITY_OTHER)).thenReturn("Tunisian");
    UserService userService = UserServiceFactory.getUserService();
    when(dataAccess.getCurrentUser()).thenReturn(new User("foo@gmail.com", "gmail.com", "123"));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    writer.flush();
    Assert.assertFalse(stringWriter.toString().contains("Tunisian"));
  }
}
