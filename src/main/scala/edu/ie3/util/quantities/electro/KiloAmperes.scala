/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro

import squants.MetricSystem
import squants.electro.ElectricCurrentUnit

object KiloAmperes extends ElectricCurrentUnit {
  val symbol = "kA"
  val conversionFactor: Double = MetricSystem.Kilo
}
