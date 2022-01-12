/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.osm.model.CommonOsmKey.{Building, Highway, Landuse}

object OsmUtil {

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
