/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.OsmEntities.ClosedWay
import org.locationtech.jts.geom.Point

object OsmUtils {

def isInsideLandUse(coordinate: Point, landuses: List[ClosedWay]): Boolean =
    landuses.exists(_.toPolygon.convexHull().contains(coordinate))

}
