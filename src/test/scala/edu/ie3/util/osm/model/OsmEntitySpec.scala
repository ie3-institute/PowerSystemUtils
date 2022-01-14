/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.osm.model.OsmEntity.Way.ClosedWay
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.ZonedDateTime
import java.util.UUID

class OsmEntitySpec extends Matchers with AnyWordSpecLike {

  "An OsmEntity" should {
    val entity = ClosedWay(
      1,
      Vector.empty,
      Map(
        "shop" -> "supermarket",
        "building" -> "residential",
        "landuse" -> "meadow"
      ),
      None
    )
    "check if the tags contains a specific key value pair" in {
      entity.hasKeyValuePair("shop", "supermarket") shouldBe true
      entity.hasKeyValuePair("supermarket", "shop") shouldBe false
      entity.hasKeyValuePair("building", "commercial") shouldBe false
      entity.hasKeyValuePair("building", "residential") shouldBe true
    }
    "check if the tags contains a specific key with a value within a given a set" in {
      val valueSet = Set("supermarket", "residential")
      entity.hasKeyValuesPairOr("shop", valueSet) shouldBe true
      entity.hasKeyValuesPairOr("building", valueSet) shouldBe true
      entity.hasKeyValuesPairOr("landuse", valueSet) shouldBe false
    }
  }

}
