/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.OsmEntities.ClosedWay
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.ZonedDateTime
import java.util.UUID

class OsmEntitiesSpec extends Matchers with AnyWordSpecLike {

  "A OsmEntitiy" should {
    val entity = ClosedWay(
      UUID.randomUUID(),
      1,
      ZonedDateTime.now(),
      Map(
        "shop" -> "supermarket",
        "building" -> "residential",
        "landuse" -> "meadow"
      ),
      List()
    )
    "check if the tags contains a specific key value pair" in {
      entity.containsKeyValuePair("shop", "supermarket") shouldBe true
      entity.containsKeyValuePair("supermarket", "shop") shouldBe false
      entity.containsKeyValuePair("building", "commercial") shouldBe false
      entity.containsKeyValuePair("building", "residential") shouldBe true
    }
    "check if the tags contains a specific key with a value within a given a set" in {
      val valueSet = Set("supermarket", "residential")
      entity.containsKeyValuePair("shop", valueSet) shouldBe true
      entity.containsKeyValuePair("building", valueSet) shouldBe true
      entity.containsKeyValuePair("landuse", valueSet) shouldBe false
    }
  }

}
