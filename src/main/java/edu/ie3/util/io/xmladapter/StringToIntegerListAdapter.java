/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.io.xmladapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This adapter converts a {@link String} of integer values to a {@link List} of {@link Integer}.
 * The string may contain single values or ranges. Example: 1, 2, 3...5, 7-10. Allowed delimiter is
 * , or ; but only one of them
 *
 * @author Kittl
 * @since 05.10.2018
 */
public class StringToIntegerListAdapter extends XmlAdapter<String, List<Integer>> {
  @Override
  public List<Integer> unmarshal(String v) {
    ArrayList<Integer> list = new ArrayList<>();

    // no subnet filter
    if (v == null || v.isEmpty()) return list;

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
    for (String split : splits) {
      boolean dash = split.contains("-");
      boolean ldots = split.contains("...");

      /* This entry is a range */
      if (dash || ldots) {
        String rangeDelimiter = dash ? "-" : "\\.\\.\\.";
        String[] rangeSplits = split.split(rangeDelimiter);
        if (rangeSplits.length > 2)
          throw new IllegalArgumentException(
              "A range may only contain of two Integer values split by either \"-\" or \"...\". Invalid String: "
                  + split);
        Integer start = Integer.parseInt(rangeSplits[0]);
        Integer end = Integer.parseInt(rangeSplits[1]);
        while (start <= end) {
          list.add(start);
          start++;
        }
      } else {
        list.add(Integer.parseInt(split));
      }
    }

    Collections.sort(list);

    return list;
  }

  @Override
  public String marshal(List<Integer> v) {
    return v.stream().map(String::valueOf).collect(Collectors.joining(", "));
  }
}
