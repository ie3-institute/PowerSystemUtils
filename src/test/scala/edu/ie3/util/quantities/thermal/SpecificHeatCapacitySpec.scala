/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SpecificHeatCapacitySpec extends AnyFlatSpec with Matchers {

  behavior of "SpecificHeatCapacity and its Units of Measure"

  it should "create values using UOM factories" in {
    KilowattHoursPerKilogramKelvin(
      1
    ).toKilowattHoursPerKilogramKelvin shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = KilowattHoursPerKilogramKelvin(1)

    x.toKilowattHoursPerKilogramKelvin shouldBe 1
    x.toJoulesPerKilogramKelvin shouldBe 3.6e6
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    KilowattHoursPerKilogramKelvin(1).toString(
      KilowattHoursPerKilogramKelvin
    ) shouldBe "1.0 kWh/(kg*K)"
  }
}
