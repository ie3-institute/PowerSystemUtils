/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.TagUtils.Keys.{building, highway, landuse}
import edu.ie3.util.osm.TagUtils.Values.{highWayValues, landUseValues}
import org.locationtech.jts.geom.Polygon

/** Container class that contains OpenStreetMap data for a specified area
  */
case class OsmModel(
    nodes: List[Node],
    ways: List[Way],
    relations: Option[List[Relation]],
    capturedArea: Polygon
)

object OsmModel {

  def extractBuildings(ways: List[Way]): List[ClosedWay] = {
    ways.collect { case way: ClosedWay if way.tags.contains(building) => way }
  }

  def extractHighWays(ways: List[Way]): List[Way] = {
    ways.filter(way => way.containsKeyValuePair(highway, highWayValues))
  }

  def extractLandUses(ways: List[Way]): List[ClosedWay] = {
    ways.collect {
      case way: ClosedWay if way.containsKeyValuePair(landuse, landUseValues) =>
        way
    }
  }

}
