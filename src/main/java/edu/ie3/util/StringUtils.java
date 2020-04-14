/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util;

import java.util.Arrays;

/** Some useful functions to manipulate Strings */
public class StringUtils {
  private StringUtils() {
    throw new IllegalStateException("Utility classes cannot be instantiated.");
  }

  /**
   * Converts a given camel case string to its snake case representation
   *
   * @param camelCaseString the camel case string
   * @return the resulting snake case representation
   */
  public static String camelCaseToSnakeCase(String camelCaseString) {
    String regularCamelCaseRegex = "([a-z0-9])([A-Z0-9]+)";
    String specialCamelCaseRegex = "((?<!_)[A-Z]?)((?<!^)[A-Z]+)";
    String snakeCaseReplacement = "$1_$2";
    return camelCaseString
        .replaceAll(regularCamelCaseRegex, snakeCaseReplacement)
        .replaceAll(specialCamelCaseRegex, snakeCaseReplacement)
        .toLowerCase();
  }

  /**
   * Converts a given snake case string to its lower camel case representation
   *
   * @param snakeCaseString the camel case string
   * @return the resulting lower camel case representation
   */
  public static String snakeCaseToCamelCase(String snakeCaseString) {
    StringBuilder sb = new StringBuilder(snakeCaseString);
    for (int i = 0; i < sb.length(); i++) {
      if (sb.charAt(i) == '_') {
        sb.deleteCharAt(i);
        sb.replace(i, i + 1, String.valueOf(Character.toUpperCase(sb.charAt(i))));
      }
    }
    return sb.toString();
  }

  /**
   * Converts a given snake case string to its upper camel case (= pascal case) representation
   *
   * @param snakeCaseString the camel case string
   * @return the resulting upper camel case (= pascal case)
   */
  public static String snakeCaseToPascalCase(String snakeCaseString) {
    StringBuilder sb = new StringBuilder();
    for (String s : snakeCaseString.split("_")) {
      sb.append(Character.toUpperCase(s.charAt(0)));
      if (s.length() > 1) {
        sb.append(s.substring(1).toLowerCase());
      }
    }
    return sb.toString();
  }

  /**
   * Converts an Array of camel case strings to its snake case representations
   *
   * @param input Array of Strings to convert
   * @return Array of converted Strings
   */
  public static String[] camelCaseToSnakeCase(String[] input) {
    return Arrays.stream(input).map(StringUtils::camelCaseToSnakeCase).toArray(String[]::new);
  }

  /**
   * Adds quotation marks at the beginning and end of the input, if they are not apparent, yet.
   *
   * @param input String to quote
   * @return Quoted String
   */
  public static String quote(String input) {
    return input.replaceAll("^([^\"])", "\"$1").replaceAll("([^\"])$", "$1\"");
  }

  /**
   * Quotes all entries of the Array
   *
   * @param input Array of Strings to quote
   * @return Array of quoted Strings
   */
  public static String[] quote(String[] input) {
    return Arrays.stream(input).map(StringUtils::quote).toArray(String[]::new);
  }

  /**
   * Replaces all non word-characters with an underscore
   *
   * @param input String to clean
   * @return the cleaned string
   */
  public static String cleanString(String input) {
    return input.replaceAll("[^\\w]", "_");
  }
}
