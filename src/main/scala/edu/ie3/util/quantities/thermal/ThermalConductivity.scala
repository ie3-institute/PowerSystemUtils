/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import squants.*
import squants.energy.Watts

import scala.util.Try

/** Represents the thermal conductivity, in W/(m*K) */
final class ThermalConductivity private (
    val value: Double,
    val unit: ThermalConductivityUnit
) extends Quantity[ThermalConductivity] {

  def dimension: ThermalConductivity.type = ThermalConductivity

  def *(that: Length): ThermalConductance = WattsPerKelvin(
    toWattsPerMeterKelvin * that.toMeters
  )

  def toWattsPerMeterKelvin: Double = to(WattsPerMeterKelvin)
  def toKilowattsPerMeterKelvin: Double = to(KilowattsPerMeterKelvin)
}

object ThermalConductivity extends Dimension[ThermalConductivity] {
  private[thermal] def apply[A](n: A, unit: ThermalConductivityUnit)(using
      num: Numeric[A]
  ) =
    new ThermalConductivity(num.toDouble(n), unit)
  def apply(value: Any): Try[ThermalConductivity] = parse(value)
  def name = "ThermalConductivity"
  def primaryUnit: WattsPerMeterKelvin.type = WattsPerMeterKelvin
  def siUnit: WattsPerMeterKelvin.type = WattsPerMeterKelvin
  def units: Set[UnitOfMeasure[ThermalConductivity]] = Set(
    WattsPerMeterKelvin,
    KilowattsPerMeterKelvin
  )
}

trait ThermalConductivityUnit
    extends UnitOfMeasure[ThermalConductivity]
    with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): ThermalConductivity =
    ThermalConductivity(n, this)
}

object WattsPerMeterKelvin
    extends ThermalConductivityUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = Watts.symbol + "/(m *" + Kelvin.symbol + ")"
}

object KilowattsPerMeterKelvin extends ThermalConductivityUnit {
  val symbol: String = "k" + WattsPerMeterKelvin.symbol
  val conversionFactor: Double = MetricSystem.Kilo
}
