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

import com.google.sps.util.URLPatterns;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet supports HTTP GET by immediately redirecting from the base url to the /landing page
 * of the mentor-matching platform.
 *
 * @author guptamudit
 * @version 1.0
 * @param URLPatterns.BASE this servlet serves requests at /
 * @param URLPatterns.MAIN this servlet serves requests at /main
 */
@WebServlet(urlPatterns = {URLPatterns.BASE, URLPatterns.MAIN})
public class MainServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendRedirect(URLPatterns.LANDING);
  }
}
