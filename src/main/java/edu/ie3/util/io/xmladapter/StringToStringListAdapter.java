/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.io.xmladapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This adapter converts a {@link String} of integer values to a {@link List} of {@link Integer}.
 * The string may contain only single values. Example: 1, 2, 3. Allowed delimiter is , or ; but only
 * one of them
 *
 * @author Kittl
 * @since 05.10.2018
 */
public class StringToStringListAdapter extends XmlAdapter<String, List<String>> {
  @Override
  public List<String> unmarshal(String v) {

    /* Remove whitespaces and other invisible input */
    String input = v.replaceAll("\\s", "");
    boolean comma = input.contains(",");
    boolean semicolon = input.contains(";");
    if (comma && semicolon)
      throw new IllegalArgumentException(
          "The input string may contain \",\" or \";\" as delimiter, but only one of both. Invalid String: "
              + v);
    String delimiter = comma ? "," : ";";

    String[] splits = input.split(delimiter);

    return new ArrayList<>(Arrays.asList(splits));
  }

  @Override
  public String marshal(List<String> v) {
    return String.join(", ", v);
  }
}
