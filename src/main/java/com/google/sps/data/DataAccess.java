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

package com.google.sps.data;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.users.User;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provides an interface for interacting with the database. This interface can be
 * implemented in many ways to provide mock data or different types of database accessors.
 *
 * @author guptamudit
 * @author tquintanilla
 * @version 1.0
 */
public interface DataAccess {

  /**
   * Generates a Jinja rendering context to use for all pages. This context includes the current
   * page url, the login status, and an instance of the currently logged in UserAccount. The context
   * also includes booleans representing what type of user this is (mentor or mentee)
   *
   * @param currentURL the current url to pass to the rendering context
   * @return the rendering context
   */
  Map<String, Object> getDefaultRenderingContext(String currentURL);

  /**
   * Gets the user that is currently logged in to their Google account This method should use the
   * AppEngine UserService API.
   *
   * @return The user object containing an ID, email, and Auth Domain (gmail.com)
   */
  User getCurrentUser();

  /**
   * Gets a user from the database based on their userID from the authentication API
   *
   * @param userID user ID we want to look for
   * @return The associated UserAccount from the database
   */
  UserAccount getUser(String userID);

  /**
   * Gets a user from the database based on their unique indentifier key within the database
   *
   * @param datastoreKey the unique identifier key
   * @return the UserAccount from the database
   */
  UserAccount getUser(long datastoreKey);

  /**
   * Gets a Mentee user from the database based on their userID from the authentication API This
   * method returns null if the result UserAccount is a Mentor.
   *
   * @param userID user ID we want to look for
   * @return The associated Mentee from the database
   */
  Mentee getMentee(String userID);

  /**
   * Gets a Mentee user from the database based on their unique indentifier key within the database
   * This method returns null if the resultt UserAccount is a Mentor.
   *
   * @param datastoreKey the unique identifier key
   * @return The associated Mentor from the database
   */
  Mentee getMentee(long datastoreKey);

  /**
   * Gets a Mentor user from the database based on their userID from the authentication API This
   * method returns null if the result UserAccount is a Mentee.
   *
   * @param userID user ID we want to look for
   * @return The associated Mentee from the database
   */
  Mentor getMentor(String userID);

  /**
   * Gets a Mentee user from the database based on their unique indentifier key within the database
   * This method returns null if the result UserAccount is a Mentee.
   *
   * @param datastoreKey the unique identifier key
   * @return The associated Mentor from the database
   */
  Mentor getMentor(long datastoreKey);

  /**
   * Creates an entry in the database with the information from the passed in UserAccount object
   * Attempting to create a user who's key is already present in the database will result in a
   * failure (return false).
   *
   * @param user the UserAccount to be placed in the database
   * @return a boolean representing the success of the operation
   */
  boolean createUser(UserAccount user);

  /**
   * Updates an entry in the database with the information from the passed in UserAccount object
   * Attempting to update a user who's key is not yet present in the database will result in a
   * failure (return false).
   *
   * @param user the UserAccount to be updated in the database
   * @return a boolean representing the success of the operation
   */
  boolean updateUser(UserAccount user);

  /**
   * Deletes an entry in the database with the information from the passed in UserAccount object
   * Attempting to delete a user who's key is not yet present in the database will result in a
   * failure (return false). All related MentorshipRequests and MentorMenteeRelations will be
   * deleted.
   *
   * @param user the UserAccount to be deleted from the database
   * @return a boolean representing the success of the operation
   */
  boolean deleteUser(UserAccount user);

  /**
   * Gets a collection of mentors from the database that are similar to the passed in mentee.
   * Invalid mentees (non existent in the database) will result in an empty list being returned.
   *
   * @param mentee the Mentee for whom to find similar Mentors in the database
   * @return a collection of mentors related to the passed in mentee (can be empty, never null)
   */
  Collection<Mentor> getRelatedMentors(Mentee mentee);

  /**
   * Gets a collection of MentorshipRequests from the database that have been sent to the passed in
   * UserAccount. Invalid users (non existent in the database) will result in an empty list being
   * returned.
   *
   * @param user the UserAccount for whom to find incoming MentorshipRequests
   * @return a collection of MentorshipRequests sent to the passed in user (can be empty, never
   *     null)
   */
  Collection<MentorshipRequest> getIncomingRequests(UserAccount user);

  /**
   * Gets a collection of MentorshipRequests from the database that were been sent from the passed
   * in UserAccount. Invalid users (non existent in the database) will result in an empty list being
   * returned.
   *
   * @param user the UserAccount for whom to find outgoing MentorshipRequests
   * @return a collection of MentorshipRequests sent from the passed in user (can be empty, never
   *     null)
   */
  Collection<MentorshipRequest> getOutgoingRequests(UserAccount user);

