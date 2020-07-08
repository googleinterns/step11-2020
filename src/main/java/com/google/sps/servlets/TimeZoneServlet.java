package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.TimeZoneInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("timezone")
public class TimeZoneServlet extends HttpServlet {
  private static final int FAKE_USER_COUNT = 502;

  private DatastoreAccess dataAccess;
  private Gson gson;

  @Override
  public void init() {
    dataAccess = new DatastoreAccess();
    gson = new Gson();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Collection<TimeZoneInfo> zones =
        TimeZoneInfo.getListOfNamesToDisplay(
            Arrays.asList(TimeZone.getAvailableIDs()).stream()
                .filter(strID -> strID.toUpperCase().equals(strID))
                .map(strID -> TimeZone.getTimeZone(strID))
                .collect(Collectors.toList()));

    response.setContentType("application/json;");
    response.getWriter().println("{\"zones\":\n" + gson.toJson(zones) + "}");
  }
}
