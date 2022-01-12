/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.osm.model.OsmContainer
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.ClosedWay.SimpleClosedWay
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.OpenWay.SimpleOpenWay
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.{ClosedWay, OpenWay}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.ZonedDateTime
import java.util.UUID

class OsmContainerSpec extends Matchers with AnyWordSpecLike {

  "The OsmContainer" should {
    val wayA = SimpleClosedWay(
      1,
      Vector.empty,
      Map(
        "shop" -> "supermarket"
      ),
      None
    )
    val wayB = SimpleClosedWay(
      1,
      Vector.empty,
      Map(
        "building" -> "supermarket"
      ),
      None
    )
    val wayC = SimpleOpenWay(
      1,
      Vector.empty,
      Map(
        "building" -> "supermarket"
      ),
      None
    )

    "extract buildings correctly" in {
      OsmUtil.extractBuildings(List(wayA, wayB, wayC)) shouldBe List(wayB)
    }

    "extract highways correctly" in {
      val highwayA = wayA.copy(tags = Map("highway" -> "residential"))
      val highwayB = wayB.copy(tags = Map("highway" -> "unlisted"))
      val highwayC = wayC.copy(tags = Map("highway" -> "path"))
      OsmUtil.extractHighways(
        List(
          highwayA,
          highwayB,
          highwayC
        ),
        Some(Set("residential", "path"))
      ) shouldBe List(highwayA, highwayC)
    }

    "extract landuse correctly" in {
      val landuseA = wayA.copy(tags = Map("landuse" -> "residential"))
      val landuseB = wayA.copy(tags = Map("landuse" -> "retail"))
      val landuseC = wayA.copy(tags = Map("landuse" -> "unlisted"))
      OsmUtil.extractLanduses(
        List(
          landuseA,
          landuseB,
          landuseC
        ),
        Some(Set("residential", "retail"))
      ) shouldBe List(landuseA, landuseB)
    }
  }
}
