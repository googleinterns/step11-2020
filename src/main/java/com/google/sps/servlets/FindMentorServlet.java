// Copyright 2019 Google LLC
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

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.DummyDataAccess;
import com.google.sps.data.Mentor;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.loader.FileLocator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/find-mentor"})
public class FindMentorServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("REQUEST AT: " + request.getServletPath());

    response.setContentType("text/html;");

    JinjavaConfig config = new JinjavaConfig();
    Jinjava jinjava = new Jinjava(config);
    try {
      jinjava.setResourceLocator(
          new FileLocator(new File(this.getClass().getResource("/templates").toURI())));
    } catch (URISyntaxException e) {
      System.err.println("templates dir not found!");
    }

    Map<String, Object> context = new HashMap<>();
    context.put("url", "/find-mentor");
    Collection<Mentor> relatedMentors = new DummyDataAccess().getRelatedMentors(null);
    context.put("mentors", relatedMentors);

    String template =
        Resources.toString(
            this.getClass().getResource("/templates/find-mentor.html"), Charsets.UTF_8);

    String renderedTemplate = jinjava.render(template, context);

    response.getWriter().println(renderedTemplate);
  }
}
