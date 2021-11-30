package edu.ie3.util.quantities

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class FooBiDooSpec extends Matchers with AnyWordSpecLike {
  "Adding two things" should {
    "provide correct values" in {
      FooBiDoo.add(10, 5) shouldBe 5
    }
  }
  "Subtracting two things" should {
    "provide correct values" in {
      FooBiDoo.add(10, 5) shouldBe 15
    }
  }
}
