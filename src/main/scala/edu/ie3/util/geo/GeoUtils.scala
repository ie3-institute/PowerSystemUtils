/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo

import edu.ie3.util.exceptions.GeoException
import org.apache.commons.lang3.ArrayUtils
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

import java.lang.Math._
import javax.measure.Quantity
import javax.measure.quantity.Length
import scala.collection.immutable.{SortedSet, TreeSet}
import scala.math.pow
import scala.util.{Failure, Success, Try}

object GeoUtils {
  val DEFAULT_GEOMETRY_FACTORY: GeometryFactory =
    new GeometryFactory(new PrecisionModel(), 4326)

  val EARTH_RADIUS: ComparableQuantity[Length] =
    Quantities.getQuantity(6378137.0, METRE);

  /** Convert a given [[LineString]] with at least two points into a 'safe to be
    * compared' [[LineString]] This is done by removing duplicates in the points
    * in the provided linestring as well as a small change of the start
    * coordinate if the linestring only consists of two coordinates. For details
    * on the bug inside [[LineString]] that is addressed here, see
    * https://github.com/locationtech/jts/issues/531
    *
    * @param lineString
    *   the linestring that should be checked and maybe converted to a 'safe to
    *   be compared' linestring
    * @return
    *   a 'safe to be compared' linestring
    */
  def buildSafeLineString(lineString: LineString): LineString =
    if (lineString.getCoordinates.length == 2)
      buildSafeLineStringBetweenPoints(
        lineString.getStartPoint,
        lineString.getEndPoint
      )
    // rebuild line with unique points
    else {
      val uniqueCoords: Array[Coordinate] = lineString.getCoordinates.distinct
      if (uniqueCoords.length == 1)
        buildSafeLineStringBetweenPoints(
          lineString.getStartPoint,
          lineString.getEndPoint
        )
      else DEFAULT_GEOMETRY_FACTORY.createLineString(uniqueCoords)
    }

  /** Build an instance of [[LineString]] between two points that is safe to be
    * compared even if the provided two points consist of exactly the same
    * coordinates. This is done by increasing the coordinate of the provided
    * Point `p1` by a small amount to make it different from Point `p2`. For
    * details on the bug inside [[LineString]] that is addressed here, see
    * https://github.com/locationtech/jts/issues/531
    *
    * @param p1
    *   start point of the linestring
    * @param p2
    *   end point of the linestring
    * @return
    *   a [[LineString]] between the provided points
    */
  def buildSafeLineStringBetweenPoints(p1: Point, p2: Point): LineString = {
    val safePoint1 = if (p1 == p2) buildSafePoint(p1) else p1
    DEFAULT_GEOMETRY_FACTORY.createLineString(
      safePoint1.getCoordinates ++ p2.getCoordinates
    )
  }

  /** Build an instance of [[LineString]] between two coordinates that is safe
    * to be compared even if the provided two coordinates are exactly the same
    * coordinates. This is done by increasing the coordinate of the provided
    * Point `code c1` by a small amount to make it different from Point `code
    * c2`. For details on the bug inside [[LineString]] that is addressed here,
    * see https://github.com/locationtech/jts/issues/531
    *
    * @param c1
    *   start coordinate of the linestring
    * @param c2
    *   end coordinate of the linestring
    * @return
    *   A safely build line string
    */
  def buildSafeLineStringBetweenCoords(
      c1: Coordinate,
      c2: Coordinate
  ): LineString = {
    val safeCoord1: Coordinate = if (c1 == c2) buildSafeCoord(c1) else c1
    DEFAULT_GEOMETRY_FACTORY.createLineString(
      ArrayUtils.addAll(Array[Coordinate](safeCoord1), c2)
    )
  }

  /** Adapt the provided point as described in buildSafeCoord ( Coordinate )}
    * and return a new, adapted instance of [[Point]]
    *
    * @param p1
    *   the point that should be adapted
    * @return
    *   the adapted point with a slightly changed coordinate
    */
  private def buildSafePoint(p1: Point): Point = {
    val safeCoord = buildSafeCoord(p1.getCoordinate)
    val safeCoordSeq = new CoordinateArraySequence(Array[Coordinate](safeCoord))
    new Point(safeCoordSeq, p1.getFactory)
  }

  /** Adapted [[Coordinate]] x, [[Coordinate]] y and [[Coordinate]] z of the
    * provided [[Coordinate]] by 1e-13 and return a new, adapted instance of
    * [[Coordinate]]
    *
    * @param coord
    *   the coordinate that should be adapted
    * @return
    *   the adapted coordinate with slightly changed x,y,z values
    */
  private def buildSafeCoord(coord: Coordinate): Coordinate = {
    val modVal: Double = 1e-13
    val p1X: Double = coord.getX + modVal
    val p1Y: Double = coord.getY + modVal
    val p1Z: Double = coord.getZ + modVal
    new Coordinate(p1X, p1Y, p1Z)
  }

