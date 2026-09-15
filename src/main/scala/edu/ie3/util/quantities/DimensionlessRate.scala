/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import squants.*
import squants.time.{Frequency, TimeIntegral}

final class DimensionlessRate private (
    val value: Double,
    val unit: DimensionlessRateUnit
) extends Quantity[DimensionlessRate] {

  def toPuPerHour: Double = to(PuPerHours)
  def toPercentPerHour: Double = to(PercentPerHours)
}

object DimensionlessRate extends Dimension[DimensionlessRate] {
  def apply[A](n: A, unit: DimensionlessUnit)(implicit num: Numeric[A]) =
    new DimensionlessRate(n, unit)
  def name = "DimensionlessRate"
  def primaryUnit: PuPerHours.type = PuPerHours
  def siUnit: PuPerHours.type = PuPerHours
  def units: Set[DimensionlessRateUnit] = Set(PuPerHours, PercentPerHours)
}

trait DimensionlessRateUnit
    extends UnitOfMeasure[DimensionlessRate]
    with UnitConverter {
  def apply[A](n: A)(implicit num: Numeric[A]) = DimensionlessRate(n, this)
}

object PuPerHours extends DimensionlessRateUnit with PrimaryUnit with SiUnit {
  val symbol = "pu/h"
}

object PercentPerHours extends DimensionlessRateUnit {
  val symbol: String = Percent.symbol + "/h"
  val conversionFactor: Double = Percent.conversionFactor
}
