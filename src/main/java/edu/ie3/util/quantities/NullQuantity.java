/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities;

import tech.units.indriya.AbstractQuantity;
import tech.units.indriya.ComparableQuantity;

import javax.measure.Quantity;
import javax.measure.Unit;

/**
 * This class represents a Quantity with the value null. So i.e. instead of "{@code Quantity<Length>
 * length = null;}" you should use "{@code Quantity<Length> length = NullQuantity.of(Units.METRE);}"
 * <br>
 * Any arithmetic operations you perform on this class will throw a NullPointerException as you're
 * not intended to do so.
 *
 * @param <Q> Unit of this quantity
 */
public final class NullQuantity<Q extends Quantity<Q>> extends AbstractQuantity<Q> {

  private static final String EXCEPTION_MESSAGE =
          "The NullQuantity represents a 'null' value, so you should not perform any operations on this Quantity.";

  private NullQuantity(Unit<Q> unit) {
    super(unit);
  }

  /**
   * Initializes a NullQuantity with the specified Unit
   *
   * @param unit The Unit for the NullQuantity
   * @param <U>  The Unit/Quantity type of the NullQuantity object
   * @return NullQuantity with specified unit
   */
  public static <U extends Quantity<U>> NullQuantity<U> of(Unit<U> unit) {
    return new NullQuantity<>(unit);
  }

  /**
   * Returns always null, as that is the only value a NullQuantity can and should have
   *
   * @return null, always
   */
  @Override
  public Number getValue() {
    return null;
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you should not
   * perform operations on this Quantity
   *
   * @param that Quantity to perform the operation on
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<Q> add(Quantity<Q> that) {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you should not
   * perform operations on this Quantity
   *
   * @param that Quantity to perform the operation on
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<Q> subtract(Quantity<Q> that) {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you should not
   * perform operations on this Quantity
   *
   * @param that Quantity to perform the operation on
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<?> divide(Quantity<?> that) {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you should not
   * perform operations on this Quantity
   *
   * @param that Quantity to perform the operation on
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<Q> divide(Number that) {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you should not
   * perform operations on this Quantity
   *
   * @param multiplier Quantity to perform the operation on
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<?> multiply(Quantity<?> multiplier) {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you should not
   * perform operations on this Quantity
   *
   * @param multiplier Quantity to perform the operation on
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<Q> multiply(Number multiplier) {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you should not
   * perform operations on this Quantity
   *
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public ComparableQuantity<?> inverse() {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you should not
   * perform operations on this Quantity
   *
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public Quantity<Q> negate() {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Decides equality based <b>only</b> on the type of the object: If it is a NullQuantity, it is
   * equal. This is based on the thought that nothing is always equals to nothing.
   *
   * @param that object to compare
   * @return true, if the object is a NullQuantity
   */
  @Override
  public boolean equals(Object that) {
    return that.getClass().isAssignableFrom(NullQuantity.class);
  }

  /**
   * Decides equality based on the type of the Quantity: If it is a NullQuantity, it is equal.
   * Otherwise, as opposed to {@link NullQuantity#equals(Object)} we know that we are supposed to
   * evaluate a Quantity, so a null here represents a NullQuantity.
   *
   * @param that object to compare
   * @return true, if the object is a NullQuantity or null
   */
  public static boolean quantityIsNull(Quantity<?> that) {
    if (that == null) return true;
    return that.getClass().isAssignableFrom(NullQuantity.class);
  }

  /**
   * Overrides the super method, only to throw a NullPointer in <b>any</b> case as you can't create
   * a hashcode representing null
   *
   * @return nothing, ever. Throws NullPointer instead.
   * @deprecated NullQuantity represents a 'null' value, so you should not perform any operations on
   *     this Quantity
   */
  @Override
  @Deprecated
  public int hashCode() {
    throw new NullPointerException(EXCEPTION_MESSAGE);
  }

  /**
   * Decides equality based on the type of the Quantity: If it is a NullQuantity, it is equal.
   * Otherwise, as opposed to {@link NullQuantity#equals(Object)} we know that we are supposed to
   * evaluate a Quantity, so a null here represents a NullQuantity.
   *
   * @param that object to compare
   * @return true, if the object is a NullQuantity or null
   */
  @Override
  public boolean isEquivalentTo(Quantity that) {
    if (that == null) return true;
    return that.getClass().isAssignableFrom(NullQuantity.class);
  }
}
