package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.DummyDataAccess;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.util.URLPatterns;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(URLPatterns.DATASTORE_TEST)
public class DatastoreTestServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    DatastoreAccess dataAccess = new DatastoreAccess();
    dataAccess.saveUser((new DummyDataAccess()).getMentor(dataAccess.getCurrentUser().getUserId()));
    response.setContentType("application/json;");
    Collection<Mentor> mentors = dataAccess.getRelatedMentors(null);
    Collection<Mentee> mentees = dataAccess.getRelatedMentees(null);
    Gson gson = new Gson();
    String mentorsString = gson.toJson(mentors);
    String menteesString = gson.toJson(mentees);
    response
        .getWriter()
        .println("{\"mentors\":\n" + mentorsString + "," + "\"mentees\":\n" + menteesString + "}");
  }
}
