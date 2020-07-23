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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents a summary of a user's timezone. Every UserAccount stores an instance of
 * this class.
 *
 * @author tquintanilla
 * @version 1.0
 */
public enum TimeZone {
  HST("Hawaii Standard Time", -10),
  AKST("Alaska Standard Time", -9),
  PST("Pacific Standard Time", -8),
  MST("Mountain Standard Time", -7),
  CST("Central Standard Time", -6),
  EST("Eastern Standard Time", -5),
  AST("Atlantic Standard Time", -4),
  CNT("Newfoundland Standard Time", -3.5),
  AGT("Argentine Time", -3),
  BET("Brasilia Time", -3),
  GMT("Greenwich Mean Time", 0),
  UTC("Coordinated Universal Time", 0),
  WET("Western European Time", 0),
  CET("Central European Time", 1),
  MET("Middle Europe Time", 1),
  ECT("Central European Time", 1),
  EET("Eastern European Time", 2),
  CAT("Central African Time", 2),
  MSK("Moscow Standard Time", 3),
  EAT("Eastern African Time", 3),
  NET("Armenia Time", 4),
  PLT("Pakistan Time", 5),
  IST("India Standard Time", 5.5),
  BST("Bangladesh Time", 6),
  VST("Indochina Time", 7),
  CTT("China Standard Time", 8),
  ROK("Korean Standard Time", 9),
  JST("Japan Standard Time", 9),
  ACT("Australian Central Standard Time (Northern Territory)", 9.5),
  AET("Australian Eastern Standard Time (New South Wales)", 10),
  SST("Solomon Is. Time", 11),
  NZ("New Zealand Standard Time", 12),
  CHAT("Chatham Standard Time", 12.75),
  WST("West Samoa Standard Time", 13);

  String name;
  double offset;

  private TimeZone(String name, double offset) {
    this.name = name;
    this.offset = offset;
  }

  public String getName() {
    return this.name;
  }

  public double getOffset() {
    return this.offset;
  }

  public static Collection<TimeZone> valuesSorted() {
    List<TimeZone> values = Arrays.asList(TimeZone.values());
    values.sort(Comparator.comparing(TimeZone::getOffset));
    return values;
  }
}
