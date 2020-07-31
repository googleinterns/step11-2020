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
import com.google.sps.util.ServletUtils;
import com.google.sps.util.URLPatterns;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet supports HTTP GET and returns an image for the specified blob key. Upon HTTP GET,
 * this servlet will send back an image to be displayed on user profiles.
 *
 * @author tquintanilla
 * @author guptamudit
 * @version 1.1
 * @param URLPatterns.IMAGES this servlet serves requests at /images
 */
@WebServlet(urlPatterns = URLPatterns.IMAGES)
public class ImagesServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(ImagesServlet.class.getName());

  private DataAccess dataAccess;

  public ImagesServlet() {
    this(new DatastoreAccess());
  }

  public ImagesServlet(DatastoreAccess dataAccess) {
    this.dataAccess = dataAccess;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String blobKeyString = ServletUtils.getParameter(request, "blob-key", "");
    if (blobKeyString != "") {
      dataAccess.serveBlob(response, blobKeyString);
    }
  }
}
