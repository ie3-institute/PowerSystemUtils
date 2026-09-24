/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import squants.thermal.ThermalCapacityUnit
import squants.time.Time

object KilowattHoursPerKelvin extends ThermalCapacityUnit {
  val symbol = "J/K"
  val conversionFactor: Double = Time.MillisecondsPerHour
}
