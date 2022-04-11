/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.OsmUtils
import edu.ie3.util.osm.OsmUtils.GeometryUtils.buildPolygon
import edu.ie3.util.osm.model.OsmContainer
import edu.ie3.util.osm.model.OsmEntity.Way.{ClosedWay, OpenWay}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.ZonedDateTime
import java.util.UUID
import scala.util.{Failure, Success}

class OsmUtilsSpec
    extends Matchers
    with AnyWordSpecLike
    with SimpleOsmTestData {

  "The OsmUtils" should {
    val wayA = ClosedWay(
      1,
      Vector.empty,
      Map(
        "shop" -> "supermarket"
      ),
      None
    )
    val wayB = ClosedWay(
      1,
      Vector.empty,
      Map(
        "building" -> "supermarket"
      ),
      None
    )
    val wayC = OpenWay(
      1,
      Vector.empty,
      Map(
        "building" -> "supermarket"
      ),
      None
    )

    "extract buildings correctly" in {
      val extracted = OsmUtils.extractBuildings(List(wayA, wayB, wayC))
      extracted.length shouldBe 2
      extracted contains allOf(wayB, wayC)
    }

    "extract highways correctly" in {
      val highwayA = wayA.copy(tags = Map("highway" -> "residential"))
      val highwayB = wayB.copy(tags = Map("highway" -> "unlisted"))
      val highwayC = wayC.copy(tags = Map("highway" -> "path"))
      OsmUtils.extractHighways(
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
      OsmUtils.extractLanduses(
        List(
          landuseA,
          landuseB,
          landuseC
        ),
        Some(Set("residential", "retail"))
      ) shouldBe List(landuseA, landuseB)
    }

    "build a polygon correctly" in {
      val maybePolygon = buildPolygon(closedWay, nodesMap)
      maybePolygon match {
        case Failure(exception) => fail("Polygon couldn't be built", exception)
        case Success(polygon) =>
          polygon.getCoordinates.length shouldBe 4
      }
    }
  }
}
