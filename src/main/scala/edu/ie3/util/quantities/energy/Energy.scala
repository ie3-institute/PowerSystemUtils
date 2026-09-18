/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.energy

import squants.energy.{EnergyUnit, Watts}
import squants.{MetricSystem, PrimaryUnit}

object VarHours extends EnergyUnit with PrimaryUnit {
  val symbol = "VArh"
}

object KilovarHours extends EnergyUnit {
  val conversionFactor: Double = Watts.conversionFactor * MetricSystem.Kilo
  val symbol = "kVArh"
}

object MegavarHours extends EnergyUnit {
  val conversionFactor: Double = Watts.conversionFactor * MetricSystem.Mega
  val symbol = "MVArh"
}
