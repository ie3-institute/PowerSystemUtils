package edu.ie3.util.geo

import edu.ie3.util.geo.GeoUtils.DEFAULT_GEOMETRY_FACTORY
import edu.ie3.util.geo.RichGeometries.RichCoordinate
import org.locationtech.jts.geom.Coordinate
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class GeoUtilsSpec extends Matchers with AnyWordSpecLike{

  "The GeoUtils" should {
    "build a line string with two equal geo coordinates, avoiding the known bug in jts geometry" in {
      val coordinate = new Coordinate(7.411111, 51.49228)
      val coordinateWithCorrection = new Coordinate(7.4111110000001, 51.4922800000001)
      val fromCoords = GeoUtils.buildSafeLineStringBetweenCoords(coordinate, coordinate).getCoordinates
      val fromPoints = GeoUtils.buildSafeLineStringBetweenPoints(coordinate.toPoint, coordinate.toPoint).getCoordinates
      val fromLineString = GeoUtils.buildSafeLineString(DEFAULT_GEOMETRY_FACTORY.createLineString(Array[Coordinate](coordinate, coordinate)))
      fromCoords(0) == coordinateWithCorrection
      fromCoords(1) == coordinate
    }
  }

}