  /** Calculates and orders the coordinate distances from a base coordinate to a
    * list of coordinates
    *
    * @param baseCoordinate
    *   the base coordinate
    * @param coordinates
    *   the coordinates to calculate the distance for
    * @return
    *   a sorted set of [[CoordinateDistance]]
    */
  def calcOrderedCoordinateDistances(
      baseCoordinate: Point,
      coordinates: Array[Point]
  ): SortedSet[CoordinateDistance] = {
    val coordinateDistances = coordinates.map(coordinate =>
      new CoordinateDistance(baseCoordinate, coordinate)
    )
    TreeSet(coordinateDistances: _*)
  }

  /** Calculates the great circle distance between two coordinates
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
    val r = EARTH_RADIUS
    val dLat = Quantities.getQuantity(toRadians(latB - latA), RADIAN)
    val dLon = Quantities.getQuantity(toRadians(longB - longA), RADIAN)
    val a = pow(sin(dLat.getValue.doubleValue / 2), 2) + cos(
      toRadians(latA)
    ) * cos(toRadians(latB)) * pow(sin(dLon.getValue.doubleValue / 2), 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    r.multiply(c)
  }

  /** Builds a convex hull from a set of latitude/longitude coordinates.
    *
    * @param coordinates
    *   the coordinates to consider
    * @return
    *   a Try of the resulting polygon
    */
  def buildConvexHull(coordinates: Set[Coordinate]): Try[Polygon] = {
    val projectedCoordinates = coordinates
    new ConvexHull(
      projectedCoordinates.toArray,
      DEFAULT_GEOMETRY_FACTORY
    ).getConvexHull match {
      case projectedPolygon: Polygon =>
        Success(buildPolygon(projectedPolygon.getCoordinates))
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

  def buildPoint(lat: Double, long: Double): Point = {
    buildPoint(buildCoordinate(lat, long))
  }

  def buildPoint(coordinate: Coordinate): Point = {
    DEFAULT_GEOMETRY_FACTORY.createPoint(coordinate)
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

  /** Builds a polygon from a List of coordinates. To build a Polygon the
    * coordinates have to form a closed ring which means that the first and last
    * coordinate have to be the same coordinate.
    *
    * @param coordinates
    *   the coordinates for building the polygon
    * @return
    *   a [[Polygon]]
    */
  def buildPolygon(coordinates: Array[Coordinate]): Polygon = {
    val arrayCoordinates = new CoordinateArraySequence(coordinates.toArray)
    val linearRing =
      new LinearRing(arrayCoordinates, DEFAULT_GEOMETRY_FACTORY)
    new Polygon(linearRing, Array[LinearRing](), DEFAULT_GEOMETRY_FACTORY)
  }

  /** Sinusoidal equal-area projection of latitude and longitude values to a 2d
    * surface. This is an approximation gets worse when lines become very long.
    *
    * Credits to Joe Kington
    * (https://stackoverflow.com/questions/4681737/how-to-calculate-the-area-of-a-polygon-on-the-earths-surface-using-python#:~:text=Basically%2C%20you%20just%20multiply%20the,the%20cosine%20of%20the%20latitude.)
    *
    * @param coordinate
    *   the coordinate to project
    * @return
    *   a projected Coordinate with values in metre
    */
  def equalAreaProjection(coordinate: Coordinate): Coordinate = {
    val lat = coordinate.getY
    val long = coordinate.getX
    val latDist =
      PI * (EARTH_RADIUS.to(METRE).getValue.doubleValue() / 180d)
    val y = lat * latDist
    val x = long * latDist * cos(toRadians(lat))
    new Coordinate(x, y)
  }

  /** Reverses the [[equalAreaProjection()]] and returns a coordinate on earths
    * surface
    *
    * @param coordinate
    *   the projected coordinate
    * @return
    *   the latitude longitude based coordinate
    */
  def reverseEqualAreaProjection(coordinate: Coordinate): Coordinate = {
    val latDist = PI * (EARTH_RADIUS.to(METRE).getValue.doubleValue() / 180d)
    val lat = coordinate.y / latDist
    val long = coordinate.x / (latDist * cos(toRadians(lat)))
    buildCoordinate(lat, long)
  }

  /** Draws a circle with a radius of the provided distance around the provided
    * center coordinates and returns the result as a drawable polygon (one point
    * per degree)
    *
    * Source: https://www.movable-type.co.uk/scripts/latlong.html "Destination
    * point given distance and bearing from start point"
    *
    * @param center
    *   coordinate of the circle's center
    * @param radius
    *   radius of the circle
    * @return
    *   a polygon without the center, but with all points of the circle
    */
  def buildCirclePolygon(
      center: Coordinate,
      radius: Quantity[Length]
  ): Polygon = {
    val centerLat = toRadians(center.y)
    val centerLon = toRadians(center.x)
    val d = radius.divide(EARTH_RADIUS).getValue.doubleValue
    val coordinates = (0 to 360)
      .map(angle => {
        val bearing = toRadians(angle)
        val latRad =
          asin(sin(centerLat) * cos(d) + cos(centerLat) * sin(d) * cos(bearing))
        val lonRad: Double = centerLon + atan2(
          sin(bearing) * sin(d) * cos(centerLat),
          cos(d) - sin(centerLat) * sin(latRad)
        )
        new Coordinate(lonRad.toDegrees, latRad.toDegrees)
      })
      .toArray
    buildPolygon(coordinates)
  }

}
