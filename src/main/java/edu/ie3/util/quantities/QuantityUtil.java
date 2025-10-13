/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities;

import static edu.ie3.util.quantities.PowerSystemUnits.DEGREE_GEOM;
import static java.lang.StrictMath.abs;

import java.util.Objects;
import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

/**
 * Offers useful methods to handle {@link Quantity}s <br>
 * <br>
 * For the purpose of this class and the use within the PowerSystemDataModel project, we
 * differentiate between equality and equivalence:
 *
 * <p><b>Equality</b> is understood like represented by {@link Object#equals(Object)}, so a quantity
 * of 1km is <i>not equals</i> to 1000m.
 *
 * <p><b>Equivalence</b> is based on the quantities magnitude, a quantity of 1km is
 * <i>equivalent</i> to 1000m.
 */
public class QuantityUtil {

  private static final double DEFAULT_TOLERANCE = 0d;

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
  public static <Q extends Quantity<Q>> ComparableQuantity<Q> asComparable(Quantity<Q> quantity) {
    return Quantities.getQuantity(quantity.getValue(), quantity.getUnit());
  }

  /**
   * Compares two {@link Quantity}s, if they are <b>equivalent</b> considering both quantities
   * magnitude. This is foremost important for {@link tech.units.indriya.quantity.NumberQuantity}s
   * with internal big decimal or double values. The comparison is made on the absolute difference
   * of both quantities' value. Both quantities are converted into <i>{@code a}</i>'s unit before
   * the comparison. Internally calls {@link QuantityUtil#isEquivalentAbs(Quantity, Quantity,
   * double)} with a default tolerance of {@value DEFAULT_TOLERANCE}
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param <Q> Type of Quantity
   * @return true, if both quantities' do not differ in values (considering no tolerance), else
   *     false
   */
  public static <Q extends Quantity<Q>> boolean isEquivalentAbs(Quantity<Q> a, Quantity<Q> b) {
    return isEquivalentAbs(a, b, DEFAULT_TOLERANCE);
  }

  /**
   * Compares two {@link Quantity}s, if they are <b>equivalent</b> considering both quantities
   * magnitude. This is foremost important for {@link tech.units.indriya.quantity.NumberQuantity}s
   * with internal big decimal or double values. The comparison is made on the absolute difference
   * of both quantities' value. Both quantities are converted into <i>{@code a}</i>'s unit before
   * the comparison.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param absQuantityTolerance Permissible absolute tolerance
   * @param <Q> Type of Quantity
   * @return true, if both quantities' values differ less than the given tolerance (based on
   *     <i>{@code a}</i>'s unit) else false
   */
  public static <Q extends Quantity<Q>> boolean isEquivalentAbs(
      Quantity<Q> a, Quantity<Q> b, double absQuantityTolerance) {

    double aVal = a.getValue().doubleValue();
    double bVal = b.to(a.getUnit()).getValue().doubleValue();

    return Math.abs(aVal - bVal) <= absQuantityTolerance;
  }

  /**
   * Compares two {@link Quantity}s, if they are <b>equivalent</b> considering both quantities
   * magnitude. This is foremost important for {@link tech.units.indriya.quantity.NumberQuantity}s
   * with internal big decimal or double values. The comparison is made on the absolute difference
   * of both quantities' value. Both quantities are converted into <i>{@code a}</i>'s unit before
   * the comparison.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param absQuantityTolerance Permissible absolute tolerance
   * @param <Q> Type of Quantity
   * @return true, if both quantities' values differ less than the given tolerance (based on
   *     <i>{@code a}</i>'s unit) else false
   * @deprecated renamed to {@link QuantityUtil#isEquivalentAbs(Quantity, Quantity, double)} for
   *     clarity und uniformity
   */
  @Deprecated(since = "1.4", forRemoval = true)
  public static <Q extends Quantity<Q>> boolean considerablyAbsEqual(
      Quantity<Q> a, Quantity<Q> b, double absQuantityTolerance) {
    return isEquivalentAbs(a, b, absQuantityTolerance);
  }

