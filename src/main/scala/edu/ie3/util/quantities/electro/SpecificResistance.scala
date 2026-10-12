/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro

import squants.electro.{ElectricalResistance, Ohms}
import squants.{
  Dimension,
  MetricSystem,
  PrimaryUnit,
  Quantity,
  SiUnit,
  UnitConverter,
  UnitOfMeasure
}
import squants.space.Length

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
  def toOhmsPerMeter: Double = to(OhmsPerMeter)
  def toMilliohmsPerMeter: Double = to(MilliohmsPerMeter)
  def toMicroohmsPerMeter: Double = to(MicroohmsPerMeter)

  /** Multiplies this specific resistance by a length to obtain a total
    * resistance.
    */
  def *(that: Length): ElectricalResistance =
    Ohms(this.toOhmsPerMeter * that.toMeters)
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
      KiloohmsPerKilometer,
      OhmsPerMeter,
      MilliohmsPerMeter,
      MicroohmsPerMeter
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

object OhmsPerMeter extends SpecificResistanceUnit with SiUnit {
  val symbol: String = Ohms.symbol + "/m"
  val conversionFactor: Double = MetricSystem.Kilo
}

object MilliohmsPerMeter extends SpecificResistanceUnit with SiUnit {
  val symbol: String = "m" + OhmsPerMeter.symbol
  val conversionFactor: Double = 1.0
}

object MicroohmsPerMeter extends SpecificResistanceUnit with SiUnit {
  val symbol: String = "µ" + OhmsPerMeter.symbol
  val conversionFactor: Double = MetricSystem.Milli
}