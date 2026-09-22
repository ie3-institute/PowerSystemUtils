/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro

import squants.MetricSystem
import squants.electro.ElectricalConductanceUnit

object NanoSiemens extends ElectricalConductanceUnit {
  val symbol = "nS"
  val conversionFactor: Double = MetricSystem.Nano
}

object MilliSiemens extends ElectricalConductanceUnit {
  val symbol = "mS"
  val conversionFactor: Double = MetricSystem.Milli
}
