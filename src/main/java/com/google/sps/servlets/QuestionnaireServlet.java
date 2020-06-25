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
import com.google.sps.data.State;
import com.google.sps.util.ResourceConstants;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.loader.FileLocator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/questionnaire")
public class QuestionnaireServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("REQUEST AT: " + request.getServletPath());
    response.setContentType("text/html;");

    JinjavaConfig config = new JinjavaConfig();
    Jinjava jinjava = new Jinjava(config);
    try {
      jinjava.setResourceLocator(
          new FileLocator(new File(this.getClass().getResource(ResourceConstants.TEMPLATES).toURI())));
    } catch (URISyntaxException e) {
      System.err.println("templates dir not found!");
    }

    Map<String, Object> context = new HashMap<>();
    context.put("url", "/");
    context.put("countries", getCountries());
    context.put("states", getStates());
    String template =
        Resources.toString(
            this.getClass().getResource(ResourceConstants.TEMPLATE_QUESTIONNAIRE), Charsets.UTF_8);
    String renderedTemplate = jinjava.render(template, context);

    response.getWriter().println(renderedTemplate);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }


  private Collection<String> getCountries() {
    ArrayList<String> countries = new ArrayList<>();
    try {
      URL url = this.getClass().getClassLoader().getResource(ResourceConstants.COUNTRIES_FILE);
      Scanner s = new Scanner(new File(url.getFile()));
      while (s.hasNext()) {
        countries.add(s.nextLine());
      }
      s.close();
    } catch (IOException e) {
      System.out.println("failed to find file");
    }
    return countries;
  }

  private Collection<State> getStates() {
    ArrayList<State> states = new ArrayList<>();
    try {
      URL url = this.getClass().getClassLoader().getResource(ResourceConstants.STATES_FILE);
      Scanner s = new Scanner(new File(url.getFile()));
      while (s.hasNext()) {
        String[] nameAbbreviation = s.nextLine().split(", ");
        states.add(new State(nameAbbreviation[0], nameAbbreviation[1]));
      }
      s.close();
    } catch (IOException e) {
      System.out.println("failed to find file");
    }
    return states;
  }
}
