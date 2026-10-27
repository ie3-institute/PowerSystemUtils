/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import edu.ie3.util.quantities.energy.KilowattHoursPerJoules
import squants.thermal.{ThermalCapacity, ThermalCapacityUnit}
import squants.{Mass, Volume}

/** Additional unit for [[ThermalCapacity]].
  */
object KilowattHoursPerKelvin extends ThermalCapacityUnit {
  val symbol = "kWh/K"
  val conversionFactor: Double = KilowattHoursPerJoules
}

extension (thermalCapacity: ThermalCapacity) {

  def /(that: Mass): SpecificHeatCapacity = JoulesPerKilogramKelvin(
    thermalCapacity.toJoulesPerKelvin / that.toKilograms
  )

  def /(that: Volume): VolumetricHeatCapacity = JoulesPerCubicMeterKelvin(
    thermalCapacity.toJoulesPerKelvin / that.toCubicMeters
  )

}
