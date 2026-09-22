/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.prices

import squants.*
import squants.market.EUR

/** Represents the price per distance.
  */
final class PricePerLength private (
    override val value: Double,
    override val unit: PricePerLengthUnit
) extends squants.Quantity[PricePerLength] {

  val dimension: PricePerLength.type = PricePerLength

  def *(that: Length): Money = EUR(toEuroPerKilometer * that.toKilometers)

  def toEuroPerKilometer: Double = to(EuroPerKilometers)
}

object PricePerLength extends Dimension[PricePerLength] {
  def apply[A](n: A, unit: PricePerLengthUnit)(implicit num: Numeric[A]) =
    new PricePerLength(num.toDouble(n), unit)

  override def name = "PricePerLength"

  override def primaryUnit: EuroPerKilometers.type = EuroPerKilometers
  override def siUnit: EuroPerKilometers.type = EuroPerKilometers
  override def units: Set[UnitOfMeasure[PricePerLength]] = Set(
    EuroPerKilometers
  )
}

trait PricePerLengthUnit
    extends UnitOfMeasure[PricePerLength]
    with UnitConverter {
  override def apply[A](n: A)(implicit num: Numeric[A]): PricePerLength =
    PricePerLength(n, this)
}

object EuroPerKilometers
    extends PricePerLengthUnit
    with PrimaryUnit
    with SiUnit {
  val symbol: String = EUR.symbol + "/km"
}
