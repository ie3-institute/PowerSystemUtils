/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ThermalResistivitySpec extends AnyFlatSpec with Matchers {

  behavior of "ThermalResistivity and its Units of Measure"

  it should "create values using UOM factories" in {
    KelvinMetersPerWatt(1).toKelvinMetersPerWatt shouldBe 1d
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    KelvinMetersPerWatt(2.5).toString(
      KelvinMetersPerWatt
    ) shouldBe "2.5 (K*m)/W"
  }
}
