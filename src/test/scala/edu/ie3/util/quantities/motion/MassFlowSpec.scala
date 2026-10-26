/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.motion

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MassFlowSpec extends AnyFlatSpec with Matchers {

  behavior of "MassFlow and its Units of Measure"

  it should "create values using UOM factories" in {
    KilogramsPerSecond(
      1
    ).toKilogramsPerSecond shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = KilogramsPerSecond(1)

    x.toKilogramsPerSecond shouldBe 1
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    KilogramsPerSecond(1).toString(
      KilogramsPerSecond
    ) shouldBe "1.0 kg/s"
  }
}
