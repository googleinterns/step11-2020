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

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides common functionality to all servlets to reduce code redundancy.
 *
 * @author guptamudit
 * @author tquintanilla
 * @version 1.1
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

  public static String[] getParameterValues(
      HttpServletRequest request, String parameterName, String[] defaultValues) {
    String[] values = request.getParameterValues(parameterName);
    if (values == null || values.length == 0) {
      values = defaultValues;
    }
    System.out.println(values);
    return values;
  }

  public static <E extends Enum<E>> String getOtherStringValue(
      E value, Class<E> enumClass, HttpServletRequest request, String otherParamTitle) {
    return value == Enum.valueOf(enumClass, "OTHER")
        ? getParameter(request, otherParamTitle, "")
        : "";
  }

  public static <E extends Enum<E>> E getEnumParameter(
      Class<E> enumClass, HttpServletRequest request, String paramTitle, String defaultValue) {
    return Enum.valueOf(enumClass, getParameter(request, paramTitle, defaultValue).toUpperCase());
  }

  public static <E extends Enum<E>> List<E> getListOfCheckedValues(
      Class<E> enumClass, HttpServletRequest request, String paramTitle, String defaultValue) {
    List<E> values = new ArrayList<>();
    String[] valuesList = getParameterValues(request, paramTitle, new String[] {defaultValue});
    for (String value : valuesList) {
      values.add(Enum.valueOf(enumClass, value));
    }
    return values;
  }
}
