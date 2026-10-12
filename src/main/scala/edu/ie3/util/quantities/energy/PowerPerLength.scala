/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.energy

import squants.energy.{Power, Watts}
import squants.space.Length
import squants.{
  Dimension,
  PrimaryUnit,
  Quantity,
  SiUnit,
  UnitConverter,
  UnitOfMeasure
}

/** Class that represents the power per unit of length, e.g. the thermal losses
  * of a cable expressed per meter of cable length.
  *
  * In W/m
  *
  * @param value
  *   power per length value
  * @param unit
  *   of the power per length
  */
final class PowerPerLength private (
    val value: Double,
    val unit: PowerPerLengthUnit
) extends Quantity[PowerPerLength] {

  def dimension: PowerPerLength.type = PowerPerLength

  def toWattsPerMeter: Double = to(WattsPerMeter)

  /** Multiplies this power per length by a length to obtain a total power.
    */
  def *(that: Length): Power =
    Watts(this.toWattsPerMeter * that.toMeters)
}

object PowerPerLength extends Dimension[PowerPerLength] {
  private[energy] def apply[A](n: A, unit: PowerPerLengthUnit)(using
      num: Numeric[A]
  ) = new PowerPerLength(num.toDouble(n), unit)

  def name = "PowerPerLength"
  def primaryUnit: WattsPerMeter.type = WattsPerMeter
  def siUnit: WattsPerMeter.type = WattsPerMeter
  def units: Set[UnitOfMeasure[PowerPerLength]] = Set(WattsPerMeter)
}

trait PowerPerLengthUnit
    extends UnitOfMeasure[PowerPerLength]
    with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): PowerPerLength =
    PowerPerLength(n, this)
}

object WattsPerMeter extends PowerPerLengthUnit with PrimaryUnit with SiUnit {
  val symbol: String = "W/m"
}
