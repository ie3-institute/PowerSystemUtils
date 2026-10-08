/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import edu.ie3.util.quantities.energy.KilowattHoursPerCubicMeter
import squants.*
import squants.energy.{EnergyDensity, KilowattHours}

import scala.util.Try

/** Represents the thermal capacitance, in J/(m³*K) or kWh/(m³*K). */
final class ThermalCapacitance private (
    val value: Double,
    val unit: ThermalCapacitanceUnit
) extends Quantity[ThermalCapacitance] {

  def dimension: ThermalCapacitance.type = ThermalCapacitance

  /** Returns the value of this quantity in J / (m³ * K) */
  def toJoulesPerCubicMeterKelvin: Double = to(JoulesPerCubicMeterKelvin)

  /** Returns the value of this quantity in kWh / (m³ * K) */
  def toKilowattHoursPerCubicMeterKelvin: Double =
    this.toJoulesPerCubicMeterKelvin / 3600000.0

  /** Calculates the EnergyDensity of a medium with a given thermal capacitance
    * based on the temperature delta. Returned energy density is in kWh/m³.
    */
  def calcEnergyDensity(
      temperatureA: Temperature,
      temperatureB: Temperature
  ): EnergyDensity =
    KilowattHoursPerCubicMeter(
      toKilowattHoursPerCubicMeterKelvin * math.abs(
        temperatureA.toKelvinScale - temperatureB.toKelvinScale
      )
    )

  /** Calculates the Energy of a medium with a given thermal capacitance based
    * on the temperature delta, and its volume. Returned energy is in kWh.
    */
  def calcEnergy(
      temperatureA: Temperature,
      temperatureB: Temperature,
      volume: Volume
  ): Energy =
    KilowattHours(
      toKilowattHoursPerCubicMeterKelvin * math.abs(
        temperatureA.toKelvinScale - temperatureB.toKelvinScale
      ) * volume.toCubicMeters
    )
}

object ThermalCapacitance extends Dimension[ThermalCapacitance] {
  def apply[A](n: A, unit: ThermalCapacitanceUnit)(implicit num: Numeric[A]) =
    new ThermalCapacitance(num.toDouble(n), unit)
  def apply(value: Any): Try[ThermalCapacitance] = parse(value)
  def name = "ThermalCapacitance"
  def primaryUnit: JoulesPerCubicMeterKelvin.type = JoulesPerCubicMeterKelvin
  def siUnit: JoulesPerCubicMeterKelvin.type = JoulesPerCubicMeterKelvin
  def units: Set[UnitOfMeasure[ThermalCapacitance]] = Set(
    JoulesPerCubicMeterKelvin,
    KilowattHoursPerCubicMeterKelvin
  )
}

trait ThermalCapacitanceUnit
    extends UnitOfMeasure[ThermalCapacitance]
    with UnitConverter {
  def apply[A](n: A)(implicit num: Numeric[A]): ThermalCapacitance =
    ThermalCapacitance(n, this)
}

object JoulesPerCubicMeterKelvin
    extends ThermalCapacitanceUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = "J/(m³K)"
}

object KilowattHoursPerCubicMeterKelvin
    extends ThermalCapacitanceUnit
    with SiUnit {
  val conversionFactor: Double = 3600000.0
  val symbol: String = "kWh/(m³K)"
}
