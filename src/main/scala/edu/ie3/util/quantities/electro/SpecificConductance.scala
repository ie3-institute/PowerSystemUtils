/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro

import squants.electro.Siemens
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
final class SpecificConductance private (
    val value: Double,
    val unit: SpecificConductanceUnit
) extends Quantity[SpecificConductance] {

  def dimension: SpecificConductance.type = SpecificConductance

  def toMicrosiemensPerKilometer: Double = to(MicrosiemensPerKilometer)
  def toMillisiemensPerKilometer: Double = to(MillisiemensPerKilometer)
  def toSiemensPerKilometer: Double = to(SiemensPerKilometer)
  def toKilosiemensPerKilometer: Double = to(KilosiemensPerKilometer)
}

object SpecificConductance extends Dimension[SpecificConductance] {
  private[electro] def apply[A](n: A, unit: SpecificConductanceUnit)(using
      num: Numeric[A]
  ) = new SpecificConductance(num.toDouble(n), unit)

  def name = "SpecificConductance"
  def primaryUnit: SiemensPerKilometer.type = SiemensPerKilometer
  def siUnit: SiemensPerKilometer.type = SiemensPerKilometer
  def units: Set[UnitOfMeasure[SpecificConductance]] =
    Set(
      SiemensPerKilometer,
      MicrosiemensPerKilometer,
      MillisiemensPerKilometer,
      KilosiemensPerKilometer
    )
}

trait SpecificConductanceUnit
    extends UnitOfMeasure[SpecificConductance]
    with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): SpecificConductance =
    SpecificConductance(n, this)
}

object MicrosiemensPerKilometer extends SpecificConductanceUnit with SiUnit {
  val conversionFactor: Double = MetricSystem.Milli
  val symbol: String = "µ" + SiemensPerKilometer.symbol
}

object MillisiemensPerKilometer extends SpecificConductanceUnit with SiUnit {
  val conversionFactor: Double = MetricSystem.Milli
  val symbol: String = "m" + SiemensPerKilometer.symbol
}

object SiemensPerKilometer
    extends SpecificConductanceUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = Siemens.symbol + "/km"
}

object KilosiemensPerKilometer extends SpecificConductanceUnit with SiUnit {
  val conversionFactor: Double = MetricSystem.Kilo
  val symbol: String = "k" + SiemensPerKilometer.symbol
}