  /**
   * Compares two {@link Quantity}s, if they are <b>equivalent</b> considering both quantities
   * magnitude. This is foremost important for {@link tech.units.indriya.quantity.NumberQuantity}s
   * with internal big decimal or double values. The comparison is made on the relative difference
   * of both quantities' value with regard to <i>{@code a}</i>'s value. Both quantities are
   * converted into <i>{@code a}</i>'s unit before the comparison. Internally calls {@link
   * QuantityUtil#isEquivalentRel(Quantity, Quantity, double)} with a default tolerance of {@value
   * DEFAULT_TOLERANCE}
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param <Q> Type of Quantity
   * @return true, if both quantities' values do not differ in value (considering no tolerance) else
   *     false
   */
  public static <Q extends Quantity<Q>> boolean isEquivalentRel(Quantity<Q> a, Quantity<Q> b) {
    return isEquivalentRel(a, b, DEFAULT_TOLERANCE);
  }

  /**
   * Compares two {@link Quantity}s, if they are <b>equivalent</b> considering both quantities
   * magnitude. This is foremost important for {@link tech.units.indriya.quantity.NumberQuantity}s
   * with internal big decimal or double values. The comparison is made on the relative difference
   * of both quantities' value with regard to <i>{@code a}</i>'s value. Both quantities are
   * converted into <i>{@code a}</i>'s unit before the comparison.
   *
   * <p>If {@code a} is 0, the result is just based on strict equality with {@code b}, since no
   * relative comparison is possible.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param relQuantityTolerance Permissible relative tolerance
   * @param <Q> Type of Quantity
   * @return true, if both quantities' values differ less than the given tolerance else false
   */
  public static <Q extends Quantity<Q>> boolean isEquivalentRel(
      Quantity<Q> a, Quantity<Q> b, double relQuantityTolerance) {

    double aVal = a.getValue().doubleValue();
    double bVal = b.to(a.getUnit()).getValue().doubleValue();

    if (aVal == 0.0) {
      return bVal == 0.0;
    }

    return (Math.abs(aVal - bVal) / Math.abs(aVal)) <= relQuantityTolerance;
  }

  /**
   * Compares two {@link Quantity}s, if they are <b>equivalent</b> considering both quantities
   * magnitude. This is foremost important for {@link tech.units.indriya.quantity.NumberQuantity}s
   * with internal big decimal or double values. The comparison is made on the relative difference
   * of both quantities' value with regard to <i>{@code a}</i>'s value. Both quantities are
   * converted into <i>{@code a}</i>'s unit before the comparison.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param relQuantityTolerance Permissible relative tolerance
   * @param <Q> Type of Quantity
   * @return true, if both quantities' values differ less than the given tolerance else false
   * @deprecated Renamed to {@link QuantityUtil#isEquivalentRel(Quantity, Quantity, double)} for
   *     clarity und uniformity
   */
  @Deprecated
  public static <Q extends Quantity<Q>> boolean considerablyRelEqual(
      Quantity<Q> a, Quantity<Q> b, double relQuantityTolerance) {
    return isEquivalentRel(a, b, relQuantityTolerance);
  }

  /**
   * Compares two {@link Angle} {@link Quantity}s, if they are <b>equivalent</b>, especially
   * considering semantic equality for angles. This is foremost important for {@link
   * tech.units.indriya.quantity.NumberQuantity}s with internal big decimal or double values. The
   * comparison is made on the absolute difference of both quantities' value. As of the repetitive
   * nature of angles, they have to be treated separately, e.g. -170° is semantically the same angle
   * as 190°. To ensure this, all quantities are converted to {@link PowerSystemUnits#DEGREE_GEOM}.
   * Internally calls {@link QuantityUtil#isEquivalentAngle(Quantity, Quantity, double)} with a
   * default tolerance of {@value DEFAULT_TOLERANCE}
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @return true, if both quantities' values differ less (converted to degree) than the given
   *     tolerance else false
   */
  public static boolean isEquivalentAngle(Quantity<Angle> a, Quantity<Angle> b) {
    return isEquivalentAngle(a, b, DEFAULT_TOLERANCE);
  }

