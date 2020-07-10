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

package com.google.step.util;

public final class ErrorMessages {
  public static final String TEMPLATES_DIRECTORY_NOT_FOUND =
      "The templates directory was not found";
  public static final String TEMPLATE_FILE_NOT_FOUND = "The template was not found: ";
  public static final String INVALID_PARAMATERS = "insufficient or invalid parameters";
  public static final String BAD_REDIRECT = "Invalid encoded redirection pathname";
  public static final String BAD_DATE_PARSE = "Invalid date format";

  public static String templateFileNotFound(String templateURL) {
    return TEMPLATE_FILE_NOT_FOUND + templateURL;
  }

  public static String badRedirect(String encodedURL) {
    return BAD_REDIRECT + encodedURL;
  }
}
