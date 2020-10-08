/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities;

import edu.ie3.util.exceptions.EmptyQuantityException;
import javax.measure.Quantity;
import javax.measure.Unit;
import tech.units.indriya.AbstractQuantity;
import tech.units.indriya.ComparableQuantity;

/**
 * This class represents a Quantity with the value null. So i.e. instead of "{@code Quantity<Length>
 * length = null;}" you should use "{@code Quantity<Length> length =
 * EmptyQuantity.of(Units.METRE);}" <br>
 * Any arithmetic operations you perform on this class will throw a EmptyQuantityException as you're
 * not intended to do so. The reason to have this quantity instead of using null directly is to
 * provide a more obvious way to deal with empty quantities instead of just using null for an empty
 * quantity.
 *
 * <p>Possible application cases are e.g. time series consisting of several quantities based on real
 * data. As real time series sometimes might lack some values, it is preferred to indicate that that
 * these values are missing in the input data by providing an instance of {@link EmptyQuantity}
 * instead of using null. This is motivated by making the missing value explicit, instead of
 * implicitly assuming that null represents a missing value.
 *
 * @param <Q> Unit of this quantity
 */
public final class EmptyQuantity<Q extends Quantity<Q>> extends AbstractQuantity<Q> {

  private static final String EXCEPTION_MESSAGE =
      "The EmptyQuantity represents a 'null' value, so you should not perform any operations on this Quantity.";

  private EmptyQuantity(Unit<Q> unit) {
    super(unit);
  }

  /**
   * Initializes an EmptyQuantity with the specified Unit
   *
   * @param unit The Unit for the EmptyQuantity
   * @param <U> The Unit/Quantity type of the EmptyQuantity object
   * @return EmptyQuantity with specified unit
   */
  public static <U extends Quantity<U>> EmptyQuantity<U> of(Unit<U> unit) {
    return new EmptyQuantity<>(unit);
  }

  /**
   * Returns always null, as that is the only value an EmptyQuantity can and should have
   *
   * @return null, always
   */
  @Override
  public Number getValue() {
    return null;
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * should not perform operations on this Quantity
   *
   * @param that Quantity to perform the operation on
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<Q> add(Quantity<Q> that) {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * should not perform operations on this Quantity
   *
   * @param that Quantity to perform the operation on
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<Q> subtract(Quantity<Q> that) {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * should not perform operations on this Quantity
   *
   * @param that Quantity to perform the operation on
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<?> divide(Quantity<?> that) {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * should not perform operations on this Quantity
   *
   * @param that Quantity to perform the operation on
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<Q> divide(Number that) {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * should not perform operations on this Quantity
   *
   * @param multiplier Quantity to perform the operation on
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<?> multiply(Quantity<?> multiplier) {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * should not perform operations on this Quantity
   *
   * @param multiplier Quantity to perform the operation on
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<Q> multiply(Number multiplier) {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * should not perform operations on this Quantity
   *
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<?> inverse() {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * should not perform operations on this Quantity
   *
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public Quantity<Q> negate() {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Decides equality based <b>only</b> on the type of the object: If it is an EmptyQuantity, it is
   * equal. This is based on the thought that nothing is always equals to nothing.
   *
   * @param that object to compare
   * @return true, if the object is an EmptyQuantity
   */
  @Override
  public boolean equals(Object that) {
    if (that == null) return false;
    return that.getClass().isAssignableFrom(EmptyQuantity.class);
  }

  /**
   * Overrides the super method, only to throw a EmptyQuantityException in <b>any</b> case as you
   * can't create a hashcode representing null.
   *
   * @return nothing, ever. Throws EmptyQuantityException instead.
   * @deprecated EmptyQuantity represents a 'null' value, so you should not perform any operations
   *     on this Quantity
   */
  @Override
  @Deprecated
  public int hashCode() {
    throw new EmptyQuantityException(EXCEPTION_MESSAGE);
  }

  /**
   * Decides equality based <b>only</b> on the type of the quantity: If it is an EmptyQuantity, it
   * is equal. An EmptyQuantity does not have a value, so equality is checked using the {@link
   * EmptyQuantity#equals(Object)} method.
   *
   * @param that object to compare
   * @return true, if the object is an EmptyQuantity
   */
  @Override
  public boolean isEquivalentTo(Quantity that) {
    return equals(that);
  }
}
