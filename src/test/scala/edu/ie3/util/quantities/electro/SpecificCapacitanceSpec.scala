/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.electro
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.Meters
import squants.electro.Farads
import squants.space.Kilometers
class SpecificCapacitanceSpec extends AnyFlatSpec with Matchers {
  behavior of "SpecificCapacitance and its Units of Measure"
  it should "create values using UOM factories" in {
    FaradsPerKilometer(1).toFaradsPerKilometer shouldBe (1d)
    FaradPerMeter(1).toFaradPerMeter shouldBe (1d)
    MicrofaradsPerKilometer(1).toMicrofaradsPerKilometer shouldBe (1d)
    MillifaradsPerKilometer(1).toMillifaradsPerKilometer shouldBe (1d)
    KilofaradsPerKilometer(1).toKilofaradsPerKilometer shouldBe (1d)
  }
  it should "properly convert between per-kilometer units" in {
    val cap = FaradsPerKilometer(1)
    cap.toMicrofaradsPerKilometer shouldBe (1e6)
    cap.toMillifaradsPerKilometer shouldBe (1000d)
    cap.toKilofaradsPerKilometer shouldBe (0.001d)
    KilofaradsPerKilometer(2).toFaradsPerKilometer shouldBe (2000d)
    MicrofaradsPerKilometer(500).toFaradsPerKilometer shouldBe (5e-4)
  }
  it should "properly convert between per-kilometer and per-meter units" in {
    FaradsPerKilometer(1).toFaradPerMeter shouldBe (0.001d)
    FaradPerMeter(1).toFaradsPerKilometer shouldBe (1000d)
  }
  it should "calculate absolute capacitance when multiplied by length" in {
    (FaradPerMeter(0.5) * Meters(10)) shouldBe (Farads(5.0))
    (FaradsPerKilometer(200) * Kilometers(2)) shouldBe (Farads(400.0))
    (MicrofaradsPerKilometer(1000) * Kilometers(2)) shouldBe (Farads(0.002))
  }
}
