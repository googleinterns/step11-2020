package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.util.RandomObjects;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Seeds the database with based on a large input dataset in JSON format. Upon calling HTTP GET,
 * this servlet attempts to seed the database and returns a JSON object representing the operations
 * success.
 *
 * @author sylviaziyuz
 * @author guptamudit
 * @version 1.0
 * @param URLPatterns.SEED_DB this servlet serves requests at /seed-db
 */
@WebServlet("random-data")
public class RandomDataServlet extends HttpServlet {

  private Gson gson;

  @Override
  public void init() {
    gson = new Gson();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    List<Mentor> mentors =
        Stream.generate(RandomObjects::randomMentor).limit(5000).collect(Collectors.toList());

    List<Mentee> mentees =
        Stream.generate(RandomObjects::randomMentee).limit(5000).collect(Collectors.toList());

    response.setContentType("application/json;");
    response
        .getWriter()
        .println(
            "{\"mentors\":\n"
                + gson.toJson(mentors)
                + ",\n \"mentees\":\n"
                + gson.toJson(mentees)
                + "}");
  }
}
