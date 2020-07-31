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
 * This class stores a constant list of url patterns. The constants stored in this class are used by
 * servlets to control what URLs they will serve and redirect to.
 *
 * @author guptamudit
 * @author tquintanilla
 * @author sylviaziyuz
 * @version 1.0
 */
public final class URLPatterns {
  public static final String BASE = "/";
  public static final String ABOUT = "/about";
  public static final String AUTHORS = "/authors";
  public static final String DASHBOARD = "/dashboard";
  public static final String FIND_MENTOR = "/find-mentor";
  public static final String IMAGES = "/images";
  public static final String LANDING = "/landing";
  public static final String LOGOUT = "/logout";
  public static final String MAIN = "/main";
  public static final String MENTORSHIP_REQUESTS = "/mentorship-requests";
  public static final String PROFILE = "/profile";
  public static final String QUESTIONNAIRE = "/questionnaire";
  public static final String REFILL_MENTOR = "/refill-mentor";
  public static final String SEED_DB = "/seed-db";

  public static final String DEFAULT_PROFILE_IMAGE = "/static/images/defaultProfilePicture.png";
}
