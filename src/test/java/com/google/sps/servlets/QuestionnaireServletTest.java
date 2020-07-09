package com.google.sps.tests;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import com.google.sps.servlets.QuestionnaireServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public final class QuestionnaireServletTest {
  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private HttpServlet servlet;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
  }

  // @Test
  // public void

  @Test
  public void correctNameInText() throws Exception {
    when(request.getParameter("name")).thenReturn("jake");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    new QuestionnaireServlet().doPost(request, response);

    verify(request).getParameter("name");
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("jake"));
  }

  @Test
  private void useDefaultValueWhenNoneProvidedForString() throws Exception {
    when(request.getParameter("name")).thenReturn("");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);

    verify(request).getParameter("name");
    writer.flush();
    assertTrue(stringWriter.toString().contains("John Doe"));
  }
}
