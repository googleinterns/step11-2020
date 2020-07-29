package com.google.sps.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.sps.data.Country;
import com.google.sps.data.EducationLevel;
import com.google.sps.data.Ethnicity;
import com.google.sps.data.Gender;
import com.google.sps.data.Language;
import com.google.sps.data.MeetingFrequency;
import com.google.sps.data.Mentee;
import com.google.sps.data.Mentor;
import com.google.sps.data.MentorType;
import com.google.sps.data.TimeZone;
import com.google.sps.data.Topic;
import com.google.sps.data.UserAccount;
import com.google.sps.data.UserType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This class provides static methods for creating random user objects. These functions are useful
 * for generating large amounts of fake data.
 *
 * @author guptamudit
 * @version 1.0
 */
public class RandomObjects {
  private static Random rnd = new Random();
  private static String[] names;

  static {
    try {
      names =
          Resources.toString(
                  RandomObjects.class.getResource(ResourceConstants.DUMMY_DATA_NAMES),
                  Charsets.UTF_8)
              .split(",");
    } catch (IOException e) {
    }
  }

  public static String randomName() {
    return names[rnd.nextInt(names.length)] + " " + names[rnd.nextInt(names.length)];
  }

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

  public static <T extends UserAccount.Builder<T>> T randomUserBuilder(T builder) {
    String name = randomName();
    String email = name.replace(" ", ".").toLowerCase() + "@example.com";
    builder
        .name(name)
        .userID(randomNumberAsString(21))
        .email(email)
        .dateOfBirth(new Date(1300187200000L - ((long) (rnd.nextDouble() * 630700000000L))))
        .country(randomEnum(Country.class))
        .language(randomEnum(Language.class))
        .timezone(randomEnum(TimeZone.class))
        .firstGen(rnd.nextBoolean())
        .lowIncome(rnd.nextBoolean())
        .isFakeUser(true);
    Set<Ethnicity> ethnicities = new HashSet<>();
    ethnicities.add(randomEnum(Ethnicity.class));
    if (ethnicities.contains(Ethnicity.OTHER)) {
      builder.ethnicityOther("unique ethnicity");
    } else {
      for (int i = rnd.nextInt(Ethnicity.values().length - 3); i > 0; i--) {
        Ethnicity newEthnicity = randomEnum(Ethnicity.class);
        if (newEthnicity != Ethnicity.OTHER && newEthnicity != Ethnicity.UNSPECIFIED) {
          ethnicities.add(newEthnicity);
        }
      }
      builder.ethnicityOther("");
    }
    builder.ethnicityList(new ArrayList(ethnicities));
    Gender gender = randomEnum(Gender.class);
    builder.gender(gender);
    if (gender == Gender.OTHER) {
      builder.genderOther("unique gender");
    } else {
      builder.genderOther("");
    }
    EducationLevel educationLevel = randomEnum(EducationLevel.class);
    builder.educationLevel(educationLevel);
    if (educationLevel == EducationLevel.OTHER) {
      builder.educationLevelOther("unique educationLevel");
    } else {
      builder.educationLevelOther("");
    }
    return builder;
  }

  public static Mentee randomMentee() {
    return randomUserBuilder(Mentee.Builder.newBuilder())
        .description(
            "This is a fake bio. I'm not a real mentee, but I would love to get some help from a fake mentor!")
        .userType(UserType.MENTEE)
        .goal(randomEnum(Topic.class))
        .desiredMeetingFrequency(randomEnum(MeetingFrequency.class))
        .dislikedMentorKeys(new HashSet<Long>())
        .desiredMentorType(randomEnum(MentorType.class))
        .build();
  }

  public static Mentor randomMentor() {
    Mentor.Builder builder =
        randomUserBuilder(Mentor.Builder.newBuilder())
            .description(
                "This is a fake bio. I'm not a real mentor, but I would love to help some fake mentees!")
            .userType(UserType.MENTOR)
            .visibility(rnd.nextBoolean())
            .mentorType(randomEnum(MentorType.class));
    Set<Topic> focuses = new HashSet<>();
    for (int i = rnd.nextInt(3); i > 0; i--) {
      focuses.add(randomEnum(Topic.class));
    }
    builder.focusList(new ArrayList(focuses));
    return builder.build();
  }
}
