/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo

import edu.ie3.util.osm.OsmEntities.{ClosedWay, Way}
import edu.ie3.util.quantities.PowerSystemUnits.KILOMETRE
import org.locationtech.jts.geom.{GeometryFactory, Point, PrecisionModel}
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.{METRE, RADIAN}

import java.lang.Math.{atan2, cos, sin, sqrt, toRadians}
import javax.measure.quantity.Length
import scala.math.pow

object GeoUtils {
  val DEFAULT_GEOMETRY_FACTORY: GeometryFactory =
    new GeometryFactory(new PrecisionModel(), 4326)

  val EARTH_RADIUS: ComparableQuantity[Length] =
    Quantities.getQuantity(6378137.0, METRE);

  /** Calculates the great circle disteance between two coordinates
    *
    * @param coordinateA
    *   coordinate a
    * @param coordinateB
    *   coordinate b
    * @return
    *   the distance between the coordinates as a quantity
    */
  def calcHaversine(
      coordinateA: Point,
      coordinateB: Point
  ): ComparableQuantity[Length] = {
    calcHaversine(
      coordinateA.getY,
      coordinateA.getX,
      coordinateB.getY,
      coordinateB.getX
    )
  }

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

  def isInsideLandUse(coordinate: Point, landuses: List[ClosedWay]): Boolean = {
    landuses.foreach(landuse =>
      if (landuse.toPolygon.convexHull().contains(coordinate)) return true
    )
    false
  }

}
