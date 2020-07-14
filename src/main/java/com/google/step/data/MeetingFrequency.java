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

package com.google.step.data;

public enum MeetingFrequency {
  DAILY("Daily"),
  TWICE_WEEKLY("Twice a week"),
  WEEKLY("Weekly"),
  TWICE_MONTHLY("Twice a month"),
  MONTHLY("Monthly"),
  WHEN_NEEDED("When needed");

  private String title;

  private MeetingFrequency(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
