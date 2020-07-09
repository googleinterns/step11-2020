package com.google.sps.util;

/** List of common error messages
 */
public final class ErrorMessages {
  public static final String TEMPLATES_DIRECTORY_NOT_FOUND =
      "The templates directory was not found";
  public static final String TEMPLATE_FILE_NOT_FOUND = "The template was not found: ";
  public static final String INVALID_PARAMATERS = "insufficient or invalid parameters";
  public static final String BAD_REDIRECT = "Invalid encoded redirection pathname";
  public static final String BAD_DATE_PARSE = "Invalid date format";

  public static String templateFileNotFound(String templateURL) {
    return TEMPLATE_FILE_NOT_FOUND + templateURL;
  }

  public static String badRedirect(String encodedURL) {
    return BAD_REDIRECT + encodedURL;
  }
}
