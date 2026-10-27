/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import edu.ie3.util.quantities.energy.KilowattHoursPerJoules
import squants.*
import squants.energy.{Joules, KilowattHours}
import squants.space.{Length, Meters}
import squants.thermal.{JoulesPerKelvin, Kelvin, ThermalCapacity}

import scala.util.Try

/** Represents the thermal capacity per unit of length, e.g. in J/(m*K).
  */
final class ThermalCapacityPerLength private (
    val value: Double,
    val unit: ThermalCapacityPerLengthUnit
) extends Quantity[ThermalCapacityPerLength] {

  def dimension: ThermalCapacityPerLength.type = ThermalCapacityPerLength

  def toJoulesPerMeterKelvin: Double = to(JoulesPerMeterKelvin)
  def toKilowattHoursPerMeterKelvin: Double = to(KilowattHoursPerMeterKelvin)

  /** Calculates the Energy of a medium with a given thermal capacity per length
    * based on the temperature delta, and its length. Returned energy is in kWh.
    */
  def calcEnergy(
      temperatureA: Temperature,
      temperatureB: Temperature,
      length: Length
  ): Energy =
    KilowattHours(
      this.toKilowattHoursPerMeterKelvin * math.abs(
        temperatureA.toKelvinScale - temperatureB.toKelvinScale
      ) * length.toMeters
    )

  /** Multiplies this thermal capacity per length (J/(m*K)) with a given length.
    *
    * @param length
    *   The length of the medium.
    * @return
    *   The resulting thermal capacity in J/K.
    */
  def *(length: Length): ThermalCapacity =
    JoulesPerKelvin(this.toJoulesPerMeterKelvin * length.toMeters)
}

object ThermalCapacityPerLength extends Dimension[ThermalCapacityPerLength] {
  def apply[A](n: A, unit: ThermalCapacityPerLengthUnit)(using
      num: Numeric[A]
  ) =
    new ThermalCapacityPerLength(num.toDouble(n), unit)
  def apply(value: Any): Try[ThermalCapacityPerLength] = parse(value)
  def name = "ThermalCapacityPerLength"
  def primaryUnit: JoulesPerMeterKelvin.type = JoulesPerMeterKelvin
  def siUnit: JoulesPerMeterKelvin.type = JoulesPerMeterKelvin
  def units: Set[UnitOfMeasure[ThermalCapacityPerLength]] = Set(
    JoulesPerMeterKelvin,
    KilowattHoursPerMeterKelvin
  )
}

trait ThermalCapacityPerLengthUnit
    extends UnitOfMeasure[ThermalCapacityPerLength]
    with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): ThermalCapacityPerLength =
    ThermalCapacityPerLength(n, this)
}

object JoulesPerMeterKelvin
    extends ThermalCapacityPerLengthUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String =
    Joules.symbol + "/(" + Meters.symbol + "*" + Kelvin.symbol + ")"
}

object KilowattHoursPerMeterKelvin extends ThermalCapacityPerLengthUnit {
  val conversionFactor: Double = KilowattHoursPerJoules
  val symbol: String =
    KilowattHours.symbol + "/(" + Meters.symbol + Kelvin.symbol + ")"
}
