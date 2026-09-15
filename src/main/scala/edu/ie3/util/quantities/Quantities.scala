/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import edu.ie3.util.NumericUtils
import squants.*
import squants.electro.ElectricPotential

import java.util.function.BiFunction

/** Object that provides some utility methods to create squants from Java.
  */
object Quantities {

  /** Constructs a quantity with the given value and unit.
    * @param value
    *   of the quantity
    * @param unit
    *   of the quantity
    * @tparam A
    *   type of quantity
    * @return
    *   the build quantity
    */
  def get[A <: Quantity[A]](value: Double, unit: UnitOfMeasure[A]): A = unit(
    value
  )

  /** Constructs a quantity with the given value and unit apply method.
    *
    * @param value
    *   of the quantity
    * @param apply
    *   of the quantity
    * @tparam A
    *   type of quantity
    * @return
    *   the build quantity
    */
  def get[A <: Quantity[A], B](
      value: Double,
      apply: BiFunction[B, Numeric[B], A]
  ): A = apply(value, NumericUtils.Double)

}
