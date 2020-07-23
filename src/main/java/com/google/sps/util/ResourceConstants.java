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

package com.google.sps.util;

/**
 * This class stores a constant list of of file paths for resources. These file locations are used
 * by various java classes to access resource files. Storing constants helps avoid typo-induced
 * errors when loading resources. The template constants refer to the templates for the various html
 * pages.
 *
 * @author guptamudit
 * @author sylviaziyuz
 * @author tquintanilla
 */
public final class ResourceConstants {
  public static final String TEMPLATES = "/templates";
  public static final String TEMPLATE_ABOUT = "/templates/about.html";
  public static final String TEMPLATE_AUTHORS = "/templates/authors.html";
  public static final String TEMPLATE_FIND_MENTOR = "/templates/find-mentor.html";
  public static final String TEMPLATE_LANDING = "/templates/landing.html";
  public static final String TEMPLATE_PROFILE = "/templates/profile.html";
  public static final String TEMPLATE_MENTORSHIP_REQUESTS = "/templates/mentorship-requests.html";
  public static final String TEMPLATE_QUESTIONNAIRE = "/templates/questionnaire.html";
  public static final String TEMPLATE_MENTOR_DASHBOARD = "/templates/mentor-dashboard.html";
  public static final String TEMPLATE_MENTEE_DASHBOARD = "/templates/mentee-dashboard.html";

  public static final String DUMMY_DATA_USERS = "/dummy_data/users.json";
  public static final String DUMMY_DATA_NAMES = "/dummy_data/names.txt";
}
