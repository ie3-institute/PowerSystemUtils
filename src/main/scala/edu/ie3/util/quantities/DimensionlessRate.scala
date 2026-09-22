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

  def toPuPerHour: Double = to(PuPerHour)
  def toPercentPerHour: Double = to(PercentsPerHour)
}

object DimensionlessRate extends Dimension[DimensionlessRate] {
  def apply[A](n: A, unit: DimensionlessRateUnit)(implicit num: Numeric[A]) =
    new DimensionlessRate(num.toDouble(n), unit)
  def name = "DimensionlessRate"
  def primaryUnit: PuPerHour.type = PuPerHour
  def siUnit: PuPerHour.type = PuPerHour
  def units: Set[UnitOfMeasure[DimensionlessRate]] =
    Set(PuPerHour, PercentsPerHour)
}

trait DimensionlessRateUnit
    extends UnitOfMeasure[DimensionlessRate]
    with UnitConverter {
  def apply[A](n: A)(implicit num: Numeric[A]) = DimensionlessRate(n, this)
}

object PuPerHour extends DimensionlessRateUnit with PrimaryUnit with SiUnit {
  val symbol = "pu/h"
}

object PercentsPerHour extends DimensionlessRateUnit {
  val symbol: String = Percent.symbol + "/h"
  val conversionFactor: Double = Percent.conversionFactor
}
