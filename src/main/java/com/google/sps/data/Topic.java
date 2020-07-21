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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * This enum represents a potential topic of study for a mentee to want/mentor to provide help with.
 * Every Mentee object stores a single instance of this enum while Mentor objects store a collection
 * of Topics.
 *
 * @author tquintanilla
 * @version 1.0
 */
public enum Topic {
  COMPUTER_SCIENCE("Computer Science"),
  PHYSICS("Physics"),
  ART("Art"),
  MEDICINE("Medicine"),
  MUSIC("Music"),
  MATH("Math"),
  LANGUAGE("Language"),
  BIOLOGY("Biology"),
  OTHER("Other");

  private String title;

  private Topic(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public static Collection<Topic> valuesSorted() {
    List<Topic> values = Arrays.asList(Topic.values());
    values.sort(Comparator.comparing(Topic::getTitle));
    return values;
  }
}
