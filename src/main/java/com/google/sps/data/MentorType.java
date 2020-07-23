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

/**
 * This enum represents a mentors's style of mentorship. Every Mentor object stores an instance of
 * this enum.
 *
 * @author tquintanilla
 * @version 1.0
 */
public enum MentorType {
  TUTOR("Tutor"),
  COLLEGE("College Advisor"),
  CAREER("Career Advisor");

  private String title;

  private MentorType(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
