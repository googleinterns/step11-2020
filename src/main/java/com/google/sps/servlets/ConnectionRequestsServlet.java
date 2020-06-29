package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;

@WebServlet(urlPatterns = URLPatterns.CONNECTION_REQUESTS)
public class ConnectionRequestServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("REQUEST AT: " + request.getServletPath());
    response.setContentType("text/html;");

    JinjavaConfig config = new JinjavaConfig();
    Jinjava jinjava = new Jinjava(config);
    try {
      jinjava.setResourceLocator(
          new FileLocator(
              new File(this.getClass().getResource(ResourceConstants.TEMPLATES).toURI())));
    } catch (URISyntaxException e) {
      System.err.println("templates dir not found!");
    }

    Map<String, Object> context = new HashMap<>();
    context.put("url", "/");
    context.put("requests",  DummyDataAccess.getIncomingRequests(DummyDataAccess.getCurrentUser()));
    String template =
        Resources.toString(
            this.getClass().getResource(ResourceConstants.TEMPLATE_ABOUT), Charsets.UTF_8);
    String renderedTemplate = jinjava.render(template, context);

    response.getWriter().println(renderedTemplate);
    }
}
