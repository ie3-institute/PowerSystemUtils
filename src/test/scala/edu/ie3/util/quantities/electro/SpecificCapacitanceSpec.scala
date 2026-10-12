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
    FaradsPerKilometer(1).toFaradsPerKilometer should be(1d)
    FaradPerMeter(1).toFaradPerMeter should be(1d)
    MicrofaradsPerKilometer(1).toMicrofaradsPerKilometer should be(1d)
    MillifaradsPerKilometer(1).toMillifaradsPerKilometer should be(1d)
    KilofaradsPerKilometer(1).toKilofaradsPerKilometer should be(1d)
  }
  it should "properly convert between per-kilometer units" in {
    val cap = FaradsPerKilometer(1)
    cap.toMicrofaradsPerKilometer should be(1e6)
    cap.toMillifaradsPerKilometer should be(1000d)
    cap.toKilofaradsPerKilometer should be(0.001d)
    KilofaradsPerKilometer(2).toFaradsPerKilometer should be(2000d)
    MicrofaradsPerKilometer(500).toFaradsPerKilometer should be(5e-4)
  }
  it should "properly convert between per-kilometer and per-meter units" in {
    FaradsPerKilometer(1).toFaradPerMeter should be(0.001d)
    FaradPerMeter(1).toFaradsPerKilometer should be(1000d)
  }
  it should "calculate absolute capacitance when multiplied by length" in {
    (FaradPerMeter(0.5) * Meters(10)) should be(Farads(5.0))
    (FaradsPerKilometer(200) * Kilometers(2)) should be(Farads(400.0))
    (MicrofaradsPerKilometer(1000) * Kilometers(2)) should be(Farads(0.002))
  }
}
