/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.prices

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.market.EUR
import squants.space.Kilometers

class PricePerLengthSpec extends AnyFlatSpec with Matchers {

  behavior of "PricePerLength and its Units of Measure"

  it should "create values using UOM factories" in {
    EuroPerKilometers(1).toEuroPerKilometer shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = EuroPerKilometers(1)
    x.toEuroPerKilometer shouldBe 1
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    EuroPerKilometers(1).toString(EuroPerKilometers) shouldBe "1.0 €/km"
  }

  it should "return Money when multiplied by Length" in {
    EuroPerKilometers(100) * Kilometers(10) shouldBe EUR(1000)
  }

}
