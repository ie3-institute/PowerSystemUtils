/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import edu.ie3.util.quantities.energy.{
  KilowattHoursPerCubicMeter,
  KilowattHoursPerJoules
}
import squants.*
import squants.energy.{EnergyDensity, KilowattHours}
import squants.thermal.{JoulesPerKelvin, ThermalCapacity}

import scala.util.Try

/** Represents the volumetric heat capacity, in J/(m³*K) or kWh/(m³*K). */
final class VolumetricHeatCapacity private (
    val value: Double,
    val unit: VolumetricHeatCapacityUnit
) extends Quantity[VolumetricHeatCapacity] {

  def dimension: VolumetricHeatCapacity.type = VolumetricHeatCapacity

  /** Returns the value of this quantity in J / (m³ * K) */
  def toJoulesPerCubicMeterKelvin: Double = to(JoulesPerCubicMeterKelvin)

  /** Returns the value of this quantity in kWh / (m³ * K) */
  def toKilowattHoursPerCubicMeterKelvin: Double =
    this.toJoulesPerCubicMeterKelvin / KilowattHoursPerJoules

  def *(that: Volume): ThermalCapacity = JoulesPerKelvin(
    toJoulesPerCubicMeterKelvin * that.toCubicMeters
  )

  def /(that: Density): SpecificHeatCapacity = JoulesPerKilogramKelvin(
    toJoulesPerCubicMeterKelvin / that.toKilogramsPerCubicMeter
  )

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

object VolumetricHeatCapacity extends Dimension[VolumetricHeatCapacity] {
  private[thermal] def apply[A](n: A, unit: VolumetricHeatCapacityUnit)(implicit
      num: Numeric[A]
  ) =
    new VolumetricHeatCapacity(num.toDouble(n), unit)
  def apply(value: Any): Try[VolumetricHeatCapacity] = parse(value)
  def name = "VolumetricHeatCapacity"
  def primaryUnit: JoulesPerCubicMeterKelvin.type = JoulesPerCubicMeterKelvin
  def siUnit: JoulesPerCubicMeterKelvin.type = JoulesPerCubicMeterKelvin
  def units: Set[UnitOfMeasure[VolumetricHeatCapacity]] = Set(
    JoulesPerCubicMeterKelvin,
    KilowattHoursPerCubicMeterKelvin
  )
}

trait VolumetricHeatCapacityUnit
    extends UnitOfMeasure[VolumetricHeatCapacity]
    with UnitConverter {
  def apply[A](n: A)(implicit num: Numeric[A]): VolumetricHeatCapacity =
    VolumetricHeatCapacity(num.toDouble(n), this)
}

object JoulesPerCubicMeterKelvin
    extends VolumetricHeatCapacityUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = "J/(m³*K)"
}

object KilowattHoursPerCubicMeterKelvin
    extends VolumetricHeatCapacityUnit
    with SiUnit {
  val conversionFactor: Double = KilowattHoursPerJoules
  val symbol: String = "kWh/(m³*K)"
}
