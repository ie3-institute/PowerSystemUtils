/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.radio

import squants.*
import squants.radio.{Irradiance, IrradianceUnit, WattsPerSquareMeter}

/** Additional unit for [[Irradiance]].
  */
object KilowattsPerSquareMeter extends IrradianceUnit with SiUnit {
  val symbol: String = "k" + WattsPerSquareMeter.symbol
  val conversionFactor: Double = MetricSystem.Kilo
}
