/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Some useful functions to work with Java's Stream API
 *
 * @author kittl
 * @version 0.1
 * @since 13.04.19
 */
public class StreamUtils {

  private StreamUtils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Usage: Stream.filter(StreamUtils.distinctByKey(node to node.getSubnet[0]))
   *
   * @param key The key to filter for
   * @param <T> Type of the Element to filter for
   * @return A Predicate that is able to do the filtering
   */
  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> key) {
    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
    return t -> seen.putIfAbsent(key.apply(t), Boolean.TRUE) == null;
  }
}
