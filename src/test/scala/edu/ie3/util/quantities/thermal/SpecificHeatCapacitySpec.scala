/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.energy.KilowattHours
import squants.space.CubicMeters
import squants.thermal.{Celsius, Kelvin}

class SpecificHeatCapacitySpec extends AnyFlatSpec with Matchers {

  behavior of "SpecificHeatCapacity and its Units of Measure"

  it should "create values using UOM factories" in {
    KilowattHoursPerKelvinTimesCubicMeters(
      1
    ).toKilowattHoursPerKelvinTimesCubicMeters shouldBe 1
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = KilowattHoursPerKelvinTimesCubicMeters(1)
    x.toKilowattHoursPerKelvinTimesCubicMeters shouldBe 1
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    KilowattHoursPerKelvinTimesCubicMeters(1).toString(
      KilowattHoursPerKelvinTimesCubicMeters
    ) shouldBe "1.0 kWh/Km³"
  }

  it should "return Energy when multiplied by Temperature delta of 1 Kelvin and Volume" in {
    KilowattHoursPerKelvinTimesCubicMeters(1000).calcEnergy(
      Kelvin(10),
      Kelvin(20),
      CubicMeters(5)
    ) shouldBe KilowattHours(50000.0)
  }

  it should "return Energy when multiplied by Temperature delta of 1 degree Celsius and Volume" in {
    KilowattHoursPerKelvinTimesCubicMeters(1000).calcEnergy(
      Celsius(100),
      Celsius(101),
      CubicMeters(5)
    ) shouldBe KilowattHours(5000)
  }
}
