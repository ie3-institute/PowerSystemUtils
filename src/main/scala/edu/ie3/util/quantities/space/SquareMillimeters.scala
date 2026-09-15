/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.quantities.space

import squants.{MetricSystem, PrimaryUnit, SiUnit}
import squants.energy.{EnergyDensityUnit, WattHours}
import squants.space.{AreaUnit, CubicMeters}

import scala.util.Try

object SquareMillimeters extends AreaUnit {
  val symbol: String = "mm²"
  val conversionFactor: Double = MetricSystem.Milli * MetricSystem.Milli
}
