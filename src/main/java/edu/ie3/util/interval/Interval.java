/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.interval;

import java.io.Serializable;
import java.util.Objects;

/**
 * Concept of an mathematical interval. Lower bound will always be the variable 'lower' ( -&gt;
 * lower = min (l, u)), using the Comparable Interface to determine the minimum of both. Same for
 * upper bound. Bounds must not be null.
 *
 * @author krause, kittl
 * @param <T> Bounding the held information to {@link Comparable} and {@link Serializable}
 */
abstract class Interval<T extends Comparable<? super T> & Serializable> implements Serializable {
  private T lower;
  private T upper;

  /**
   * Set the upper boundary of the interval. It the parameter is null or the upper boundary is
   * smaller than the lower boundary, an Exception is thrown.
   *
   * @param u The desired upper boundary.
   */
  void setUpperBound(T u) {
    if (u == null) throw new NullPointerException("Bound must not be null");
    if (lower == null) {
      lower = u;
      upper = u;
    } else if (lower.compareTo(u) <= 0) {
      upper = u;
    } else {
      throw new IllegalArgumentException("Upper boundary may not be smaller than lower boundary.");
    }
  }

  /**
   * Set the lower boundary of the interval. It the parameter is null or the lower boundary is
   * greater than the upper boundary, an Exception is thrown.
   *
   * @param l The desired lower boundary.
   */
  void setLowerBound(T l) {
    if (l == null) throw new NullPointerException("Bound must not be null");
    if (lower == null) {
      lower = l;
      upper = l;
    } else if (upper.compareTo(l) >= 0) {
      lower = l;
    } else {
      throw new IllegalArgumentException("Lower boundary may not be bigger than upper boundary.");
    }
  }

  /**
   * Tests, if the given value is included in the interval.
   *
   * @param value Value to test for
   * @return True, if it is in the interval, otherwise false.
   */
  abstract boolean includes(T value);

  /**
   * Getting the lower boundary.
   *
   * @return The lower boundary
   */
  public T getLower() {
    return lower;
  }

  /**
   * Getting the upper boundary.
   *
   * @return The upper boundary.
   */
  public T getUpper() {
    return upper;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Interval<?> interval = (Interval<?>) o;
    return lower.equals(interval.lower) && upper.equals(interval.upper);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lower, upper);
  }
}
