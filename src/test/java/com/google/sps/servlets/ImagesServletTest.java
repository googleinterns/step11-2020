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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.sps.data.DatastoreAccess;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * This test class uses JUnit to test that the ImagesServlet properly serves images from the
 * BlobstoreService.
 *
 * @param JUnit4.class makes the tests run under the JUnit 4 framework
 * @author guptamudit
 * @version 1.0
 */
@RunWith(JUnit4.class)
public final class ImagesServletTest {
  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private DatastoreAccess dataAccess;
  @InjectMocks private ImagesServlet servlet;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getWithBadParamsTest() throws Exception {
    servlet.doGet(request, response);

    verify(dataAccess, never()).serveBlob(any(HttpServletResponse.class), anyString());
  }

  @Test
  public void getWithCorrectParamsTest() throws Exception {
    String expectedBlobKeyString = "blobKey";
    when(request.getParameter("blob-key")).thenReturn(expectedBlobKeyString);

    servlet.doGet(request, response);

    verify(dataAccess).serveBlob(response, expectedBlobKeyString);
  }
}
