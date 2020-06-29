package com.google.sps.util;

public class ErrorMessages {
  public static final String TEMPLATES_DIRECTORY_NOT_FOUND =
      "The templates directory was not found";
  public static final String TEMPLATE_FILE_NOT_FOUND = "The template was not found: ";

  public static String templateFileNotFound(String templateURL) {
    return TEMPLATE_FILE_NOT_FOUND + templateURL;
  }
}
