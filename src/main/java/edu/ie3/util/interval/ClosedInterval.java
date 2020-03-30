/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.interval;

import java.io.Serializable;

/**
 * Closed interval.
 *
 * @author krause, kittl
 */
public class ClosedInterval<T extends Comparable<? super T> & Serializable> extends Interval<T> {
  private static final long serialVersionUID = -5287853375331545052L;

  public ClosedInterval(T l, T u) {
    setLowerBound(l);
    setUpperBound(u);
  }

  /**
   * Checks if value is included in the interval, treating it as an closed interval
   *
   * @param value Value to be checked
   * @return true, if the values lays inside of the interval
   */
  public boolean includes(T value) {
    return value.compareTo(getLower()) >= 0 && value.compareTo(getUpper()) <= 0;
  }

  @Override
  public String toString() {
    return "Interval [" + getLower() + ", " + getUpper() + "]";
  }
}
