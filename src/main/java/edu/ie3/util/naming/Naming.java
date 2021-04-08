/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.naming;

import edu.ie3.util.StringUtils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Naming {
  private static final String delimiterRegex = "[_\\-|]";

  /**
   * Builds a naming from a collection of single words. We assume, that all words are yet
   * de-composed. This means, they only comply with {@link NamingConvention#Flat}. Components, that
   * contain one of the known delimiters (_, -, |) are neglected.
   *
   * @param singleWords The single words, which shall compose the naming later
   * @return Proper naming object
   */
  public static Naming from(String... singleWords) {
    /* Go through all single words, make them lower case and filter out those, that contain one of known delimiters */
    LinkedList<String> components =
        Arrays.stream(singleWords)
            .map(String::toLowerCase)
            .filter(entry -> !entry.matches(".*" + delimiterRegex + ".*"))
            .collect(Collectors.toCollection(LinkedList::new));

    String flatCase = String.join("", components);
    String upperFlatCase =
        components.stream().map(String::toUpperCase).collect(Collectors.joining(""));
    String camelCase = String.join("", camelCasing(components));
    String pascalCase =
        components.stream().map(StringUtils::capitalize).collect(Collectors.joining(""));
    String snakeCase = String.join("_", components);
    String screamingSnakeCase =
        components.stream().map(String::toUpperCase).collect(Collectors.joining("_"));
    String camelSnakeCase = String.join("_", camelCasing(components));
    String pascalSnakeCase =
        components.stream().map(StringUtils::capitalize).collect(Collectors.joining("_"));
    String kebabCase = String.join("-", components);
    String donerCase = String.join("|", components);
    String screamingKebabCase =
        components.stream().map(String::toUpperCase).collect(Collectors.joining("-"));
    String trainCase =
        components.stream().map(StringUtils::capitalize).collect(Collectors.joining("-"));

    return new Naming(
        flatCase,
        upperFlatCase,
        camelCase,
        pascalCase,
        snakeCase,
        screamingSnakeCase,
        camelSnakeCase,
        pascalSnakeCase,
        kebabCase,
        donerCase,
        screamingKebabCase,
        trainCase);
  }

  /**
   * Adjust the case of the first letter of each component to comply with the rules of camel casing:
   *
   * <ul>
   *   <li>The first letter is always small case
   *   <li>A letter, that follows a digit is always small case
   *   <li>A letter, that follows a single letter component is always small case
   *   <li>All other first letters are upper case
   * </ul>
   *
   * @param components The small case components
   * @return A {@link LinkedList} with properly capitalized components
   */
  private static LinkedList<String> camelCasing(LinkedList<String> components) {
    LinkedList<String> output = new LinkedList<>();

    /* The first letter is always not capitalized */
    boolean blockCapitalization = true;
    for (String component : components) {
      output.add(blockCapitalization ? component : StringUtils.capitalize(component));
      /* The next component is not capitalized, if the last one ends with a digit or is only one character */
      blockCapitalization = component.matches("\\d$") || component.length() == 1;
    }
    return output;
  }

  private final String flatCase;
  private final String upperFlatCase;
  private final String camelCase;
  private final String pascalCase;
  private final String snakeCase;
  private final String screamingSnakeCase;
  private final String camelSnakeCase;
  private final String pascalSnakeCase;
  private final String kebabCase;
  private final String donerCase;
  private final String screamingKebabCase;
  private final String trainCase;

  private Naming(
      String flatCase,
      String upperFlatCase,
      String camelCase,
      String pascalCase,
      String snakeCase,
      String screamingSnakeCase,
      String camelSnakeCase,
      String pascalSnakeCase,
      String kebabCase,
      String donerCase,
      String screamingKebabCase,
      String trainCase) {
    this.flatCase = flatCase;
    this.upperFlatCase = upperFlatCase;
    this.camelCase = camelCase;
    this.pascalCase = pascalCase;
    this.snakeCase = snakeCase;
    this.screamingSnakeCase = screamingSnakeCase;
    this.camelSnakeCase = camelSnakeCase;
    this.pascalSnakeCase = pascalSnakeCase;
    this.kebabCase = kebabCase;
    this.donerCase = donerCase;
    this.screamingKebabCase = screamingKebabCase;
    this.trainCase = trainCase;
  }

  public String flatCase() {
    return flatCase;
  }

  public String upperFlatCase() {
    return upperFlatCase;
  }

  public String camelCase() {
    return camelCase;
  }

  public String pascalCase() {
    return pascalCase;
  }

  public String snakeCase() {
    return snakeCase;
  }

  public String screamingSnakeCase() {
    return screamingSnakeCase;
  }

  public String camelSnakeCase() {
    return camelSnakeCase;
  }

  public String pascalSnakeCase() {
    return pascalSnakeCase;
  }

  public String kebabCase() {
    return kebabCase;
  }

  public String donerCase() {
    return donerCase;
  }

  public String screamingKebabCase() {
    return screamingKebabCase;
  }

  public String trainCase() {
    return trainCase;
  }

  /**
   * Get the naming as the given convention.
   *
   * @param convention The desired convention
   * @return The naming
   */
  public String as(NamingConvention convention) {
    switch (convention) {
      case Flat:
        return flatCase();
      case Camel:
        return camelCase();
      case Doner:
        return donerCase();
      case Kebab:
        return kebabCase();
      case Snake:
        return snakeCase();
      case Train:
        return trainCase();
      case Pascal:
        return pascalCase();
      case UpperFlat:
        return upperFlatCase();
      case CamelSnake:
        return camelSnakeCase();
      case PascalSnake:
        return pascalSnakeCase();
      case ScreamingKebab:
        return screamingKebabCase();
      case ScreamingSnake:
        return screamingSnakeCase();
      default:
        throw new IllegalArgumentException(
            "The naming convention '"
                + convention
                + "', you requested, is currently not supported.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Naming)) return false;
    Naming naming = (Naming) o;
    return flatCase.equals(naming.flatCase)
        && upperFlatCase.equals(naming.upperFlatCase)
        && camelCase.equals(naming.camelCase)
        && pascalCase.equals(naming.pascalCase)
        && snakeCase.equals(naming.snakeCase)
        && screamingSnakeCase.equals(naming.screamingSnakeCase)
        && camelSnakeCase.equals(naming.camelSnakeCase)
        && pascalSnakeCase.equals(naming.pascalSnakeCase)
        && kebabCase.equals(naming.kebabCase)
        && donerCase.equals(naming.donerCase)
        && screamingKebabCase.equals(naming.screamingKebabCase)
        && trainCase.equals(naming.trainCase);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        flatCase,
        upperFlatCase,
        camelCase,
        pascalCase,
        snakeCase,
        screamingSnakeCase,
        camelSnakeCase,
        pascalSnakeCase,
        kebabCase,
        donerCase,
        screamingKebabCase,
        trainCase);
  }

  @Override
  public String toString() {
    return "Naming{" + "camelCase='" + camelCase + '\'' + '}';
  }
}
