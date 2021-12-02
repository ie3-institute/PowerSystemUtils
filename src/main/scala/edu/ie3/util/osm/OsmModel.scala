/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.OsmEntities.{ClosedWay, Node, Relation, Way}
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

  object KeyUtils {
    val BUILDING = "building"
    val HIGHWAY = "highway"
    val LANDUSE = "landuse"
  }

  /** Convenience method to extract buildings from a list of ways
    *
    * @param ways
    *   the ways to extract from
    * @param specificValues
    *   only match values within the Set when given otherwise match all
    */
  def extractBuildings(
      ways: List[Way],
      specificValues: Option[Set[String]] = None
  ): List[ClosedWay] = {
    extractClosedWays(ways, KeyUtils.BUILDING, specificValues)
  }

  /** Convenience method to extract land uses from a list of ways
    *
    * @param ways
    *   the ways to extract from
    * @param specificValues
    *   only match values within the Set when given otherwise match all
    */
  def extractLandUses(
      ways: List[Way],
      specificValues: Option[Set[String]] = None
  ): List[ClosedWay] = {
    extractClosedWays(ways, KeyUtils.LANDUSE, specificValues)
  }

  /** Extract closed ways via their tags.
    *
    * @param ways
    *   the ways to extract from
    * @param key
    *   the key of the tag to match
    * @param specificValues
    *   only match values within the Set when given otherwise match all
    */
  def extractClosedWays(
      ways: List[Way],
      key: String,
      specificValues: Option[Set[String]] = None
  ): List[ClosedWay] = {
    specificValues match {
      case Some(values) =>
        ways.collect {
          case way: ClosedWay if way.containsKeyValuePair(key, values) =>
            way
        }
      case None =>
        ways.collect {
          case way: ClosedWay if way.tags.contains(KeyUtils.BUILDING) => way
        }
    }
  }

  /** Convenience method to extract highways from a list of ways
    *
    * @param ways
    *   the ways to extract from
    * @param specificValues
    *   only match values within the Set when given otherwise match all
    */
  def extractHighways(
      ways: List[Way],
      specificValues: Option[Set[String]]
  ): List[Way] = {
    extractOpenWays(ways, KeyUtils.HIGHWAY, specificValues)
  }

  /** Extract open ways via their tags.
    *
    * @param ways
    *   the ways to extract from
    * @param key
    *   the key of the tag to match
    * @param specificValues
    *   only match values within the Set when given otherwise match all
    */
  def extractOpenWays(
      ways: List[Way],
      key: String,
      specificValues: Option[Set[String]] = None
  ): List[Way] = {
    specificValues match {
      case Some(values) =>
        ways.filter(way => way.containsKeyValuePair(key, values))
      case None => ways.filter(way => way.tags.contains(key))
    }
  }

}
