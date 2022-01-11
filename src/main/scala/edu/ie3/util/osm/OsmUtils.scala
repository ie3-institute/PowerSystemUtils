/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.geo.RichGeometries.RichCoordinate
import edu.ie3.util.osm.OsmEntities.ClosedWay
import org.locationtech.jts.geom.{Coordinate, Point}

object OsmUtils {

  def isInsideLandUse(coordinate: Point, landuses: List[ClosedWay]): Boolean =
    landuses.exists(_.toPolygon.convexHull().contains(coordinate))

  def isInsideLandUse(
      coordinate: Coordinate,
      landuses: List[ClosedWay]
  ): Boolean =
    isInsideLandUse(coordinate.toPoint, landuses)

}
