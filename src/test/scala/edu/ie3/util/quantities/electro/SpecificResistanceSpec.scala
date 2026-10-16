/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.Meters
import squants.electro.Ohms
import squants.space.Kilometers

class SpecificResistanceSpec extends AnyFlatSpec with Matchers {

  behavior of "SpecificResistance and its Units of Measure"

  it should "create values using UOM factories" in {
    OhmsPerKilometer(1).toOhmsPerKilometer shouldBe 1d
    MilliohmsPerKilometer(1).toMilliohmsPerKilometer shouldBe 1d
    KiloohmsPerKilometer(1).toKiloohmsPerKilometer shouldBe 1d
    OhmsPerMeter(1).toOhmsPerMeter shouldBe 1d
    MilliohmsPerMeter(1).toMilliohmsPerMeter shouldBe 1d
    MicroohmsPerMeter(1).toMicroohmsPerMeter shouldBe 1d
  }

  it should "properly convert between per-kilometer units" in {
    val res = OhmsPerKilometer(1000)

    res.toMilliohmsPerKilometer shouldBe 1000000d
    res.toKiloohmsPerKilometer shouldBe 1d

    KiloohmsPerKilometer(2).toOhmsPerKilometer shouldBe 2000d
    MilliohmsPerKilometer(500).toOhmsPerKilometer shouldBe 0.5d
  }

  it should "properly convert between per-meter units" in {
    val res = OhmsPerMeter(1)

    res.toMilliohmsPerMeter shouldBe 1000d
    res.toMicroohmsPerMeter shouldBe 1e6

    MilliohmsPerMeter(500).toOhmsPerMeter shouldBe 0.5d
    MicroohmsPerMeter(2000).toOhmsPerMeter shouldBe 0.002d
  }

  it should "properly convert between per-kilometer and per-meter units" in {
    OhmsPerKilometer(1).toOhmsPerMeter shouldBe 0.001d
    OhmsPerMeter(1).toOhmsPerKilometer shouldBe 1000d

    MilliohmsPerKilometer(1).toMilliohmsPerMeter shouldBe 0.001d
    MicroohmsPerMeter(1000).toOhmsPerMeter shouldBe 0.001d
  }

  it should "treat per-kilometer and per-meter units that are physically equal as equal" in {
    KiloohmsPerKilometer(1).toOhmsPerMeter shouldBe 1d
    OhmsPerMeter(1).toKiloohmsPerKilometer shouldBe 1d

    MilliohmsPerMeter(1000).toOhmsPerMeter shouldBe 1d
    OhmsPerMeter(1).toMilliohmsPerMeter shouldBe 1000d

    MicroohmsPerMeter(1000).toMilliohmsPerMeter shouldBe 1d
    MilliohmsPerMeter(1).toMicroohmsPerMeter shouldBe 1000d
  }

  it should "calculate absolute electrical resistance when multiplied by length" in {
    (OhmsPerMeter(0.5) * Meters(10)) shouldBe Ohms(5.0)
    (OhmsPerKilometer(200) * Kilometers(2)) shouldBe Ohms(400.0)
    (MilliohmsPerMeter(10) * Meters(100)) shouldBe Ohms(1.0)
  }
}
