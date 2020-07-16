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

package com.google.sps.data;

import com.google.appengine.api.users.User;
import java.util.Collection;
import java.util.Map;

/**
 * This class provides an interface for interacting with the database. This interface can be
 * implemented in many ways to provide mock data or different types of database accessors.
 *
 * @author guptamudit
 * @author tquintanilla
 * @version 1.0
 */
public interface DataAccess {

  Map<String, Object> getDefaultRenderingContext(String currentURL);

  User getCurrentUser();

  UserAccount getUser(String userID);

  UserAccount getUser(long datastoreKey);

  Mentee getMentee(String userID);

  Mentee getMentee(long datastoreKey);

  Mentor getMentor(String userID);

  Mentor getMentor(long datastoreKey);

  boolean createUser(UserAccount user);

  boolean updateUser(UserAccount user);

  Collection<Mentor> getRelatedMentors(Mentee mentee);

  Collection<Mentee> getRelatedMentees(Mentor mentor);

  Collection<MentorshipRequest> getIncomingRequests(UserAccount user);

  Collection<MentorshipRequest> getOutgoingRequests(UserAccount user);

  boolean dislikeMentor(Mentee mentee, Mentor mentor);

  Collection<Mentor> getDislikedMentors(Mentee mentee);

  boolean publishRequest(MentorshipRequest request);

  MentorshipRequest getMentorshipRequest(long requestKey);

  boolean deleteRequest(MentorshipRequest request);

  // delete request object and create mentorMenteeRelation object
  boolean approveRequest(MentorshipRequest request);

  // delete request object
  boolean denyRequest(MentorshipRequest request);

  boolean makeMentorMenteeRelation(long mentorKey, long menteeKey);

  Collection<MentorMenteeRelation> getMentorMenteeRelations(UserAccount user);
}
