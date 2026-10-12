/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.energy

import squants.SiUnit
import squants.energy.{EnergyDensity, EnergyDensityUnit}
import squants.space.CubicMeters

/** Additional unit for [[EnergyDensity]].
  */
object KilowattHoursPerCubicMeter extends EnergyDensityUnit with SiUnit {
  val symbol: String = "kWh/" + CubicMeters.symbol
  val conversionFactor = 3.6e6
}

/** Extension methods for [[EnergyDensity]]
  */
extension (energyDensity: EnergyDensity) {

  /** Returns a double in the unit of [[KilowattHoursPerCubicMeter]].
    */
  def toKilowattHoursPerCubicMeter: Double =
    energyDensity.to(KilowattHoursPerCubicMeter)
}
