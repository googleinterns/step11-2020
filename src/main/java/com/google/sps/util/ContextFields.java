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
 * This class stores a constant list of of possible fields for jinja rendering contexts. These
 * constants are used when passing parameters into the context for rendering templates with Jinja.
 * The use of these constants helps avoid typos when passing variables to the jinja renderer from
 * different files.
 *
 * @author guptamudit
 * @version 1.0
 */
public class ContextFields {
  public static final String URL = "url";
  public static final String IS_LOGGED_IN = "isLoggedIn";
  public static final String TOGGLE_LOGIN_URL = "toggleLoginURL";
  public static final String IS_MENTOR = "isMentor";
  public static final String IS_MENTEE = "isMentee";
  public static final String CURRENT_USER = "currentUser";

  public static final String FORM_TYPE = "formType";
  public static final String QUESTIONNAIRE_SUBMIT_URL = "questionnaireSubmitURL";

  public static final String PROFILE_USER = "profileUser";

  public static final String MENTORS = "mentors";

  public static final String MENTORSHIP_REQUESTS = "mentorshipRequests";

  public static final String MENTOR_MENTEE_RELATIONS = "mentorMenteeRelations";
}
