/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.OsmEntities.{ClosedWay, Node, Relation, Way}
import edu.ie3.util.osm.TagUtils.Keys.{building, highway, landuse}
import edu.ie3.util.osm.TagUtils.Values.{highWayValues, landUseValues}
import org.locationtech.jts.geom.Polygon

/** Container class that contains OpenStreetMap data for a specified area.
  *
  * @param nodes
  *   all nodes in captured area
  * @param ways
  *   all ways in captured area
  * @param relations
  *   all relations in captured area
  * @param capturedArea
  *   the considered area
  */
case class OsmModel(
    nodes: List[Node],
    ways: List[Way],
    relations: Option[List[Relation]],
    capturedArea: Polygon
)

object OsmModel {

  /** Extract buildings from a list of ways.
    *
    * @param ways
    *   the ways to extract from
    * @return
    *   all was that represent buildings
    */
  def extractBuildings(ways: List[Way]): List[ClosedWay] = {
    ways.collect { case way: ClosedWay if way.tags.contains(building) => way }
  }

  /** Extract high ways from a list of ways.
    *
    * @param ways
    *   the ways to extract from
    * @return
    *   all ways that represent highways
    */
  def extractHighways(ways: List[Way]): List[Way] = {
    ways.filter(way => way.containsKeyValuePair(highway, highWayValues))
  }

  /** Extract land uses from a list of ways
    *
    * @param ways
    *   the ways to extract from
    * @return
    *   all ways that represent land uses
    */
  def extractLandUses(ways: List[Way]): List[ClosedWay] = {
    ways.collect {
      case way: ClosedWay if way.containsKeyValuePair(landuse, landUseValues) =>
        way
    }
  }

}
