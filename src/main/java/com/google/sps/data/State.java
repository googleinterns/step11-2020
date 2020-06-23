package com.google.sps.data;

public class State {
  String abbreviation;
  String name;

  public State(String name, String abbreviation) {
    this.name = name;
    this.abbreviation = abbreviation;
  }

  public String getAbbreviation() {
    return this.abbreviation;
  }

  public String getName() {
    return this.name;
  }
}
