package com.google.sps.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PublicAccessPage {

  public static final Set<String> publicAccessPage =
      new HashSet<>(Arrays.asList("/about", "/authors", "/landing", "/"));
}
