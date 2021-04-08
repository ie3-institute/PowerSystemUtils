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
    return input.matches("^\".*\"$") ? input : "\"" + input + "\"";
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

  /**
   * Quotes a given string that contains special characters to comply with the csv specification RFC
   * 4180 (https://tools.ietf.org/html/rfc4180). Double quotes are escaped according to
   * specification.
   *
   * @param inputString string that should be converted to a valid rfc 4180 string
   * @param csvSep separator of the csv file
   * @return a csv string that is valid according to rfc 4180
   */
  public static String csvString(String inputString, String csvSep) {
    if (needsCsvRFC4180Quote(inputString, csvSep)) {
      /* Get rid of first and last quotation if there is some. */
      String inputUnquoted = unquoteStartEnd(inputString);
      /* Escape every double quotation mark within the String by doubling it */
      String withEscapedQuotes = inputUnquoted.replace("\"", "\"\"");
      /* finally add quotes to the strings start and end again */
      return quote(withEscapedQuotes);
    } else return inputString;
  }

  /**
   * Removes double quotes at start and end position of the provided string, if any
   *
   * @param input string that should be unquoted
   * @return copy of the provided string without start and end double quotes
   */
  public static String unquoteStartEnd(String input) {
    return input.matches("^\".*\"$") ? input.substring(1, input.length() - 1) : input;
  }

  /**
   * Check if the provided string needs to be quoted according to the csv specification RFC 4180
   *
   * @param inputString the string that should be checked
   * @param csvSep separator of the csv file
   * @return true of the string needs to be quoted, false otherwise
   */
  private static boolean needsCsvRFC4180Quote(String inputString, String csvSep) {
    return inputString.contains(csvSep)
        || inputString.contains(",")
        || inputString.contains("\"")
        || inputString.contains("\n");
  }

  /**
   * Capitalizes a given String.
   *
   * @param inputString The String that is given.
   * @return The String, that is taken
   */
  public static String capitalize(String inputString) {
    if (inputString.isEmpty()) return inputString;
    else return inputString.substring(0, 1).toUpperCase() + inputString.substring(1);
  }
}
