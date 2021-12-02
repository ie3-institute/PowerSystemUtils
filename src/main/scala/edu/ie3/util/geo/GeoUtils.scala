/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo

import edu.ie3.util.exceptions.GeoException
import edu.ie3.util.quantities.PowerSystemUnits.KILOMETRE
import org.locationtech.jts.algorithm.ConvexHull
import org.locationtech.jts.geom.impl.CoordinateArraySequence
import org.locationtech.jts.geom.{
  Coordinate,
  GeometryCollection,
  GeometryFactory,
  LineString,
  LinearRing,
  Point,
  Polygon,
  PrecisionModel
}
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.{METRE, RADIAN}

import java.lang.Math.{atan2, cos, sin, sqrt, toRadians}
import javax.measure.Quantity
import javax.measure.quantity.Length
import scala.math.pow
import scala.util.{Failure, Success, Try}

object GeoUtils {
  val DEFAULT_GEOMETRY_FACTORY: GeometryFactory =
    new GeometryFactory(new PrecisionModel(), 4326)

  val EARTH_RADIUS: ComparableQuantity[Length] =
    Quantities.getQuantity(6378137.0, METRE);

  /** Calculates the great circle disteance between two coordinates
    *
    * @param latA
    *   latitude of coordinate a
    * @param longA
    *   longitude of coordinate a
    * @param latB
    *   latitude of coordinate b
    * @param longB
    *   longitude of coordinate b
    * @return
    *   the distance between the coordinates as a quantity
    */
  def calcHaversine(
      latA: Double,
      longA: Double,
      latB: Double,
      longB: Double
  ): ComparableQuantity[Length] = {
    val r = EARTH_RADIUS.to(KILOMETRE)
    val dLat = Quantities.getQuantity(toRadians(latB - latA), RADIAN)
    val dLon = Quantities.getQuantity(toRadians(longB - longA), RADIAN)
    val a = pow(sin(dLat.getValue.doubleValue / 2), 2) + cos(
      toRadians(latA)
    ) * cos(toRadians(latB)) * pow(sin(dLon.getValue.doubleValue / 2), 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    r.multiply(c)
  }

  /** Builds a convex hull from a set of coordinates.
    *
    * @param coordinates
    *   the coordinates to consider
    * @return
    *   a Try of the resulting polygon
    */
  def buildConvexHull(coordinates: Set[Coordinate]): Try[Polygon] = {
    new ConvexHull(
      coordinates.toArray,
      DEFAULT_GEOMETRY_FACTORY
    ).getConvexHull match {
      case polygon: Polygon => Success(polygon)
      case _: LineString =>
        Failure(
          new GeoException(
            s"Got a line string as a convex hull. Probably coordinates: $coordinates only contains two different coordinates."
          )
        )
      case _: Point =>
        Failure(
          new GeoException(
            s"Got a point as a convex hull. Probably coordinates: $coordinates only contains one coordinate."
          )
        )
      case _: GeometryCollection =>
        Failure(
          new GeoException(
            s"Got a GeometryCollection. Probably $coordinates was empty."
          )
        )
      case _ => Failure(new GeoException(s"Got an unexpected return type."))
    }
  }

  /** Build a coordinate from latitude and longitude values.
    *
    * @param lat
    *   latitude value
    * @param long
    *   longitude value
    * @return
    *   the built [[Coordinate]]
    */
  def buildCoordinate(lat: Double, long: Double): Coordinate = {
    new Coordinate(long, lat)
  }

  /** Builds a polygon from a List of coordinates
    *
    * @param coordinates
    *   the coordinates for building the polygon
    * @return
    *   a [[Polygon]]
    */
  def buildPolygon(coordinates: List[Coordinate]): Polygon = {
    val arrayCoordinates = new CoordinateArraySequence(coordinates.toArray)
    val linearRing =
      new LinearRing(arrayCoordinates, DEFAULT_GEOMETRY_FACTORY)
    new Polygon(linearRing, Array[LinearRing](), DEFAULT_GEOMETRY_FACTORY)
  }

  /** Credits to Joe Kington
    * (https://stackoverflow.com/questions/4681737/how-to-calculate-the-area-of-a-polygon-on-the-earths-surface-using-python#:~:text=Basically%2C%20you%20just%20multiply%20the,the%20cosine%20of%20the%20latitude.)
    * Sinusoidal equal-area projection of latitude and longitude values to a 2d
    * surface. This is an approximation gets worse when lines become very long
    *
    * @param lat
    *   latitude to project
    * @param long
    *   longitude to project
    * @return
    *   a projected Coordinate with values in metre
    */
  def equalAreaProjection(lat: Double, long: Double): Coordinate = {
    val latDist =
      Math.PI * (EARTH_RADIUS.to(METRE).getValue.doubleValue() / 180.0)
    val y = lat * latDist
    val x = long * latDist * cos(toRadians(lat))
    new Coordinate(x, y)
  }

  /** Draws a circle with a radius of the provided distance around the provided
    * center coordinates and returns the result as a drawable polygon (one point
    * per degree)
    *
    * @param center
    *   coordinate of the circle's center
    * @param radius
    *   radius of the circle
    * @return
    *   a polygon without the center, but with all points of the circle
    */
  def buildCircle(center: Coordinate, radius: Quantity[Length]): Polygon = {
    ???
  }

}
