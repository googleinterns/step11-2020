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
 * This class represents a user's gender. Every UserAccount object stores an instance of this enum.
 *
 * @author guptamudit
 * @version 1.0
 */
public enum Gender {
  UNSPECIFIED(""),
  AGENDER("Agender"),
  ANDROGYNOUS("Androgynous"),
  BIGENDER("Bigender"),
  DEMIGIRL("Demigirl"),
  DEMIGUY("Demiguy"),
  FEMALE("Female"),
  GENDERQUEER("Genderqueer"),
  GENDERFLUID("Genderfluid"),
  INTERSEX("Intersex"),
  MALE("Male"),
  NEUTROIS("Neutrois"),
  NONBINARY("Nonbinary"),
  PANGENDER("Pangender"),
  THIRD_GENDER("Third Gender"),
  WOMAN("Woman"),
  OTHER("Other");

  private String title;

  private Gender(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
