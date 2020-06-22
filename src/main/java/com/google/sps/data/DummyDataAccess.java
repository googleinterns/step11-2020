package com.google.sps.data;

import java.util.ArrayList;
import java.util.Collection;

public class DummyDataAccess {
  public static Collection<Mentor> getRelatedMentors() {
    Collection<Mentor> relatedMentors = new ArrayList<>(5);
    relatedMentors.add(
        new Mentor("Alice", "12345", "03/17/2001", "technology", "Master's in Computer Science"));
    relatedMentors.add(
        new Mentor("Bob", "23456", "05/23/1985", "medicine", "Master's in Biomedical Engineering"));
    relatedMentors.add(
        new Mentor("Charlie", "34567", "07/27/1960", "mental health", "PhD is Psychology"));
    relatedMentors.add(
        new Mentor("Dave", "45678", "11/08/1992", "arts and crafts", "Bachelor of Arts"));
    relatedMentors.add(
        new Mentor("Edward", "56789", "09/31/1972", "personal branding", "Highschool Graduate"));
    return relatedMentors;
  }
}
