/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import com.typesafe.scalalogging.LazyLogging
import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.osm.model.OsmEntity.{Node, Way}
import org.locationtech.jts.geom.{Coordinate, Polygon}
import edu.ie3.util.geo.RichGeometries.*
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.unit.Units

import javax.measure.quantity.Area

trait RichClosedWaySupport extends WayCache with LazyLogging {

  def wayAreaOnEarth(wayId: Long): Option[ComparableQuantity[Area]] =
    wayPolygon(wayId).map(_.calcAreaOnEarth.to(Units.SQUARE_METRE))

  def wayCentroid(wayId: Long): Option[Array[Coordinate]] =
    wayPolygon(wayId).map(_.getCentroid.getCoordinates)

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
