/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo

import edu.ie3.util.geo.GeoUtils.{
  DEFAULT_GEOMETRY_FACTORY,
  buildCirclePolygon,
  buildConvexHull,
  buildPoint,
  buildPolygon,
  calcHaversine,
  calcOrderedCoordinateDistances,
  equalAreaProjection,
  reverseEqualAreaProjection
}
import edu.ie3.util.geo.RichGeometries.RichCoordinate
import edu.ie3.util.quantities.QuantityMatchers.equalWithTolerance
import org.locationtech.jts.geom.Coordinate
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.METRE

import scala.math.abs
import scala.util.{Failure, Success}

class GeoUtilsSpec extends Matchers with AnyWordSpecLike {

  "The GeoUtils" should {
    "build a line string with two equal geo coordinates, avoiding the known bug in jts geometry" in {
      val coordinate = new Coordinate(7.411111, 51.49228)
      val coordinateWithCorrection =
        new Coordinate(7.4111110000001, 51.4922800000001)
      val fromCoords = GeoUtils
        .buildSafeLineStringBetweenCoords(coordinate, coordinate)
        .getCoordinates
      val fromPoints = GeoUtils
        .buildSafeLineStringBetweenPoints(
          coordinate.toPoint,
          coordinate.toPoint
        )
        .getCoordinates
      val fromLineString = GeoUtils
        .buildSafeLineString(
          DEFAULT_GEOMETRY_FACTORY.createLineString(
            Array[Coordinate](coordinate, coordinate)
          )
        )
        .getCoordinates
      fromCoords(0) == coordinateWithCorrection
      fromCoords(1) == coordinate
      fromPoints(0) == coordinateWithCorrection
      fromPoints(1) == coordinate
      fromLineString(0) == coordinateWithCorrection
      fromLineString(1) == coordinate
    }

    "strip a line string to its distinct elements when building a safe one" in {
      val coordinateA = new Coordinate(7.411111, 51.49228)
      val coordinateB = new Coordinate(7.411111, 52.49228)
      val safeLineString = GeoUtils.buildSafeLineString(
        DEFAULT_GEOMETRY_FACTORY.createLineString(
          Array[Coordinate](coordinateA, coordinateA, coordinateB, coordinateB)
        )
      )
      val strippedCoordinates = safeLineString.getCoordinates
      strippedCoordinates.length shouldBe 2
      strippedCoordinates(0) == coordinateA
      strippedCoordinates(1) == coordinateB
    }

    "calculate ordered coordinate distances correctly" in {
      val base = buildPoint(51.53651260695586, 9.934043825403268)
      val nearest = buildPoint(51.553166628160746, 9.968032777276226)
      val middle = buildPoint(51.44074066208236, 10.039100585737868)
      val furthest = buildPoint(51.6273949370414, 10.341911247878777)
      val actual =
        calcOrderedCoordinateDistances(base, List(middle, furthest, nearest))
      val list = actual.toList
      list.size shouldBe 3
      list.head.to shouldBe nearest
      list(1).to shouldBe middle
      list(2).to shouldBe furthest
    }

    "calculate the haversine distance correctly" in {
      val latA = 37.87532764735112
      val longA = -122.25311279296875
      val latB = 37.87934174490509
      val longB = -122.2537350654602
      val expected = Quantities.getQuantity(450.18011568984845, METRE)
      val actual = calcHaversine(latA, longA, latB, longB)
      actual should equalWithTolerance(expected)
    }

    "build a convex hull correctly" in {
      val topLeft = new Coordinate(7, 50)
      val betweenTlTr = new Coordinate(7.5, 50)
      val topRight = new Coordinate(8, 50)
      val bottomRight = new Coordinate(8, 48)
      val betweenBlBr = new Coordinate(7.5, 48)
      val bottomLeft = new Coordinate(7, 48)
      val coordinates = List(
        topLeft,
        betweenTlTr,
        topRight,
        bottomRight,
        betweenBlBr,
        bottomLeft
      )
      val tryConvexHull = buildConvexHull(coordinates)
      tryConvexHull match {
        case Failure(exception) => throw exception
        case Success(polygon) =>
          val hullCoordinates = polygon.getCoordinates
          // contains all corner coordinates and filters out all colinear ones
          hullCoordinates.size shouldBe 5
          hullCoordinates(0).equals2D(topLeft, 1e-10)
          hullCoordinates(1).equals(topRight, 1e-10) shouldBe true
          hullCoordinates(2).equals(bottomRight, 1e-10) shouldBe true
          hullCoordinates(3).equals(bottomLeft, 1e-10) shouldBe true
          hullCoordinates(4).equals(topLeft, 1e-10) shouldBe true
      }
    }

    "build a coordinate correctly" in {
      val lat = 51.49860455457335
      val long = 7.468448342940863
      val actual = GeoUtils.buildCoordinate(lat, long)
      actual.getX shouldBe long
      actual.getY shouldBe lat
    }

    "build a polygon correctly" in {
      val coordinateA = new Coordinate(7.468448342940863, 51.49860455457335)
      val coordinateB = new Coordinate(7.521007845835815, 51.50450661471354)
      val coordinateC = new Coordinate(7.5598606548385545, 51.456498140367934)
      val actual =
        buildPolygon(List(coordinateA, coordinateB, coordinateC, coordinateA))
      actual.getCoordinates shouldBe Array(
        coordinateA,
        coordinateB,
        coordinateC,
        coordinateA
      )
    }

    "do an equal area projection of a coordinate correctly" in {
      val actual = equalAreaProjection(
        new Coordinate(8.748497631269068, 51.72341137638795)
      )
      actual.getX shouldBe 603277.0126920443 +- 1e-12
      actual.getY shouldBe 5757823.816510521 +- 1e-12
    }

    "reverse the equal area projection correctly" in {
      val original = new Coordinate(7.468448342940863, 51.49860455457335)
      val reversed = reverseEqualAreaProjection(equalAreaProjection(original))
      original.getX shouldBe reversed.getX +- 1e-12
      original.getY shouldBe reversed.getY +- 1e-12
    }

    "build a circle polygon correctly" in {
      val center = new Coordinate(51.49860455457335, 7.468448342940863)
      val radius = Quantities.getQuantity(50, METRE)
      val actualCoordinates = buildCirclePolygon(center, radius).getCoordinates
      implicit val precision: Double = 1e-6
      actualCoordinates.length shouldBe 361
      abs(actualCoordinates(90).y - center.y) shouldBe 0.0 +- precision
      actualCoordinates(90).haversineDistance(center) should equalWithTolerance(
        radius
      )
      abs(actualCoordinates(180).x - center.x) shouldBe 0.0 +- precision
      actualCoordinates(180).haversineDistance(
        center
      ) should equalWithTolerance(radius)
      abs(actualCoordinates(270).y - center.y) shouldBe 0.0 +- precision
      actualCoordinates(270).haversineDistance(
        center
      ) should equalWithTolerance(radius)
      abs(actualCoordinates(360).x - center.x) shouldBe 0.0 +- precision
      actualCoordinates(360).haversineDistance(
        center
      ) should equalWithTolerance(radius)
    }

  }

}
