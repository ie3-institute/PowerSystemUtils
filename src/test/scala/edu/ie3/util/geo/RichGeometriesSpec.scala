/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo

import edu.ie3.util.geo.GeoUtils.{
  DEFAULT_GEOMETRY_FACTORY,
  buildCoordinate,
  buildPolygon,
  calcHaversine
}
import edu.ie3.util.geo.RichGeometries.{
  GeoCoordinate,
  GeoLineString,
  GeoPolygon
}
import edu.ie3.util.quantities.QuantityMatchers.equalWithTolerance
import edu.ie3.util.quantities.QuantityUtils.RichQuantityDouble
import org.locationtech.jts.geom.Coordinate
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.METRE

import scala.util.{Failure, Success}

class RichGeometriesSpec extends Matchers with AnyWordSpecLike {

  "A rich GeoCordinate" should {

    "calculate the haversine distance correctly" in {
      val latA = 37.87532764735112
      val longA = -122.25311279296875
      val latB = 37.87934174490509
      val longB = -122.2537350654602
      val coordinateA = buildCoordinate(latA, longA)
      val coordinateB = buildCoordinate(latB, longB)
      val expected = Quantities.getQuantity(450.18011568984845, METRE)
      val actual = coordinateA.haversineDistance(coordinateB)
      actual should equalWithTolerance(expected)
    }

    "check if a coordinate is between two others" in {
      val coordinateA = buildCoordinate(10, 5)
      val coordinateB = buildCoordinate(20, 5)
      val coordinateBetween = buildCoordinate(15, 5)
      val coordinateNotExactlyBetween = buildCoordinate(16, 5.005)
      val coordinateNotBetween = buildCoordinate(15, 6)

      coordinateBetween.isBetween(coordinateA, coordinateB) shouldBe true
      coordinateBetween.isBetween(coordinateB, coordinateA) shouldBe true
      coordinateNotExactlyBetween.isBetween(
        coordinateA,
        coordinateB
      ) shouldBe false
      coordinateNotExactlyBetween.isBetween(
        coordinateA,
        coordinateB,
        1e-5
      ) shouldBe true
      coordinateNotBetween.isBetween(coordinateB, coordinateA) shouldBe false
    }

    "transform to a point correctly" in {
      val coordinate = buildCoordinate(10, 5)
      val actual = coordinate.toPoint
      actual.getCoordinate.x shouldBe coordinate.getX +- 1e-12
      actual.getCoordinate.y shouldBe coordinate.getY +- 1e-12
    }
  }

  "A GeoLinestring" should {

    "calculate the aggregated length correctly" in {
      val coordinateA = buildCoordinate(10, 5)
      val coordinateB = buildCoordinate(11, 6)
      val coordinateC = buildCoordinate(12, 7)
      val lineString = DEFAULT_GEOMETRY_FACTORY.createLineString(
        Array(coordinateA, coordinateB, coordinateC)
      )
      lineString.haversineLength should equalWithTolerance(
        coordinateA
          .haversineDistance(coordinateB)
          .add(coordinateB.haversineDistance(coordinateC))
      )
    }
  }

  "A GeoPolygon" should {

    "calculate the intersection between polygons correctly" in {
      val aBottomLeft = buildCoordinate(0, 1)
      val aTopLeft = buildCoordinate(1, 1)
      val aTopRight = buildCoordinate(1, 3)
      val aBottomRight = buildCoordinate(0, 3)

      val bBottomLeft = buildCoordinate(0, 2)
      val bTopLeft = buildCoordinate(0.5, 2)
      val bTopRight = buildCoordinate(0.5, 4)
      val bBottomRight = buildCoordinate(0, 4)

      val polygonA = buildPolygon(
        Array(
          aBottomLeft,
          aTopLeft,
          aTopRight,
          aBottomRight,
          aBottomLeft
        )
      )

      val polygonB = buildPolygon(
        Array(
          bBottomLeft,
          bTopLeft,
          bTopRight,
          bBottomRight,
          bBottomLeft
        )
      )

      val expectedIntersection = Array(
        buildCoordinate(0.5, 3),
        bBottomLeft,
        bTopLeft,
        aBottomRight
      )

      polygonA.intersect(polygonB) match {
        case Failure(exception) => throw exception
        case Success(polygon) =>
          polygon.getCoordinates.foreach(intCoordinate =>
            expectedIntersection.exists(expected =>
              expected.equals2D(intCoordinate)
            ) shouldBe true
          )
      }
    }

    "calculate area on earth correctly" in {
      val coordinateA = new Coordinate(8.748497631269068, 51.72341137638795)
      val coordinateB = new Coordinate(8.76167264195022, 51.723225286136866)
      val coordinateC = new Coordinate(8.76240220280227, 51.715568337479546)
      val coordinateD = new Coordinate(8.74832596989211, 51.71546198184121)
      val polygon = DEFAULT_GEOMETRY_FACTORY.createPolygon(
        Array(coordinateA, coordinateB, coordinateC, coordinateD, coordinateA)
      )
      val actual = 813431.49.asSquareMetre
      polygon.calcAreaOnEarth
        .divide(actual)
        .getValue
        .doubleValue() shouldBe 1d +- 0.01
    }
  }
}
