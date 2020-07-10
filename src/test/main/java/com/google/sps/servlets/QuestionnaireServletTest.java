package com.google.sps.tests;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.servlets.QuestionnaireServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }
}
