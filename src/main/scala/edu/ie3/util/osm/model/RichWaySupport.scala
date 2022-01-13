/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.osm.model.OsmEntity.{Node, Way}
import org.locationtech.jts.geom.{Coordinate, Polygon}
import cats.implicits.*
import edu.ie3.util.geo.RichGeometries.*
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.unit.Units

import javax.measure.quantity.Area

trait RichWaySupport {

  protected def _getNode: Long => Option[Node]

  protected def _getWay: Long => Option[Way]

  def wayAreaOnEarth(wayId: Long): Option[ComparableQuantity[Area]] =
    wayPolygon(wayId).map(_.calcAreaOnEarth.to(Units.SQUARE_METRE))

  def wayCentroid(wayId: Long): Option[Array[Coordinate]] =
    wayPolygon(wayId).map(_.getCentroid.getCoordinates)

  def wayPolygon(wayId: Long): Option[Polygon] =
    _wayPolygonCache
      .get(wayId)
      .orElse(
        _getWay(wayId).flatMap(way =>
          wayNodes(way)
            .map(buildPolygon)
            .map(polygon => {
              _wayPolygonCache.addOne(wayId -> polygon)
              polygon
            })
        )
      )

  private lazy val _wayPolygonCache: collection.mutable.Map[Long, Polygon] =
    collection.mutable.Map.empty[Long, Polygon]

  private lazy val _wayNodeCache: collection.mutable.Map[Long, Seq[Node]] =
    collection.mutable.Map.empty[Long, Seq[Node]]

  private def wayNodes(way: Way): Option[Seq[Node]] =
    _wayNodeCache
      .get(way.id)
      .orElse(
        way.nodes
          .collect(_getNode(_))
          .traverse(identity)
          .map(nodes => {
            _wayNodeCache.addOne(way.id -> nodes)
            nodes
          })
      )

  private def buildPolygon(nodes: Seq[Node]): Polygon =
    GeoUtils.buildPolygon(
      nodes
        .map(node => new Coordinate(node.longitude, node.latitude))
        .toArray
    )

}
