/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.motion

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.motion.Pascals
import squants.time.Seconds

class DynamicViscositySpec extends AnyFlatSpec with Matchers {

  behavior of "DynamicViscosity and its Units of Measure"

  it should "create values using UOM factories" in {
    PascalSeconds(
      1
    ).toPascalSeconds shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = PascalSeconds(1)

    x.toPascalSeconds shouldBe 1
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    PascalSeconds(1).toString(
      PascalSeconds
    ) shouldBe "1.0 Pa*s"
  }

  it should "return a pressure when divided by a time" in {
    PascalSeconds(10) / Seconds(5) shouldBe Pascals(2)
  }

}
