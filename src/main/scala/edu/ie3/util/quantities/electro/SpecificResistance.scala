/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro

import squants.electro.Ohms
import squants.{
  Dimension,
  MetricSystem,
  PrimaryUnit,
  Quantity,
  SiUnit,
  UnitConverter,
  UnitOfMeasure
}

/** Class that represents a specific electrical resistance.
  *
  * @param value
  *   resistance value
  * @param unit
  *   of the specific resistance
  */
final class SpecificResistance private (
    val value: Double,
    val unit: SpecificResistanceUnit
) extends Quantity[SpecificResistance] {

  def dimension: SpecificResistance.type = SpecificResistance

  def toMilliohmsPerKilometer: Double = to(MilliohmsPerKilometer)
  def toOhmsPerKilometer: Double = to(OhmsPerKilometer)
  def toKiloohmsPerKilometer: Double = to(KiloohmsPerKilometer)
}

object SpecificResistance extends Dimension[SpecificResistance] {
  private[electro] def apply[A](n: A, unit: SpecificResistanceUnit)(using
      num: Numeric[A]
  ) = new SpecificResistance(num.toDouble(n), unit)

  def name = "SpecificResistance"
  def primaryUnit: OhmsPerKilometer.type = OhmsPerKilometer
  def siUnit: OhmsPerKilometer.type = OhmsPerKilometer
  def units: Set[UnitOfMeasure[SpecificResistance]] =
    Set(
      OhmsPerKilometer,
      MilliohmsPerKilometer,
      KiloohmsPerKilometer
    )
}

trait SpecificResistanceUnit
    extends UnitOfMeasure[SpecificResistance]
    with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): SpecificResistance =
    SpecificResistance(n, this)
}

object MilliohmsPerKilometer extends SpecificResistanceUnit with SiUnit {
  val conversionFactor: Double = MetricSystem.Milli
  val symbol: String = "m" + OhmsPerKilometer.symbol
}

object OhmsPerKilometer
    extends SpecificResistanceUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = Ohms.symbol + "/km"
}

object KiloohmsPerKilometer extends SpecificResistanceUnit with SiUnit {
  val conversionFactor: Double = MetricSystem.Kilo
  val symbol: String = "k" + OhmsPerKilometer.symbol
}