  /**
   * updates the database to reflect that a certain mentee does not want to work with a certain
   * mentor This method will fail if either the passed in mentee or mentor done exist in the
   * database
   *
   * @param mentee the Mentee that doesn't like the passed in Mentor
   * @param mentor the Mentor that has been disliked (blocklisted) by the passed in Mentee
   * @return a boolean representing the success of the operation
   */
  boolean dislikeMentor(Mentee mentee, Mentor mentor);

  boolean requestMentor(Mentee mentee, Mentor mentor);

  /**
   * retrieves a collection of mentors from the database based on the blacklist of the passed in
   * Mentee
   *
   * @param mentee the Mentee for whom to get the disliked Mentors
   * @return a Collection of the disliked Mentors of the passed in Mentee (can be empty, never
   *     null);
   */
  Collection<Mentor> getDislikedMentors(Mentee mentee);

  /**
   * creates a MentorshipRequest entry in the database based on the passed in MentorshipRequest
   * object The passed in object must have valid keys (can be found in the database) for the sender
   * and receiver
   *
   * @param request the request to write to the database
   * @return a boolean representing the success of the operation
   */
  boolean publishRequest(MentorshipRequest request);

  /**
   * returns a MentorshipRequest based on its unique indentifier database key This method returns
   * null for invalid keys.
   *
   * @param requestKey the unique identifier key
   * @return the MentorshipRequest with the passed in key
   */
  MentorshipRequest getMentorshipRequest(long requestKey);

  /**
   * deletes the MentorshipRequest entry from the database based on the passed in request This
   * deletion will only succeed if there is an existing request in the database with the same
   * information as the passed in request
   *
   * @param request the MentorshipRequest to delete from the database
   * @return a boolean representing the success of the operation
   */
  boolean deleteRequest(MentorshipRequest request);

  /**
   * approves the MentorshipRequest entry from the database based on the passed in request This will
   * first delete the existing request, and afterwards, a MentorMenteeRelation will be added to the
   * database for the associated Mentor and Mentee,
   *
   * @param request the MentorshipRequest to convert to a MentorMenteeRelation
   * @return a boolean representing the success of the operation
   */
  boolean approveRequest(MentorshipRequest request);

  /**
   * deletes a MentorshipRequest from the database based on the passed in request. This method
   * provides a semantically meaning reference to the deleteReference method.
   *
   * @param request the request to be deleted
   * @return a boolean representing the success of the operation
   */
  boolean denyRequest(MentorshipRequest request);

  /**
   * creates a MentorMenteeRelation entry in the database based on the passed in keys for a mentor
   * and mentee This will add a MentorMenteeRelation to the database for the associated Mentor and
   * Mentee,
   *
   * @param mentorKey the uniquely indentifying key for mentor to be connected to the other Mentee
   * @param menteeKey the uniquely indentifying key for mentee to be connected to the other Mentor
   * @return a boolean representing the success of the operation
   */
  boolean makeMentorMenteeRelation(long mentorKey, long menteeKey);

  /**
   * Gets a collection of MentorMenteeRelations from the database that the passed in UserAccount is
   * a part of. Invalid users (non existent in the database) will result in an empty list being
   * returned.
   *
   * @param user the UserAccount for whom to find all MentorMenteeRelations
   * @return a collection of MentorMenteeRelations containing the passed in user (can be empty,
   *     never null)
   */
  Collection<MentorMenteeRelation> getMentorMenteeRelations(UserAccount user);

  /**
   * Deletes the specified MentorMenteeRelation from the database. Invalid relations (non existent
   * in the database) will result in a failure (retrn false).
   *
   * @param relation the MentorMenteeRelation to be removed from the database
   * @return a boolean representing the success of the operation
   */
  boolean deleteMentorMenteeRelation(MentorMenteeRelation relation);

  /**
   * Gets the blobstore uploads that were sent to the passed in request. This method will return a
   * mapping of the initial upload names to the key values as stored in Blobstore.
   *
   * @param request the HTTP Request object for which to find blob uploads
   * @return A Map from String names to the associated Lists of BlobKeys
   */
  Map<String, List<BlobKey>> getBlobUploads(HttpServletRequest request);

  /**
   * This function extracts the blob information from the blostore based on a BlobKey
   *
   * @param blobKey the BlobKey of the blob to be retrieved from blobstore
   * @return the BlobInfo based on the specified key (can be null)
   */
  BlobInfo getBlobInfo(BlobKey blobKey);

  /**
   * Serves a blob from the Blobstore based on the specifed key. This method will write the data
   * stored in the specifed blob to the response object.
   *
   * @param response the HTTP response object to be populated with blob data
   * @param blobKeyString the key for the blobstore data
   */
  void serveBlob(HttpServletResponse response, String blobKeyString) throws IOException;

  /**
   * Deletes a blob from the Blobstore based on the specifed key. This method will remove the blob
   * entry from the database.
   *
   * @param blobKeyString the key for the blobstore data
   */
  void deleteBlob(String blobKeyString);
}
