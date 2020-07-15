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

import java.util.ArrayList;
import java.util.Collection;
import java.util.TimeZone;

/**
 * This class represents a summary of a java.util.TimeZone object. It's useful for minimizing the
 * stored data in the fake data file. Every UserAccount stores an instance of this class.
 *
 * @author tquintanilla
 * @version 1.0
 */
public class TimeZoneInfo {

  private static final double MILLISECONDS_PER_HOURS = 3600000.0;

  String id;
  String name;
  double offset;

  public TimeZoneInfo(TimeZone timeZone) {
    this.id = timeZone.getID();
    this.name = timeZone.getDisplayName();
    this.offset = timeZone.getRawOffset() / MILLISECONDS_PER_HOURS;
  }

  public String getID() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public double getOffset() {
    return this.offset;
  }

  public static Collection<TimeZoneInfo> getListOfNamesToDisplay(Collection<TimeZone> timeZones) {
    Collection<TimeZoneInfo> infoList = new ArrayList<>();
    for (TimeZone timeZone : timeZones) {
      infoList.add(new TimeZoneInfo(timeZone));
    }
    return infoList;
  }
}
