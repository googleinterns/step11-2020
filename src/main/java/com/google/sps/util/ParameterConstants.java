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
 * This class stores a constant list of of various attribute names related to each object. These
 * constants are used for keyword maintenance for datastore entity properties, HttpRequest parameter
 * names, and datastore queries.
 *
 * @author tquintanilla
 * @author sylviaziyuz
 * @author guptamudit
 * @version 1.0
 */
public final class ParameterConstants {
  public static final String ENTITY_TYPE_USER_ACCOUNT = "UserAccount";
  public static final String USER_ID = "userID";
  public static final String EMAIL = "email";
  public static final String NAME = "name";
  public static final String DATE_OF_BIRTH = "dateOfBirth";
  public static final String COUNTRY = "country";
  public static final String LANGUAGE = "language";
  public static final String TIMEZONE = "timezone";
  public static final String ENCODED_CURSOR = "encodedCursor";
  public static final String ETHNICITY = "ethnicity";
  public static final String ETHNICITY_OTHER = "ethnicityOther";
  public static final String GENDER = "gender";
  public static final String GENDER_OTHER = "genderOther";
  public static final String FIRST_GEN = "firstGen";
  public static final String LOW_INCOME = "lowIncome";
  public static final String EDUCATION_LEVEL = "educationLevel";
  public static final String EDUCATION_LEVEL_OTHER = "educationLevelOther";
  public static final String DESCRIPTION = "description";
  public static final String PROFILE_PIC_BLOB_KEY = "profilePicBlobKey";
  public static final String USER_TYPE = "userType";
  public static final String IS_FAKE_USER = "isFakeUser";
  public static final String MENTEE_GOAL = "goal";
  public static final String MENTEE_DESIRED_MEETING_FREQUENCY = "desiredMeetingFrequency";
  public static final String MENTEE_DESIRED_MENTOR_TYPE = "desiredMentorType";
  public static final String MENTEE_DISLIKED_MENTOR_KEYS = "dislikedMentorKeys";
  public static final String MENTEE_LAST_DISLIKED_MENTOR_KEY = "lastDislikedMentorKey";
  public static final String MENTEE_LAST_REQUESTED_MENTOR_KEY = "lastRequestedMentorKey";
  public static final String MENTEE_REQUESTED_MENTOR_KEYS = "requestedMentorKeys";
  public static final String MENTEE_SERVED_MENTOR_KEYS = "servedMentorKeys";
  public static final String MENTOR_VISIBILITY = "visibility";
  public static final String MENTOR_FOCUS_LIST = "focusList";
  public static final String MENTOR_TYPE = "mentorType";

  public static final String ENTITY_TYPE_MENTORSHIP_REQUEST = "MentorshipRequest";
  public static final String TO_USER_KEY = "toUserKey";
  public static final String FROM_USER_KEY = "fromUserKey";

  public static final String ENTITY_TYPE_MENTOR_MENTEE_RELATION = "MentorMenteeRelation";
  public static final String MENTOR_KEY = "mentorKey";
  public static final String MENTEE_KEY = "menteeKey";

  public static final String CHOICE = "choice";
  public static final String FORM_TYPE = "formType";
  public static final String PROFILE_PICTURE = "profilePicture";
  public static final String MENTOR_ID = "mentorID";
  public static final String REQUEST_ID = "requestID";
  public static final String REDIR = "redir";
}
