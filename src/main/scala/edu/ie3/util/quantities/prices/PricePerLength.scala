/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.prices

import edu.ie3.util.quantities.prices.EnergyPrice.parse
import squants.energy.{KilowattHours, MegawattHours}
import squants.market.{Currency, EUR}
import squants.*

import scala.util.Try

/** Represents the price per distance.
  */
final class PricePerLength private (
    override val value: Double,
    override val unit: PricePerLengthUnit
) extends squants.Quantity[PricePerLength] {

  def *(that: Length): Currency = EUR(toEuroPerKilometer * that.toKilometers)

  def toEuroPerKilometer: Double = to(EuroPerKilometers)
}

object PricePerLength extends Dimension[PricePerLength] {
  def apply[A](n: A, unit: PricePerLengthUnit)(implicit num: Numeric[A]) =
    new PricePerLength(n, unit)

  override def name = "PricePerLength"

  override def primaryUnit: EuroPerKilometers.type = EuroPerKilometers
  override def siUnit: EuroPerKilometers.type = EuroPerKilometers
  override def units: Set[UnitOfMeasure[EnergyPrice]] = Set(EuroPerKilometers)
}

trait PricePerLengthUnit
    extends UnitOfMeasure[PricePerLength]
    with UnitConverter {
  override def apply[A](n: A)(implicit num: Numeric[A]): PricePerLength =
    PricePerLength(n, this)
}

object EuroPerKilometers extends EnergyPriceUnit with PrimaryUnit with SiUnit {
  override val symbol: String = EUR.symbol + "/km"
}
