/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.geo.RichGeometries.RichCoordinate
import edu.ie3.util.osm.model.OsmEntity.Node
import edu.ie3.util.osm.model.OsmEntity.Way.ClosedWay
import edu.ie3.util.osm.model.OsmEntity.Way.ClosedWay.GeometricClosedWay
import org.locationtech.jts.geom.{Coordinate, Point, Polygon}

object OsmUtils {

  /** Determine, if a certain point lays within the convex hull of one of the
    * land use polygons
    *
    * @param point
    *   The point to check for
    * @param closedWays
    *   Land uses as closed way with already built polygon
    * @return
    *   true, if the point is within at least one land use
    */
  def isInsideLandUse(
      point: Point,
      closedWays: Seq[GeometricClosedWay]
  ): Boolean =
    closedWays.exists(_.polygon.convexHull().contains(point))

  @deprecated(
    "Use #isInsideLandUse(Coordinate, Seq[GeometricClosedWay]) instead"
  )
  def isInsideLandUse(
      coordinate: Point,
      landuses: Seq[ClosedWay],
      nodes: Seq[Node]
  ): Boolean =
    isInsideLandUse(coordinate, polygonsFromClosedWays(landuses, nodes))

  private def polygonsFromClosedWays(ways: Seq[ClosedWay], nodes: Seq[Node]) =
    ways.map(way => GeometricClosedWay(way, nodes))

  /** Determine, if a certain coordinate lays within the convex hull of one of
    * the land use polygons
    *
    * @param coordinate
    *   The coordinate to check for
    * @param landuses
    *   Land use polygons
    * @return
    *   true, if the point is within at least one land use
    */
  def isInsideLandUse(coordinate: Coordinate, landuses: Seq[Polygon]): Boolean =
    landuses.exists(_.convexHull().contains(coordinate.toPoint))

  @deprecated("Use #isInsideLandUse(Coordinate, Seq[Polygon]) instead")
  def isInsideLandUse(
      coordinate: Coordinate,
      landuses: Seq[ClosedWay],
      nodes: Seq[Node]
  ): Boolean =
    isInsideLandUse(coordinate.toPoint, landuses, nodes)

}
