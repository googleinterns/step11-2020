// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.sps.data.DataAccess;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.util.EnumAdapterFactory;
import com.google.sps.util.ServletUtils;
import com.google.sps.util.URLPatterns;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet supports HTTP GET and returns an html page with a information about each of the
 * mentors that may be similar to the currently logged in mentee. If the mentor finds one of the
 * mentors relatable, they can send them a mentorship request. This servlet supports HTTP POST for
 * mentees to send requests to or dislike mentors.
 *
 * @author sylviaziyuz
 * @version 1.0
 * @param URLPatterns.REFILL_MENTOR this servlet serves requests at /find-mentor
 */
@WebServlet(urlPatterns = URLPatterns.REFILL_MENTOR)
public class RefillMentorServlet extends HttpServlet {

  private DataAccess dataAccess;
  private Gson gson;

  @Override
  public void init() {
    dataAccess = new DatastoreAccess();
    gson = (new GsonBuilder()).registerTypeAdapterFactory(new EnumAdapterFactory()).create();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    User user = dataAccess.getCurrentUser();
    if (user != null) {
      Mentee mentee = dataAccess.getMentee(user.getUserId());
      if (mentee != null) {
        response.setContentType(ServletUtils.CONTENT_JSON);
        Collection<Mentor> relatedMentors = dataAccess.getRelatedMentors(mentee);
        response.getWriter().println(gson.toJson(relatedMentors));
        return;
      }
    }
    response.sendRedirect(URLPatterns.LANDING);
  }
}
