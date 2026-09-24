/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro

import squants.MetricSystem
import squants.electro.ElectricalConductanceUnit

object Nanosiemens extends ElectricalConductanceUnit {
  val symbol = "nS"
  val conversionFactor: Double = MetricSystem.Nano
}

object Millisiemens extends ElectricalConductanceUnit {
  val symbol = "mS"
  val conversionFactor: Double = MetricSystem.Milli
}
