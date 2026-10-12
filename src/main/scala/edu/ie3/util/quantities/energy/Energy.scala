/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.energy

import squants.energy.{Energy, EnergyUnit, Watts}
import squants.{MetricSystem, PrimaryUnit}

/** Additional unit for [[Energy]].
  */
object VarHours extends EnergyUnit with PrimaryUnit {
  val symbol = "VArh"
}

/** Additional unit for [[Energy]].
  */
object KilovarHours extends EnergyUnit {
  val conversionFactor: Double = MetricSystem.Kilo
  val symbol = "kVArh"
}

/** Additional unit for [[Energy]].
  */
object MegavarHours extends EnergyUnit {
  val conversionFactor: Double = MetricSystem.Mega
  val symbol = "MVArh"
}
