/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import edu.ie3.util.quantities.energy.KilowattHoursPerJoules
import squants.*
import squants.thermal.{JoulesPerKelvin, ThermalCapacity}

import scala.util.Try

/** Represents the specific heat capacity, in J/(kg*K) or kWh/(kg*K). */
final class SpecificHeatCapacity private (
    val value: Double,
    val unit: SpecificHeatCapacityUnit
) extends Quantity[SpecificHeatCapacity] {

  def dimension: SpecificHeatCapacity.type = SpecificHeatCapacity

  /** Returns the value of this quantity in J / (kg * K) */
  def toJoulesPerKilogramKelvin: Double = to(JoulesPerKilogramKelvin)

  /** Returns the value of this quantity in kWh / (kg * K) */
  def toKilowattHoursPerKilogramKelvin: Double =
    this.toJoulesPerKilogramKelvin / KilowattHoursPerJoules

  def *(that: Mass): ThermalCapacity = JoulesPerKelvin(
    toJoulesPerKilogramKelvin * that.toKilograms
  )

  def *(that: Density): VolumetricHeatCapacity = JoulesPerCubicMeterKelvin(
    toJoulesPerKilogramKelvin * that.toKilogramsPerCubicMeter
  )
}

object SpecificHeatCapacity extends Dimension[SpecificHeatCapacity] {
  private[thermal] def apply[A](n: A, unit: SpecificHeatCapacityUnit)(implicit
      num: Numeric[A]
  ) =
    new SpecificHeatCapacity(num.toDouble(n), unit)
  def apply(value: Any): Try[SpecificHeatCapacity] = parse(value)
  def name = "SpecificHeatCapacity"
  def primaryUnit: JoulesPerKilogramKelvin.type = JoulesPerKilogramKelvin
  def siUnit: JoulesPerKilogramKelvin.type = JoulesPerKilogramKelvin
  def units: Set[UnitOfMeasure[SpecificHeatCapacity]] = Set(
    JoulesPerKilogramKelvin,
    KilowattHoursPerKilogramKelvin
  )
}

trait SpecificHeatCapacityUnit
    extends UnitOfMeasure[SpecificHeatCapacity]
    with UnitConverter {
  def apply[A](n: A)(implicit num: Numeric[A]): SpecificHeatCapacity =
    SpecificHeatCapacity(n, this)
}

object JoulesPerKilogramKelvin
    extends SpecificHeatCapacityUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = "J/(kg*K)"
}

object KilowattHoursPerKilogramKelvin
    extends SpecificHeatCapacityUnit
    with SiUnit {
  val conversionFactor: Double = KilowattHoursPerJoules
  val symbol: String = "kWh/(kg*K)"
}
