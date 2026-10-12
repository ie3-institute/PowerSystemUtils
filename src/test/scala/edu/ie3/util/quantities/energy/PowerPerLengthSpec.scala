/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.energy
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import squants.Meters
import squants.energy.Watts
import squants.space.Kilometers
class PowerPerLengthSpec extends AnyFlatSpec with Matchers {
  behavior of "PowerPerLength and its Units of Measure"
  it should "create values using UOM factories" in {
    WattsPerMeter(1).toWattsPerMeter should be(1d)
  }
  it should "calculate absolute power when multiplied by length" in {
    (WattsPerMeter(0.5) * Meters(10)) should be(Watts(5.0))
    (WattsPerMeter(200) * Kilometers(2)) should be(Watts(400000.0))
    (WattsPerMeter(1) * Meters(100)) should be(Watts(100.0))
  }
}