  /**
   * Compares two {@link Angle} {@link Quantity}s, if they are <b>equivalent</b>, especially
   * considering semantic equality for angles. This is foremost important for {@link
   * tech.units.indriya.quantity.NumberQuantity}s with internal big decimal or double values. The
   * comparison is made on the absolute difference of both quantities' value. As of the repetitive
   * nature of angles, they have to be treated separately, e.g. -170° is semantically the same angle
   * as 190°. To ensure this, all quantities are converted to {@link PowerSystemUnits#DEGREE_GEOM}.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param quantityTolerance Permissible absolute tolerance
   * @return true, if both quantities' values differ less (converted to degree) than the given
   *     tolerance else false
   */
  public static boolean isEquivalentAngle(
      Quantity<Angle> a, Quantity<Angle> b, double quantityTolerance) {

    double aVal;
    double bVal;

    aVal = a.to(DEGREE_GEOM).getValue().doubleValue();
    bVal = b.to(DEGREE_GEOM).getValue().doubleValue();

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
   * Compares two {@link Angle} {@link Quantity}s, if they are <b>equivalent</b>, especially
   * considering semantic equality for angles. This is foremost important for {@link
   * tech.units.indriya.quantity.NumberQuantity}s with internal big decimal or double values. The
   * comparison is made on the absolute difference of both quantities' value. As of the repetitive
   * nature of angles, they have to be treated separately, e.g. -170° is semantically the same angle
   * as 190°. To ensure this, all quantities are converted to {@link PowerSystemUnits#DEGREE_GEOM}.
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param quantityTolerance Permissible absolute tolerance
   * @return true, if both quantities' values differ less (converted to degree) than the given
   *     tolerance else false
   * @deprecated Renamed to {@link QuantityUtil#isEquivalentAngle(Quantity, Quantity, double)}
   */
  @Deprecated
  public static boolean considerablyEqualAngle(
      Quantity<Angle> a, Quantity<Angle> b, double quantityTolerance) {
    return isEquivalentAngle(a, b, quantityTolerance);
  }

  /**
   * Compares two {@link Quantity}s, if they are equivalent. Returns true only if both quantities
   * are of type {@link EmptyQuantity} or if they represent equivalent values. Throws a
   * NullPointerException if any quantity is null, as null is not to be expected as any known empty
   * value should be replaced by an EmptyQuantity. Internally calls {@link
   * QuantityUtil#isEquivalentConsideringEmpty(ComparableQuantity, ComparableQuantity, double)} with
   * a default tolerance of {@value DEFAULT_TOLERANCE}
   *
   * <p>isEquivalentConsideringEmpty(1 km, 1000 m)// true <br>
   * isEquivalentConsideringEmpty(1.0 km, 1 km) // true
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param <Q> Type of the Quantity
   * @return true, if both are an EmptyQuantity or if they are equivalent
   * @throws NullPointerException if any quantity is null
   */
  public static <Q extends Quantity<Q>> boolean isEquivalentConsideringEmpty(
      ComparableQuantity<Q> a, ComparableQuantity<Q> b) {
    return isEquivalentConsideringEmpty(a, b, DEFAULT_TOLERANCE);
  }

  /**
   * Compares two {@link Quantity}s, if they are equivalent. Returns true only if both quantities
   * are of type {@link EmptyQuantity} or if they represent equivalent values, considering the given
   * tolerance. Throws a NullPointerException if any quantity is null, as null is not to be expected
   * as any known empty value should be replaced by an EmptyQuantity.
   *
   * <p>QuantityUtil.isEquivalentConsideringEmpty(1 km, 1000 m, 0) //true <br>
   * QuantityUtil.isEquivalentConsideringEmpty(1.0 km, 1 km, 0) //true <br>
   * QuantityUtil.isEquivalentConsideringEmpty(1.0km, 1.1km, 0.1) //true <br>
   * QuantityUtil.isEquivalentConsideringEmpty(1.0km, 1100m, 0.1) //true
   * QuantityUtil.isEquivalentConsideringEmpty(1100m, 1.0km, 0.1) //false
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param quantityTolerance permissible absolute tolerance - applied in <i>{@code a}</i>'s units
   * @param <Q> Type of the Quantity
   * @return true, if both are an EmptyQuantity or if they are equivalent
   * @throws NullPointerException if any quantity is null
   */
  public static <Q extends Quantity<Q>> boolean isEquivalentConsideringEmpty(
      ComparableQuantity<Q> a, ComparableQuantity<Q> b, double quantityTolerance) {
    Objects.requireNonNull(
        a, "There was an error while comparing quantities: quantity a was not set");
    Objects.requireNonNull(
        b, "There was an error while comparing quantities: quantity b was not set");
    if (!QuantityUtil.isEmpty(a)) {
      if (QuantityUtil.isEmpty(b)) return false;
      if (a.isEquivalentTo(b)) return true;
      return isEquivalentAbs(a, b, quantityTolerance);
    } else return QuantityUtil.isEmpty(b);
  }

  /**
   * Checks if the given quantity is empty by returning positive if it is an EmptyQuantity.
   *
   * @param that quantity to compare
   * @return true, if the object is a EmptyQuantity - false otherwise, even if the quantity is null
   */
  public static boolean isEmpty(Quantity<?> that) {
    if (that == null) return false;
    return that.getClass().isAssignableFrom(EmptyQuantity.class);
  }

  /**
   * Compares two {@link Quantity}s, if they are equal. Returns true only if both quantities are of
   * type {@link EmptyQuantity} or if they are equal in value <b>and</b> Unit. The type of the value
   * does not matter. Throws a NullPointerException if any quantity is null, as null is not to be
   * expected as any known empty value should be replaced by an EmptyQuantity.<br>
   * Internally calls {@link QuantityUtil#equals(Quantity, Quantity, double)} with a default
   * tolerance of {@value DEFAULT_TOLERANCE}
   *
   * <p>QuantityUtil.equals(1 km, 1000 m) // false <br>
   * QuantityUtil.equals(1.0 km, 1 km) //true
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param <Q> Type of the Quantity
   * @return true, if both are an EmptyQuantity or if they are equal
   * @throws NullPointerException if any quantity is null
   */
  public static <Q extends Quantity<Q>> boolean equals(Quantity<Q> a, Quantity<Q> b) {
    return equals(a, b, DEFAULT_TOLERANCE);
  }

  /**
   * Compares two {@link Quantity}s, if they are equal. Returns true only if both quantities are of
   * type {@link EmptyQuantity} or if they are equal in value <b>and</b> Unit. The type of the value
   * does not matter. Throws a NullPointerException if any quantity is null, as null is not to be
   * expected as any known empty value should be replaced by an EmptyQuantity.
   *
   * <p>QuantityUtil.equals(1 km, 1000 m, 0) //false <br>
   * QuantityUtil.equals(1.0 km, 1 km, 0) //true <br>
   * QuantityUtil.equals(1.0km, 1.1km, 0.1) //true
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param quantityTolerance permissible absolute tolerance
   * @param <Q> Type of the Quantity
   * @return true, if both are an EmptyQuantity or if they are equal
   * @throws NullPointerException if any quantity is null
   */
  public static <Q extends Quantity<Q>> boolean equals(
      Quantity<Q> a, Quantity<Q> b, double quantityTolerance) {
    Objects.requireNonNull(
        a, "There was an error while comparing quantities: quantity a was not set");
    Objects.requireNonNull(
        b, "There was an error while comparing quantities: quantity b was not set");
    if (!QuantityUtil.isEmpty(a)) {
      if (QuantityUtil.isEmpty(b)) return false;
      if (!a.getUnit().equals(b.getUnit())) return false;
      return Math.abs(a.getValue().doubleValue() - b.getValue().doubleValue()) <= quantityTolerance;
    } else return QuantityUtil.isEmpty(b);
  }

  /**
   * Compares two {@link Quantity}s, if they are equal. Returns true only if both quantities are of
   * type {@link EmptyQuantity} or if they are equal in value <b>and</b> Unit. The type of the value
   * does not matter. Throws a NullPointerException if any quantity is null, as null is not to be
   * expected as any known empty value should be replaced by an EmptyQuantity.
   *
   * <p>isTheSameConsideringEmpty(1 km, 1000 m) // false <br>
   * isTheSameConsideringEmpty(1.0 km, 1 km) //true
   *
   * @param a First quantity to compare
   * @param b Second quantity to compare
   * @param <Q> Type of the Quantity
   * @return true, if both are an EmptyQuantity or if they are equal
   * @throws NullPointerException if any quantity is null
   * @deprecated Renamed to {@link QuantityUtil#equals(Quantity, Quantity)} for clarity and
   *     uniformity
   */
  @Deprecated
  public static <Q extends Quantity<Q>> boolean isTheSameConsideringEmpty(
      Quantity<Q> a, Quantity<Q> b) {
    return equals(a, b);
  }
}
