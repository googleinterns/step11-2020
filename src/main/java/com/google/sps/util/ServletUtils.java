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

package com.google.sps.util;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides common functionality to all servlets to reduce code redundancy.
 *
 * @author guptamudit
 * @version 1.0
 */
public final class ServletUtils {
  public static final String CONTENT_HTML = "text/html;";
  public static final String CONTENT_JSON = "application/json;";

  public static String getParameter(
      HttpServletRequest request, String parameterName, String defaultValue) {
    String value = request.getParameter(parameterName);
    if (value == null || value == "") {
      return defaultValue;
    }
    return value;
  }
}
