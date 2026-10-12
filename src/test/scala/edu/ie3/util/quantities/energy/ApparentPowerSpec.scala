/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.energy

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.{Amperes, Each}
import squants.electro.Volts
import squants.energy.Watts

class ApparentPowerSpec extends AnyFlatSpec with Matchers {

  behavior of "ApparentPower and its Units of Measure"

  it should "create values using UOM factories" in {
    Voltamperes(1).toVoltamperes shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = Voltamperes(1)
    x.toVoltamperes shouldBe 1
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    Voltamperes(1).toString(Voltamperes) shouldBe "1.0 VA"
  }

  it should "return ElectricCurrent when divided by ElectricPotential" in {
    Voltamperes(100) / Volts(10) shouldBe Amperes(10)
  }

  it should "return ElectricPotential when divided by ElectricCurrent" in {
    Voltamperes(100) / Amperes(10) shouldBe Volts(10)
  }

}
