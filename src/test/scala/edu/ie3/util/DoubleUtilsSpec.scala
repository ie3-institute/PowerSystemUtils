/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import edu.ie3.util.DoubleUtils.*

class DoubleUtilsSpec extends Matchers with AnyWordSpecLike {
  "Utilizing the rich class to compare double values" when {
    "using the positive operator" should {
      "detect sensibly similar values" in {
        val lhs = 0.3
        val rhs = 0.3000001

        implicit val precision: Double = 1e-6

        (lhs ~= rhs) shouldBe true
        (rhs ~= lhs) shouldBe true
      }

      "detect differing values" in {
        val lhs = 0.3
        val rhs = 0.300001

        implicit val precision: Double = 1e-6

        (lhs ~= rhs) shouldBe false
        (rhs ~= lhs) shouldBe false
      }
    }
    "using the negated operator" should {
      "detect sensibly similar values" in {
        val lhs = 0.3
        val rhs = 0.3000001

        implicit val precision: Double = 1e-6

        (lhs !~= rhs) shouldBe false
        (rhs !~= lhs) shouldBe false
      }

      "detect differing values" in {
        val lhs = 0.3
        val rhs = 0.300001

        implicit val precision: Double = 1e-6

        (lhs !~= rhs) shouldBe true
        (rhs !~= lhs) shouldBe true
      }
    }
  }
}
