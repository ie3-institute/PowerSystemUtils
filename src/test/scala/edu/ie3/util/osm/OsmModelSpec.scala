/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.OsmEntities.{ClosedWay, OpenWay}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.ZonedDateTime
import java.util.UUID

class OsmModelSpec extends Matchers with AnyWordSpecLike {

  "The OsmModel" should {
    val wayA = ClosedWay(
      UUID.randomUUID(),
      1,
      ZonedDateTime.now(),
      Map(
        "shop" -> "supermarket"
      ),
      List()
    )
    val wayB = ClosedWay(
      UUID.randomUUID(),
      1,
      ZonedDateTime.now(),
      Map(
        "building" -> "supermarket"
      ),
      List()
    )
    val wayC = OpenWay(
      UUID.randomUUID(),
      1,
      ZonedDateTime.now(),
      Map(
        "building" -> "supermarket"
      ),
      List()
    )

    "extract buildings correctly" in {
      OsmModel.extractBuildings(List(wayA, wayB, wayC)) shouldBe List(wayB)
    }

    "extract highways correctly" in {
      val highwayA = wayA.copy(tags = Map("highway" -> "residential"))
      val highwayB = wayB.copy(tags = Map("highway" -> "unlisted"))
      val highwayC = wayC.copy(tags = Map("highway" -> "path"))
      OsmModel.extractHighways(
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
      OsmModel.extractLandUses(
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
