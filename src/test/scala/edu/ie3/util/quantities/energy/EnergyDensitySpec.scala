/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.energy

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EnergyDensitySpec extends AnyFlatSpec with Matchers {

  behavior of "EnergyDensity and its Units of Measure"

  it should "create values using UOM factories" in {
    KilowattHoursPerCubicMeter(1).toKilowattHoursPerCubicMeter shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = KilowattHoursPerCubicMeter(1)
    x.toKilowattHoursPerCubicMeter shouldBe 1
  }

  it should "properly convert to JoulesPerCubicMeter correctly" in {
    val x = KilowattHoursPerCubicMeter(1)
    x.toJoulesPerCubicMeter shouldBe 3.6e6
  }
}
