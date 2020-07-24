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

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.util.ErrorMessages;
import com.google.sps.util.ResourceConstants;
import com.google.sps.util.ServletUtils;
import com.google.sps.util.URLPatterns;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.loader.FileLocator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet supports HTTP GET and returns a static (except for the navbar) html page with a
 * brief summary of the mentor-matching platform.
 *
 * @author tquintanilla
 * @author guptamudit
 * @version 1.1
 * @param URLPatterns.ABOUT this servlet serves requests at /about
 */
@WebServlet(urlPatterns = URLPatterns.ABOUT)
public class AboutServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(AboutServlet.class.getName());

  private String staticResponse;
  private Jinjava jinjava;

  @Override
  public void init() {
    JinjavaConfig config = new JinjavaConfig();
    jinjava = new Jinjava(config);
    try {
      jinjava.setResourceLocator(
          new FileLocator(
              new File(this.getClass().getResource(ResourceConstants.TEMPLATES).toURI())));
    } catch (URISyntaxException | FileNotFoundException e) {
      LOG.severe(ErrorMessages.TEMPLATES_DIRECTORY_NOT_FOUND);
    }

    Map<String, Object> context = new HashMap<>();

    try {
      String template =
          Resources.toString(
              this.getClass().getResource(ResourceConstants.TEMPLATE_ABOUT), Charsets.UTF_8);
      staticResponse = jinjava.render(template, context);
    } catch (IOException e) {
      LOG.severe(ErrorMessages.templateFileNotFound(ResourceConstants.TEMPLATE_ABOUT));
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(ServletUtils.CONTENT_HTML);

    if (staticResponse == null) {
      response.setStatus(500);
      return;
    }

    Map<String, Object> context =
        new DatastoreAccess().getDefaultRenderingContext(URLPatterns.ABOUT);

    String rendered = jinjava.render(staticResponse, context);

    response.getWriter().println(rendered);
  }
}
