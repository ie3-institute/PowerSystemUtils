/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.prices

import edu.ie3.util.DoubleMatchers
import edu.ie3.util.quantities.SquantsMatchers
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.energy.{KilowattHours, WattHours}
import squants.market.{EUR, Money}

class EnergyPriceSpec
    extends AnyFlatSpec
    with Matchers
    with DoubleMatchers
    with SquantsMatchers {

  // testing tolerances
  given Double = 1e-10
  given Money = EUR(1e-10)

  behavior of "EnergyPrice and its Units of Measure"

  it should "create values using UOM factories" in {
    EuroPerWattHours(1).toEuroPerWattHour shouldBe 1
    EuroPerKilowattHours(1).toEuroPerKilowattHour shouldBe 1
    EuroPerMegawattHours(1).toEuroPerMegawattHour shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = EuroPerKilowattHours(1)
    x.toEuroPerKilowattHour shouldBe 1
    x.toEuroPerWattHour should approximate(1e-3)
    x.toEuroPerMegawattHour should approximate(1e3)
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    EuroPerWattHours(1).toString(
      EuroPerWattHours
    ) shouldBe "1.0 €/Wh"

    EuroPerKilowattHours(1).toString(
      EuroPerKilowattHours
    ) shouldBe "1.0 €/kWh"

    EuroPerMegawattHours(1).toString(
      EuroPerMegawattHours
    ) shouldBe "1.0 €/MWh"
  }

  it should "return Euro when multiplied with power" in {
    EuroPerKilowattHours(1) * KilowattHours(10) should
      approximate(EUR(10))

    EuroPerMegawattHours(80) * KilowattHours(100) should
      approximate(EUR(8))

    EuroPerWattHours(1) * WattHours(100) should
      approximate(EUR(100))
  }
}
