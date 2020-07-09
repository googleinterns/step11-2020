package com.google.sps.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Stores a list of pages that are available to non-logged in users.
 */
public class PublicAccessPage {

  public static final Set<String> publicAccessPage =
      new HashSet<>(Arrays.asList("/about", "/authors", "/landing", "/"));
}
