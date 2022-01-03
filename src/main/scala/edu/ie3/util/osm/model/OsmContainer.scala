/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.osm.model.OsmEntity
import edu.ie3.util.osm.model.CommonOsmKey.{Building, Highway, Landuse}
import edu.ie3.util.osm.model.OsmEntity.{Node, Way, Relation}

import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.immutable.ParMap

final case class OsmContainer(
    nodes: Seq[Node],
    ways: Seq[Way],
    relations: Seq[Relation]
) {

  lazy val nodesMap: ParMap[Long, Node] =
    nodes.map(node => (node.id, node)).toMap.par

  def nodeFromId(osmId: Long): Option[Node] =
    nodesMap.get(osmId)

  def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]] =
    osmIds.map(nodesMap.get)

}

object OsmContainer {

  def extractBuildings(
      entities: Seq[OsmEntity],
      specificTagValues: Option[Set[String]] = None
  ): Seq[OsmEntity] =
    entitiesByKey(entities, Building.toString, specificTagValues)

  def extractLanduses(
      entities: Seq[OsmEntity],
      specificTagValues: Option[Set[String]] = None
  ): Seq[OsmEntity] =
    entitiesByKey(entities, Landuse.toString, specificTagValues)

  def extractHighways(
      entities: Seq[OsmEntity],
      specificTagValues: Option[Set[String]] = None
  ): Seq[OsmEntity] =
    entitiesByKey(entities, Highway.toString, specificTagValues)

  def entitiesByKey(
      entities: Seq[OsmEntity],
      osmKey: String,
      specificTagValues: Option[Set[String]] = None
  ): Seq[OsmEntity] =
    specificTagValues match {
      case Some(tagValues) =>
        entities.filter(_.hasKeyValuesPairOr(osmKey, tagValues))
      case None =>
        entities.filter(_.hasKey(osmKey))
    }

}
