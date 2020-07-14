package com.google.sps.util;

import com.google.sps.data.Country;
import com.google.sps.data.EducationLevel;
import com.google.sps.data.Ethnicity;
import com.google.sps.data.Gender;
import com.google.sps.data.Language;
import com.google.sps.data.MeetingFrequency;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MentorType;
import com.google.sps.data.TimeZoneInfo;
import com.google.sps.data.Topic;
import com.google.sps.data.UserType;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * This class provides static methods for creating random user objects.
 * These functions are useful for generating large amounts of fake data.
 *
 * @author guptamudit
 * @version 1.0
 */
public class RandomObjects {
  private static Random rnd = new Random();

  public static String randomNumberAsString(int digCount) {
    StringBuilder sb = new StringBuilder(digCount);
    for (int i = 0; i < digCount; i++) sb.append((char) ('0' + rnd.nextInt(10)));
    return sb.toString();
  }

  public static String randomLetters(int targetStringLength) {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'

    String generatedString =
        rnd.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

    return generatedString;
  }

  public static <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
    int x = rnd.nextInt(enumClass.getEnumConstants().length);
    return enumClass.getEnumConstants()[x];
  }

  public static Mentee randomMentee() {
    return (new Mentee.Builder())
        .name(randomLetters(10))
        .userID(randomNumberAsString(21))
        .email(randomLetters(7) + "@gmail.com")
        .dateOfBirth(new Date())
        .country(randomEnum(Country.class))
        .language(randomEnum(Language.class))
        .timezone(
            new TimeZoneInfo(TimeZone.getTimeZone(TimeZone.getAvailableIDs()[rnd.nextInt(500)])))
        .ethnicityList(Arrays.asList(randomEnum(Ethnicity.class)))
        .ethnicityOther(randomLetters(10))
        .gender(randomEnum(Gender.class))
        .genderOther(randomLetters(10))
        .firstGen(rnd.nextBoolean())
        .lowIncome(rnd.nextBoolean())
        .educationLevel(randomEnum(EducationLevel.class))
        .educationLevelOther(randomLetters(10))
        .description(randomLetters(30))
        .userType(UserType.MENTEE)
        .goal(randomEnum(Topic.class))
        .desiredMeetingFrequency(randomEnum(MeetingFrequency.class))
        .dislikedMentorKeys(Collections.emptySet())
        .desiredMentorType(randomEnum(MentorType.class))
        .build();
  }

  public static Mentor randomMentor() {
    return (new Mentor.Builder())
        .name(randomLetters(10))
        .userID(randomNumberAsString(21))
        .email(randomLetters(7) + "@gmail.com")
        .dateOfBirth(new Date())
        .country(randomEnum(Country.class))
        .language(randomEnum(Language.class))
        .timezone(
            new TimeZoneInfo(TimeZone.getTimeZone(TimeZone.getAvailableIDs()[rnd.nextInt(500)])))
        .ethnicityList(Arrays.asList(randomEnum(Ethnicity.class)))
        .ethnicityOther(randomLetters(10))
        .gender(randomEnum(Gender.class))
        .genderOther(randomLetters(10))
        .firstGen(rnd.nextBoolean())
        .lowIncome(rnd.nextBoolean())
        .educationLevel(randomEnum(EducationLevel.class))
        .educationLevelOther(randomLetters(10))
        .description(randomLetters(30))
        .userType(UserType.MENTOR)
        .visibility(rnd.nextBoolean())
        .focusList(Arrays.asList())
        .mentorType(randomEnum(MentorType.class))
        .build();
  }
}
