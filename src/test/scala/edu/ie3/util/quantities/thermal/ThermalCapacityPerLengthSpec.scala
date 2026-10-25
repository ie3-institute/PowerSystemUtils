/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.thermal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.energy.KilowattHours
import squants.space.Meters
import squants.thermal.{Celsius, JoulesPerKelvin, Kelvin}

class ThermalCapacityPerLengthSpec extends AnyFlatSpec with Matchers {

  behavior of "ThermalCapacityPerLength and its Units of Measure"

  it should "create values using UOM factories" in {
    KilowattHoursPerMeterKelvin(1).toJoulesPerMeterKelvin shouldBe 3600000.0
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = JoulesPerMeterKelvin(3600000.0)

    x.toJoulesPerMeterKelvin shouldBe 3600000.0
    x.to(KilowattHoursPerMeterKelvin) shouldBe 1.0
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    JoulesPerMeterKelvin(1).toString(JoulesPerMeterKelvin) shouldBe "1.0 J/(mK)"
    KilowattHoursPerMeterKelvin(1).toString(
      KilowattHoursPerMeterKelvin
    ) shouldBe "1.0 kWh/(mK)"
  }

  it should "return Energy when multiplied by Temperature delta of 1 Kelvin and Length" in {
    JoulesPerMeterKelvin(3600000.0).calcEnergy(
      Kelvin(10),
      Kelvin(20),
      Meters(5)
    ) shouldBe KilowattHours(5.0)
  }

  it should "return Energy when multiplied by Temperature delta of 1 degree Celsius and Length" in {
    JoulesPerMeterKelvin(3600000.0).calcEnergy(
      Celsius(100),
      Celsius(101),
      Meters(5)
    ) shouldBe KilowattHours(5.0)
  }

  it should "return ThermalCapacity when multiplied by Length" in {
    JoulesPerMeterKelvin(1000.0) * Meters(5) shouldBe JoulesPerKelvin(5000.0)
  }
}
