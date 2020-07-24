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

import com.google.sps.data.DataAccess;
import com.google.sps.data.DatastoreAccess;
import com.google.sps.util.ParameterConstants;
import com.google.sps.util.ServletUtils;
import com.google.sps.util.URLPatterns;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet allows people to logout from the app and redirects them back to the page they came
 * from.
 *
 * @author guptamudit
 * @version 1.0
 * @param URLPatterns.LOGOUT this servlet serves requests at /logout
 */
@WebServlet(urlPatterns = URLPatterns.LOGOUT)
public class LogoutServlet extends HttpServlet {

  private DataAccess dataAccess;

  @Override
  public void init() {
    dataAccess = new DatastoreAccess();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String redirURL =
        ServletUtils.getParameter(request, ParameterConstants.REDIR, URLPatterns.LANDING);
    if (dataAccess.getCurrentUser() != null) {
      ServletUtils.eraseCookie(response, ServletUtils.DEV_SERVER_AUTH_COOKIE);
      ServletUtils.eraseCookie(response, ServletUtils.HTTP_AUTH_COOKIE);
      ServletUtils.eraseCookie(response, ServletUtils.HTTPS_AUTH_COOKIE);
    }
    response.sendRedirect(redirURL);
  }
}
