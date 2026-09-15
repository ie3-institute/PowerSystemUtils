/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.energy

import squants.{PrimaryUnit, SiUnit}
import squants.energy.{EnergyDensityUnit, WattHours}
import squants.space.CubicMeters

import scala.util.Try

object KilowattHoursPerCubicMeter
    extends EnergyDensityUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = "k" + WattHours.symbol + "/" + CubicMeters.symbol

}
