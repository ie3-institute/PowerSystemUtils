/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities;

import javax.measure.Quantity;
import tec.uom.se.ComparableQuantity;
import tec.uom.se.quantity.Quantities;

/** Offers useful methods to handle {@link Quantity}s */
public class QuantityUtil {
  private QuantityUtil() {
    throw new IllegalStateException("Utility classes cannot be instantiated.");
  }

  /**
   * Converts a given quantity to a comparable quantity
   *
   * @param quantity Input quantity
   * @param <Q> Type of input quantity
   * @return The same value and unit, but as a {@link ComparableQuantity}
   */
  public static <Q extends Quantity<Q>> ComparableQuantity<Q> makeComparable(Quantity<Q> quantity) {
    return Quantities.getQuantity(quantity.getValue(), quantity.getUnit());
  }
}
