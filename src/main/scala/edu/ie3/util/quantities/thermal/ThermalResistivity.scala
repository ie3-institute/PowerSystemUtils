/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import squants.energy.Watts
import squants.space.Meters
import squants.thermal.Kelvin
import squants.{
  Dimension,
  PrimaryUnit,
  Quantity,
  SiUnit,
  UnitConverter,
  UnitOfMeasure
}

/** Represents the thermal resistivity, in K*m/W.
  *
  * @param value
  *   thermal resistivity value
  * @param unit
  *   of the thermal resistivity
  */
final class ThermalResistivity private (
    val value: Double,
    val unit: ThermalResistivityUnit
) extends Quantity[ThermalResistivity] {

  def dimension: ThermalResistivity.type = ThermalResistivity

  def toKelvinMetersPerWatt: Double = to(KelvinMetersPerWatt)
}

object ThermalResistivity extends Dimension[ThermalResistivity] {
  private[thermal] def apply[A](n: A, unit: ThermalResistivityUnit)(using
      num: Numeric[A]
  ) = new ThermalResistivity(num.toDouble(n), unit)

  def name = "ThermalResistivity"
  def primaryUnit: KelvinMetersPerWatt.type = KelvinMetersPerWatt
  def siUnit: KelvinMetersPerWatt.type = KelvinMetersPerWatt
  def units: Set[UnitOfMeasure[ThermalResistivity]] = Set(KelvinMetersPerWatt)
}

trait ThermalResistivityUnit
    extends UnitOfMeasure[ThermalResistivity]
    with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): ThermalResistivity =
    ThermalResistivity(n, this)
}

object KelvinMetersPerWatt
    extends ThermalResistivityUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String =
    "(" + Kelvin.symbol + "*" + Meters.symbol + ")/" + Watts.symbol
}
