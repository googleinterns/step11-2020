package com.google.sps.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TimeZone;

public class TimeZoneInfo {

  private static final double MILLISECONDS_TO_HOURS = 3600000.0;

  String name;
  double offset;

  private TimeZoneInfo(TimeZone timeZone) {
    this.name = timeZone.getID();
    this.offset = timeZone.getRawOffset() / MILLISECONDS_TO_HOURS;
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
