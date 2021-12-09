/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo

import edu.ie3.util.exceptions.GeoException
import edu.ie3.util.geo.GeoUtils.{
  DEFAULT_GEOMETRY_FACTORY,
  buildPolygon,
  calcHaversine,
  reverseEqualAreaProjection
}
import edu.ie3.util.quantities.QuantityUtils.RichQuantityDouble
import org.locationtech.jts.geom.{Coordinate, LineString, Point, Polygon}
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.METRE

import javax.measure.quantity.{Area, Length}
import scala.math.abs
import scala.util.{Failure, Success, Try}

object RichGeometries {

  implicit class GeoCoordinate(coordinate: Coordinate) {

    /** Calculates the great circle distance between two coordinates
      *
      * @param coordinateB
      *   coordinate b
      * @return
      *   the distance between the coordinates as a quantity
      */
    def haversineDistance(
        coordinateB: Coordinate
    ): ComparableQuantity[Length] = {
      calcHaversine(
        coordinate.getY,
        coordinate.getX,
        coordinateB.getY,
        coordinateB.getX
      )
    }

    /** Checks if the coordinate lies between two coordinates a and b by
      * comparing the distances between a and b with the sum of distances
      * between the coordinate and a and the coordinate and b
      *
      * @param a
      *   coordinate a
      * @param b
      *   coordinate b
      * @param epsilon
      *   permitted relative deviation
      * @return
      *   whether or not the coordinate lies between
      */
    def isBetween(
        a: Coordinate,
        b: Coordinate,
        epsilon: Double = 1e-12
    ): Boolean = {
      val distance = a.haversineDistance(b)
      val distancePassingMe = a
        .haversineDistance(coordinate)
        .add(coordinate.haversineDistance(b))
        .getValue
        .doubleValue
      if (
        abs(1 - (distancePassingMe / distance.getValue.doubleValue())) < epsilon
      ) true
      else false
    }

    def toPoint: Point = {
      DEFAULT_GEOMETRY_FACTORY.createPoint(coordinate)
    }
  }

  implicit class GeoLineString(lineString: LineString) {

    /** Compute length of a [[LineString]] on earth's surface.
      *
      * @return
      *   the length in kilometre as a quantity
      */
    def haversineLength: ComparableQuantity[Length] = {
      val coordinates = lineString.getCoordinates.toVector
      val coordinateSize = coordinates.size
      coordinates.zipWithIndex
        .foldLeft(Quantities.getQuantity(0, METRE))((acc, current) => {
          if (current._2 < coordinateSize - 1) {
            val currentCoordinate = current._1
            val nextCoordinate = coordinates(current._2 + 1)
            acc.add(currentCoordinate.haversineDistance(nextCoordinate))
          } else acc
        })
    }
  }

  implicit class GeoPolygon(polygon: Polygon) {

    // Fixme: Is this correct or should the function calculate the specific coordinates shared by the polygons
    /** Calculates intersection between polygons
      *
      * @param polygonB
      *   polygon with which to calculate the intersection
      * @return
      */
    def intersect(polygonB: Polygon): Try[Polygon] = {
      Try(
        polygon.intersection(polygonB.equalAreaProjection)
      ) match {
        case Failure(exception) =>
          Failure(
            new GeoException(
              s"Couldn't calculate intersection of polygons: ${polygon.toString} and ${polygonB.toString}. Reason:",
              exception
            )
          )
        case Success(polygon: Polygon) => Success(polygon)
      }
    }

    /** Calculates the area of a polygon on earth's surface.
      *
      * @return
      *   a Quantity of area in metre
      */
    def calcAreaOnEarth: ComparableQuantity[Area] = {
      equalAreaProjection.getArea.asSquareMetre
    }

    /** Does an equal area projection of the polygon onto a two-dimensional
      * surface to account for earth's curvature when calculating the polygon's
      * area.
      *
      * @return
      *   the projected polygon
      */
    def equalAreaProjection: Polygon = {
      val projectedCoordinates = polygon.getCoordinates.map(coordinate =>
        GeoUtils.equalAreaProjection(coordinate)
      )
      buildPolygon(projectedCoordinates)
    }

    /** Checks whether the polygon contains the coordinate. Uses "covers()"
      * insted of "contains()" so borders are included.
      *
      * @param coordinate
      *   the coordinate to check
      * @return
      *   whether the polygon contains the coordinate
      */
    def containsCoordinate(coordinate: Coordinate): Boolean = {
      polygon.covers(coordinate.toPoint)
    }

  }
}
