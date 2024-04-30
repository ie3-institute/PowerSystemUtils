/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.osm.SimpleOsmTestData
import edu.ie3.util.osm.model.OsmEntity.Way
import edu.ie3.util.osm.model.OsmEntity.Way.{ClosedWay, OpenWay}
import org.scalatest.Inside.inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class OsmEntitySpec
    extends Matchers
    with AnyWordSpecLike
    with SimpleOsmTestData {

  "An OsmEntity" should {
    val entity = ClosedWay(
      1,
      Vector.empty,
      Map(
        "shop" -> "supermarket",
        "building" -> "residential",
        "landuse" -> "meadow"
      ),
      None,
      Some(1)
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

  "A Way" should {
    "be built correctly as ClosedWay" in {
      val wayNodes = Seq(n1.id, n2.id, n3.id, n1.id)
      inside(Way(100L, wayNodes, Map.empty, None, Some(1))) {
        case ClosedWay(id, nodes, tags, metaInformation, Some(1)) =>
          id shouldBe 100L
          nodes shouldBe wayNodes
          tags shouldBe Map.empty
          metaInformation shouldBe None
      }
    }

    "be built correctly as OpenWay" in {
      val wayNodes = Seq(n1.id, n2.id, n3.id)
      inside(Way(100L, wayNodes, Map.empty, None, Some(1))) {
        case OpenWay(id, nodes, tags, metaInformation, Some(1)) =>
          id shouldBe 100L
          nodes shouldBe wayNodes
          tags shouldBe Map.empty
          metaInformation shouldBe None
      }

    }
  }

}
