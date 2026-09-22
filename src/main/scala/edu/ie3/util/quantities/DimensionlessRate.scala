/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import squants.*

final class DimensionlessRate private (
    val value: Double,
    val unit: DimensionlessRateUnit
) extends Quantity[DimensionlessRate] {

  def dimension: DimensionlessRate.type = DimensionlessRate

  def toPuPerHour: Double = to(PuPerHours)
  def toPercentPerHour: Double = to(PercentPerHours)
}

object DimensionlessRate extends Dimension[DimensionlessRate] {
  def apply[A](n: A, unit: DimensionlessRateUnit)(implicit num: Numeric[A]) =
    new DimensionlessRate(num.toDouble(n), unit)
  def name = "DimensionlessRate"
  def primaryUnit: PuPerHours.type = PuPerHours
  def siUnit: PuPerHours.type = PuPerHours
  def units: Set[UnitOfMeasure[DimensionlessRate]] =
    Set(PuPerHours, PercentPerHours)
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
