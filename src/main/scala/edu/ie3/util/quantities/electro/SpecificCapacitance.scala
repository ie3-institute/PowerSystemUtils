/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro

import squants.electro.Farads
import squants.{
  Dimension,
  MetricSystem,
  PrimaryUnit,
  Quantity,
  SiUnit,
  UnitConverter,
  UnitOfMeasure
}

/** Class that represents a specific electrical conductance.
  *
  * @param value
  *   conductance value
  * @param unit
  *   of the specific conductance
  */
final class SpecificCapacitance private (
    val value: Double,
    val unit: SpecificCapacitanceUnit
) extends Quantity[SpecificCapacitance] {

  def dimension: SpecificCapacitance.type = SpecificCapacitance

  def toFaradPerMeter: Double = to(FaradPerMeter)
  def toMicrofaradsPerKilometer: Double = to(MicrofaradsPerKilometer)
  def toMillifaradsPerKilometer: Double = to(MillifaradsPerKilometer)
  def toFaradsPerKilometer: Double = to(FaradsPerKilometer)
  def toKilofaradsPerKilometer: Double = to(KilofaradsPerKilometer)
}

object SpecificCapacitance extends Dimension[SpecificCapacitance] {
  private[electro] def apply[A](n: A, unit: SpecificCapacitanceUnit)(using
      num: Numeric[A]
  ) = new SpecificCapacitance(num.toDouble(n), unit)

  def name = "SpecificCapacitance"
  def primaryUnit: FaradsPerKilometer.type = FaradsPerKilometer
  def siUnit: FaradsPerKilometer.type = FaradsPerKilometer
  def units: Set[UnitOfMeasure[SpecificCapacitance]] =
    Set(
      FaradsPerKilometer,
      FaradPerMeter,
      MicrofaradsPerKilometer,
      MillifaradsPerKilometer,
      KilofaradsPerKilometer
    )
}

trait SpecificCapacitanceUnit
    extends UnitOfMeasure[SpecificCapacitance]
    with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): SpecificCapacitance =
    SpecificCapacitance(n, this)
}

object MicrofaradsPerKilometer extends SpecificCapacitanceUnit with SiUnit {
  val conversionFactor: Double = MetricSystem.Milli
  val symbol: String = "µ" + FaradsPerKilometer.symbol
}

object MillifaradsPerKilometer extends SpecificCapacitanceUnit with SiUnit {
  val conversionFactor: Double = MetricSystem.Milli
  val symbol: String = "m" + FaradsPerKilometer.symbol
}

object FaradsPerKilometer
    extends SpecificCapacitanceUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = Farads.symbol + "/km"
}

object FaradPerMeter extends SpecificCapacitanceUnit with SiUnit {
  val symbol: String = Farads.symbol + "/m"
  val conversionFactor: Double = MetricSystem.Kilo
}

object KilofaradsPerKilometer extends SpecificCapacitanceUnit with SiUnit {
  val conversionFactor: Double = MetricSystem.Kilo
  val symbol: String = "k" + FaradsPerKilometer.symbol
}
