/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.exceptions.GeoException
import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.geo.GeoUtils.{
  buildPolygon,
  calcHaversine,
  equalAreaProjection
}
import edu.ie3.util.quantities.PowerSystemUnits.KILOMETRE
import edu.ie3.util.quantities.QuantityUtils.RichQuantityDouble
import org.locationtech.jts.geom.{Coordinate, LineString, Polygon}
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities

import javax.measure.quantity.{Area, Length}
import scala.math.abs
import scala.util.{Failure, Success, Try}

object RichGeometries {

  implicit class EarthCoordinate(coordinate: Coordinate) {

    /** Calculates the great circle disteance between two coordinates
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

    /** Checks if the coordinate lies between two coordinates a and b.
      *
      * @param a
      *   coordinate a
      * @param b
      *   coordinate b
      * @param epsilon
      *   permitted deviation
      * @return
      *   whether or not the coordinate lies between
      */
    def isBetween(
        a: Coordinate,
        b: Coordinate,
        epsilon: Double = 1e-11
    ): Boolean = {
      val distance = a.haversineDistance(b)
      val distancePassingMe = a
        .haversineDistance(coordinate)
        .add(coordinate.haversineDistance(b))
        .getValue
        .doubleValue
      if (abs(1 - (distancePassingMe / distance)) < epsilon) true
      else false
    }
  }

  implicit class EarthLineString(lineString: LineString) {

    /** Compute length of a [[LineString]] on earth's surface.
      *
      * @return
      *   the length in kilometre as a quantity
      */
    def haversineLength: ComparableQuantity[Length] = {
      val coordinates = lineString.getCoordinates.toVector
      val coordinateSize = coordinates.size
      coordinates.zipWithIndex
        .foldLeft(Quantities.getQuantity(0, KILOMETRE))((acc, current) => {
          if (current._2 < coordinateSize - 1) {
            val currentCoordinate = current._1
            val nextCoordinate = coordinates(current._2 + 1)
            acc.add(currentCoordinate.haversineDistance(nextCoordinate))
          } else acc
        })
    }
  }

  implicit class EarthPolygon(polygon: Polygon) {

    // Fixme: Is not precisely right because earths curvature - use equal area projection ?
    def intersect(polygonB: Polygon): Try[Set[Coordinate]] = {
      Try(polygon.intersection(polygonB)) match {
        case Failure(exception) =>
          Failure(
            new GeoException(
              s"Couldn't calculate intersection of polygons: ${polygon.toString} and ${polygonB.toString}. Reason:",
              exception
            )
          )
        case Success(geometry) => Success(geometry.getCoordinates.toSet)
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
        GeoUtils.equalAreaProjection(coordinate.y, coordinate.getX)
      )
      buildPolygon(projectedCoordinates.toList)
    }

  }
}
