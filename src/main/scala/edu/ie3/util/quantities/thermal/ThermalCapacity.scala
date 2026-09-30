/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import squants.thermal.{ThermalCapacity, ThermalCapacityUnit}
import squants.time.Time

/** Additional unit for [[ThermalCapacity]].
  */
object KilowattHoursPerKelvin extends ThermalCapacityUnit {
  val symbol = "kWh/K"
  val conversionFactor: Double = 3.6e6
}
