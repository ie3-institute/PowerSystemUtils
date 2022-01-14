/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import com.typesafe.scalalogging.LazyLogging
import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.osm.model.OsmEntity.{Node, Way}
import org.locationtech.jts.geom.{Coordinate, Point, Polygon}
import edu.ie3.util.geo.RichGeometries.*
import edu.ie3.util.osm.model.OsmEntity.Way.ClosedWay
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.unit.Units

import javax.measure.quantity.Area

/** Trait to support calculations for ways located inside a [[OsmContainer]]
  * instance
  */
trait RichClosedWaySupport extends WayCache with LazyLogging {

  /** Calculate the area a [[ClosedWay]] covers on earth. If the [[Way]] is not
    * closed or the area cannot be calculated, [[None]] is returned
    *
    * @param wayId
    *   the identifier of the way of interested
    * @return
    *   the optional area of the provided way
    */
  def wayAreaOnEarth(wayId: Long): Option[ComparableQuantity[Area]] =
    wayPolygon(wayId).map(_.calcAreaOnEarth.to(Units.SQUARE_METRE))

  /** Calculate the area a [[ClosedWay]] covers on earth. If the area cannot be
    * calculated, [[None]] is returned
    *
    * @param way
    *   the way of interested
    * @return
    *   the optional area of the provided way
    */
  def wayAreaOnEarth(way: ClosedWay): Option[ComparableQuantity[Area]] =
    wayAreaOnEarth(way.id)

  /** Get the centroid of a [[ClosedWay]]. If the [[Way]] is not closed or the
    * centroid cannot be calculated, [[None]] is returned
    *
    * @param wayId
    *   the identifier of the way of interested
    * @return
    *   the optional centroid of the provided way
    */
  def wayCentroid(wayId: Long): Option[Point] =
    wayPolygon(wayId).map(_.getCentroid)

  /** Get the centroid of a [[ClosedWay]]. If the [[Way]] is not closed or the
    * centroid cannot be calculated, [[None]] is returned
    *
    * @param way
    *   the way of interested
    * @return
    *   the optional centroid of the provided way
    */
  def wayCentroid(way: ClosedWay): Option[Point] =
    wayCentroid(way.id)

  /** Checks if the provided point is covered by the area of the provided
    * [[ClosedWay]] identifier. If the way polygon cannot be calculated,
    * [[None]] is returned
    *
    * @param point
    *   the point that should be checked
    * @param wayId
    *   the identifier of the closed way that may contains the provided point
    * @return
    *   true if the is located within the provided way, false if the way is not
    *   closed or the point is not covered
    */
  def wayAreaCovers(point: Point, wayId: Long): Boolean =
    wayPolygon(wayId).exists(_.covers(point))

  /** Checks if the provided point is covered by the area of the provided
    * [[ClosedWay]]
    *
    * @param point
    *   the point that should be checked
    * @param way
    *   the closed way that may contains the provided point
    * @return
    *   true if the is located within the provided way, false otherwise
    */
  def wayAreaCovers(point: Point, way: ClosedWay): Boolean =
    wayAreaCovers(point, way.id)

  /** Get the optional polygon of a [[ClosedWay]]. If the [[Way]] is not closed
    * or the polygon cannot be constructed, [[None]] is returned
    *
    * @param wayId
    *   the identifier of the way of interested
    * @return
    *   the optional polygon of the provided way
    */
  def wayPolygon(wayId: Long): Option[Polygon] =
    _wayPolygonCache
      .safeGet(wayId)
      .orElse(
        _getWay(wayId).flatMap(way =>
          way match {
            case Way.OpenWay(id, nodes, tags, metaInformation) =>
              logger
                .error(s"Cannot create polygon for OpenWay with id '$wayId'!")
              None
            case closedWay: Way.ClosedWay =>
              wayNodes(way)
                .map(buildPolygon)
                .map(polygon => {
                  _wayPolygonCache.putIfAbsent(wayId, polygon)
                  polygon
                })
          }
        )
      )

  private lazy val _wayPolygonCache =
    java.util.concurrent.ConcurrentHashMap[Long, Polygon]()

  private def buildPolygon(nodes: Seq[Node]): Polygon =
    GeoUtils.buildPolygon(
      nodes
        .map(node => new Coordinate(node.longitude, node.latitude))
        .toArray
    )

}
