/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import javax.measure.{Quantity, Unit}

// Numeric Implementation for quantitiesused
final class ComparableQuantityNumeric[Q <: Quantity[Q]](unit: Unit[Q])
    extends Numeric[ComparableQuantity[Q]] {

  override def plus(
      x: ComparableQuantity[Q],
      y: ComparableQuantity[Q]
  ): ComparableQuantity[Q] = x.add(y)
  override def minus(
      x: ComparableQuantity[Q],
      y: ComparableQuantity[Q]
  ): ComparableQuantity[Q] = x.subtract(y)
  override def times(
      x: ComparableQuantity[Q],
      y: ComparableQuantity[Q]
  ): ComparableQuantity[Q] =
    throw new UnsupportedOperationException(
      "Numeric.times is not supported for ComparableQuantity"
    )
  override def negate(x: ComparableQuantity[Q]): ComparableQuantity[Q] =
    x.multiply(-1)
  override def fromInt(x: Int): ComparableQuantity[Q] =
    Quantities.getQuantity(x, unit)
  override def toInt(x: ComparableQuantity[Q]): Int =
    x.to(unit).getValue.intValue()
  override def toLong(x: ComparableQuantity[Q]): Long =
    x.to(unit).getValue.longValue()

  override def toFloat(x: ComparableQuantity[Q]): Float =
    x.to(unit).getValue.floatValue()
  override def toDouble(x: ComparableQuantity[Q]): Double =
    x.to(unit).getValue.doubleValue()
  override def compare(
      x: ComparableQuantity[Q],
      y: ComparableQuantity[Q]
  ): Int = x.compareTo(y)
  override def parseString(str: String): Option[ComparableQuantity[Q]] = None
}

object ComparableQuantityNumeric {
  def apply[Q <: Quantity[Q]](unit: Unit[Q]): ComparableQuantityNumeric[Q] =
    new ComparableQuantityNumeric(unit)
}
