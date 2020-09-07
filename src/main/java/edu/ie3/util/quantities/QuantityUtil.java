/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities;

import edu.ie3.util.quantities.dep.PowerSystemUnits;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.UnconvertibleException;
import javax.measure.quantity.Angle;

import static edu.ie3.util.quantities.PowerSystemUnits.DEGREE_GEOM;
import static java.lang.StrictMath.abs;

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
   * @return The same value and unit, but as a {@link tec.uom.se.ComparableQuantity}
   * @deprecated Use {@link this#asComparable(Quantity)} instead. Will be removed with version 1.4
   */
  @Deprecated
  public static <Q extends Quantity<Q>> tec.uom.se.ComparableQuantity<Q> makeComparable(
      Quantity<Q> quantity) {
    return tec.uom.se.quantity.Quantities.getQuantity(quantity.getValue(), quantity.getUnit());
  }

  /**
   * Converts a given quantity to a comparable quantity
   *
   * @param quantity Input quantity
   * @param <Q> Type of input quantity
   * @return The same value and unit, but as a {@link ComparableQuantity}
   */
  public static <Q extends Quantity<Q>> ComparableQuantity<Q> asComparable(Quantity<Q> quantity) {
    return Quantities.getQuantity(quantity.getValue(), quantity.getUnit());
  }

  /**
   * Compares two {@link Quantity}s, if they are considerably equal. This is foremost important for
   * {@link tec.uom.se.quantity.DoubleQuantity}s. The comparison is made on the absolute difference
   * of both quantities' value. Both quantities are converted into a's unit before the comparison.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param absQuantityTolerance Permissible absolute tolerance
   * @param <Q> Type of Quantity
   * @return true, if both quantities' values differ less then the given tolerance else false
   */
  public static <Q extends Quantity<Q>> boolean considerablyAbsEqual(
      Quantity<Q> a, Quantity<Q> b, double absQuantityTolerance) {

    double aVal = a.getValue().doubleValue();
    double bVal = b.to(a.getUnit()).getValue().doubleValue();

    return Math.abs(aVal - bVal) <= absQuantityTolerance;
  }

  /**
   * Compares two {@link Quantity}s, if they are considerably equal. This is foremost important for
   * {@link tec.uom.se.quantity.DoubleQuantity}s. The comparison is made on the relative difference
   * of both quantities' value with regard to a's value. Both quantities are converted into a's unit
   * before the comparison.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param relQuantityTolerance Permissible relative tolerance
   * @param <Q> Type of Quantity
   * @return true, if both quantities' values differ less then the given tolerance else false
   */
  public static <Q extends Quantity<Q>> boolean considerablyRelEqual(
      Quantity<Q> a, Quantity<Q> b, double relQuantityTolerance) {

    double aVal = a.getValue().doubleValue();
    double bVal = b.to(a.getUnit()).getValue().doubleValue();

    return (Math.abs(aVal - bVal) / Math.abs(aVal)) <= relQuantityTolerance;
  }

  /**
   * Compares two {@link Angle} {@link Quantity}s, if they are considerably equal. This is foremost
   * important for {@link tec.uom.se.quantity.DoubleQuantity}s. The comparison is made on the
   * absolute difference of both quantities' value. As of the repetitive nature of angles, they have
   * to be treated separately, e.g. -170° is semantically the same angle as 190°. To ensure this,
   * all quantities are converted to {@link PowerSystemUnits#DEGREE_GEOM}.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param quantityTolerance Permissible absolute tolerance
   * @return true, if both quantities' values differ less then the given tolerance else false
   */
  public static boolean considerablyEqualAngle(
      Quantity<Angle> a, Quantity<Angle> b, double quantityTolerance) {

    double aVal;
    double bVal;
    /* FIXME: Remove this catch, when the deprecated Units disappear in version 1.4 */
    try {
      aVal = a.to(DEGREE_GEOM).getValue().doubleValue();
      bVal = b.to(DEGREE_GEOM).getValue().doubleValue();
    } catch (UnconvertibleException e) {
      aVal =
          a.to(edu.ie3.util.quantities.dep.PowerSystemUnits.DEGREE_GEOM).getValue().doubleValue();
      bVal =
          b.to(edu.ie3.util.quantities.dep.PowerSystemUnits.DEGREE_GEOM).getValue().doubleValue();
    }

    /* When they match on the first trial, return true */
    boolean isConsiderablyEqual = abs(aVal - bVal) <= quantityTolerance;
    if (isConsiderablyEqual) return true;
    else {
      /* Compare, if the distance to 180° is the same, preserving the sign of the distance */
      if (aVal > bVal) return abs((aVal - 180) - (bVal + 180)) <= quantityTolerance;
      else return abs((aVal + 180) - (bVal - 180)) <= quantityTolerance;
    }
  }

  /**
   * * Compares two {@link Quantity}s, if they are equal. Replaces Objects.equals for Quantities, as
   * they cannot be checked on semantic equality with Quantity.equals(Quantity)
   *
   * @param a   First quantity to compare
   * @param b   Second quantity to compare
   * @param <Q> Type of Quantity
   * @return true, if both quantities are semantically equal
   */
  public static <Q extends Quantity<Q>> boolean equals(
          ComparableQuantity<Q> a, ComparableQuantity<Q> b) {
    if (a == null) return b == null;
    if (b == null) return false;
    return a.isEquivalentTo(b);
  }
}
