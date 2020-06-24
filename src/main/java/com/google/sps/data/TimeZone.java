package com.google.sps.data;

public enum TimeZone {
  DST("Dateline Standard Time", -12, "(UTC-12:00) International Date Line West"),
  HST("Hawaiian Standard Time", -10, "(UTC-10:00) Hawaii"),
  AKDT("Alaskan Standard Time", -8, "(UTC-09:00) Alaska"),
  PDT("Pacific Daylight Time", -7, "(UTC-07:00) Pacific Time (US & Canada)"),
  PST("Pacific Standard Time", -8, "(UTC-08:00) Pacific Time (US & Canada)"),
  UMST("US Mountain Standard Time", -7, "(UTC-07:00) Arizona"),
  MDT("Mountain Standard Time", -6, "(UTC-07:00) Mountain Time (US & Canada)"),
  CDT("Central Standard Time", -5, "(UTC-06:00) Central Time (US & Canada)"),
  CCST("Canada Central Standard Time", -6, "(UTC-06:00) Saskatchewan"),
  SPST("SA Pacific Standard Time", -5, "(UTC-05:00) Bogota, Lima, Quito"),
  EDT("Eastern Standard Time", -4, "(UTC-05:00) Eastern Time (US & Canada)"),
  UEDT("US Eastern Standard Time", -4, "(UTC-05:00) Indiana (East)"),
  VST("Venezuela Standard Time", -4.5, "(UTC-04:30) Caracas"),
  PYT("Paraguay Standard Time", -4, "(UTC-04:00) Asuncion"),
  ADT("Atlantic Standard Time", -3, "(UTC-04:00) Atlantic Time (Canada)"),
  CBST("Central Brazilian Standard Time", -4, "(UTC-04:00) Cuiaba"),
  SWST("SA Western Standard Time", -4, "(UTC-04:00) Georgetown, La Paz, Manaus, San Juan"),
  PSST("Pacific SA Standard Time", -4, "(UTC-04:00) Santiago"),
  NDT("Newfoundland Standard Time", -2.5, "(UTC-03:30) Newfoundland"),
  ESAST("E. South America Standard Time", -3, "(UTC-03:00) Brasilia"),
  AST("Argentina Standard Time", -3, "(UTC-03:00) Buenos Aires"),
  SEST("SA Eastern Standard Time", -3, "(UTC-03:00) Cayenne, Fortaleza"),
  GDT("Greenland Standard Time", -3, "(UTC-03:00) Greenland"),
  MST("Montevideo Standard Time", -3, "(UTC-03:00) Montevideo"),
  CVST("Cape Verde Standard Time", -1, "(UTC-01:00) Cape Verde Is."),
  UTC("UTC", 0, "(UTC) Coordinated Universal Time"),
  GMT("GMT Standard Time", 0, "(UTC) Edinburgh, London"),
  BST("British Summer Time", 1, "(UTC+01:00) Edinburgh, London"),
  GST("Greenwich Standard Time", 0, "(UTC) Monrovia, Reykjavik"),
  WEDT(
      "W. Europe Standard Time", 2, "(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna"),
  CEDT(
      "Central Europe Standard Time",
      2,
      "(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague"),
  RDT("Romance Standard Time", 2, "(UTC+01:00) Brussels, Copenhagen, Madrid, Paris"),
  WCAST("W. Central Africa Standard Time", 1, "(UTC+01:00) West Central Africa"),
  NST("Namibia Standard Time", 1, "(UTC+01:00) Windhoek"),
  MEDT("Middle East Standard Time", 3, "(UTC+02:00) Beirut"),
  EST("Egypt Standard Time", 2, "(UTC+02:00) Cairo"),
  SDT("Syria Standard Time", 3, "(UTC+02:00) Damascus"),
  EEDT("E. Europe Standard Time", 3, "(UTC+02:00) E. Europe"),
  FDT("FLE Standard Time", 3, "(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius"),
  TDT("Turkey Standard Time", 3, "(UTC+03:00) Istanbul"),
  JDT("Israel Standard Time", 3, "(UTC+02:00) Jerusalem"),
  LST("Libya Standard Time", 2, "(UTC+02:00) Tripoli"),
  KST("Kaliningrad Standard Time", 3, "(UTC+02:00) Kaliningrad"),
  EAST("E. Africa Standard Time", 3, "(UTC+03:00) Nairobi"),
  MSK("Moscow Standard Time", 3, "(UTC+03:00) Moscow, St. Petersburg, Volgograd, Minsk"),
  SAMT("Samara Time", 4, "(UTC+04:00) Samara, Ulyanovsk, Saratov"),
  IDT("Iran Standard Time", 4.5, "(UTC+03:30) Tehran"),
  GET("Georgian Standard Time", 4, "(UTC+04:00) Tbilisi"),
  WAST("West Asia Standard Time", 5, "(UTC+05:00) Ashgabat, Tashkent"),
  YEKT("Yekaterinburg Time", 5, "(UTC+05:00) Yekaterinburg"),
  PKT("Pakistan Standard Time", 5, "(UTC+05:00) Islamabad, Karachi"),
  IST("India Standard Time", 5.5, "(UTC+05:30) Chennai, Kolkata, Mumbai, New Delhi"),
  SLST("Sri Lanka Standard Time", 5.5, "(UTC+05:30) Sri Jayawardenepura"),
  CAST("Central Asia Standard Time", 6, "(UTC+06:00) Nur-Sultan (Astana)"),
  SAST("SE Asia Standard Time", 7, "(UTC+07:00) Bangkok, Hanoi, Jakarta"),
  NCAST("N. Central Asia Standard Time", 7, "(UTC+07:00) Novosibirsk"),
  CST("China Standard Time", 8, "(UTC+08:00) Beijing, Chongqing, Hong Kong, Urumqi"),
  NAST("North Asia Standard Time", 8, "(UTC+08:00) Krasnoyarsk"),
  MPST("Singapore Standard Time", 8, "(UTC+08:00) Kuala Lumpur, Singapore"),
  UST("Ulaanbaatar Standard Time", 8, "(UTC+08:00) Ulaanbaatar"),
  NAEST("North Asia East Standard Time", 8, "(UTC+08:00) Irkutsk"),
  JST("Japan Standard Time", 9, "(UTC+09:00) Osaka, Sapporo, Tokyo"),
  ACST("AUS Central Standard Time", 9.5, "(UTC+09:30) Darwin"),
  AEST("AUS Eastern Standard Time", 10, "(UTC+10:00) Canberra, Melbourne, Sydney"),
  WPST("West Pacific Standard Time", 10, "(UTC+10:00) Guam, Port Moresby"),
  YST("Yakutsk Standard Time", 9, "(UTC+09:00) Yakutsk"),
  CPST("Central Pacific Standard Time", 11, "(UTC+11:00) Solomon Is., New Caledonia"),
  NZST("New Zealand Standard Time", 12, "(UTC+12:00) Auckland, Wellington"),
  U("UTC+12", 12, "(UTC+12:00) Coordinated Universal Time+12"),
  FST("Fiji Standard Time", 12, "(UTC+12:00) Fiji"),
  KDT("Kamchatka Standard Time", 13, "(UTC+12:00) Petropavlovsk-Kamchatsky - Old"),
  TST("Tonga Standard Time", 13, "(UTC+13:00) Nuku'alofa"),
  SST("Samoa Standard Time", 13, "(UTC+13:00) Samoa");

  private String title;
  private double utcOffset;
  private String longText;

  private TimeZone(String title, double utcOffset, String longText) {
    this.title = title;
    this.utcOffset = utcOffset;
    this.longText = longText;
  }

  public String getTitle() {
    return title;
  }

  public double getUtcOffset() {
    return utcOffset;
  }

  public String getLongText() {
    return longText;
  }
}
