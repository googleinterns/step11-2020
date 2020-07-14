package com.google.step.util;

import com.google.step.data.Country;
import com.google.step.data.EducationLevel;
import com.google.step.data.Ethnicity;
import com.google.step.data.Gender;
import com.google.step.data.Language;
import com.google.step.data.MeetingFrequency;
import com.google.step.data.Mentee;
import com.google.step.data.Mentor;
import com.google.step.data.MentorType;
import com.google.step.data.TimeZoneInfo;
import com.google.step.data.Topic;
import com.google.step.data.UserType;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

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
