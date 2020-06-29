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
import com.google.sps.util.ResourceConstants;
import com.google.sps.data.LoginState;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/authenticate"})
public class AuthenticateServlet extends HttpServlet {

  private LoginState loginState = new LoginState();
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    UserService userService = UserServiceFactory.getUserService();
    String responseString;
    if (!userService.isUserLoggedIn()) {
      String redirUrlAfterLogin = request.getRequestURL().toString();
      loginState.toggleLoginURL = userService.createLoginURL(redirUrlAfterLogin);
      loginState.isLoggedIn = false;
    }
    else {
      String redirUrlAfterLogout = "/";
      loginState.toggleLoginURL = userService.createLogoutURL(redirUrlAfterLogout);
      loginState.userProfileURL = "/" + userService.getCurrentUser().getUserID(); + "/profile";
      loginState.isLoggedIn = true;
    }
    response.getWriter().println(new Gson().toJson(loginState));
  }
}
