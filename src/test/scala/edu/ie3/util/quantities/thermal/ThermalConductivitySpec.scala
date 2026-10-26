/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.Meters

class ThermalConductivitySpec extends AnyFlatSpec with Matchers {

  behavior of "ThermalConductivity and its Units of Measure"

  it should "create values using UOM factories" in {
    WattsPerMeterKelvin(
      1
    ).toWattsPerMeterKelvin shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = WattsPerMeterKelvin(1000)

    x.toWattsPerMeterKelvin shouldBe 1000
    x.toKilowattsPerMeterKelvin shouldBe 1
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    WattsPerMeterKelvin(1).toString(
      WattsPerMeterKelvin
    ) shouldBe "1.0 W/(m*K)"
  }

  it should "return a thermal conductance when multiplied by a length" in {
    WattsPerMeterKelvin(2.5) * Meters(4) shouldBe WattsPerKelvin(10)
  }
}
