/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import com.typesafe.scalalogging.LazyLogging
import edu.ie3.util.exceptions.OsmException
import edu.ie3.util.geo.RichGeometries.RichCoordinate
import edu.ie3.util.osm.model.CommonOsmKey.{Building, Highway, Landuse}
import edu.ie3.util.osm.model.OsmEntity
import org.locationtech.jts.geom.{Coordinate, Point, Polygon}

import scala.collection.parallel.immutable.ParSeq
import scala.util.{Failure, Success, Try}

object OsmUtils extends LazyLogging {

  val par: ParOsmUtil.type = ParOsmUtil

  object ParOsmUtil {

    def extractBuildings(
        entities: ParSeq[OsmEntity],
        specificTagValues: Option[Set[String]] = None
    ): ParSeq[OsmEntity] =
      entitiesByKey(entities, Building.toString, specificTagValues)

    def extractLanduses(
        entities: ParSeq[OsmEntity],
        specificTagValues: Option[Set[String]] = None
    ): ParSeq[OsmEntity] =
      entitiesByKey(entities, Landuse.toString, specificTagValues)

    def extractHighways(
        entities: ParSeq[OsmEntity],
        specificTagValues: Option[Set[String]] = None
    ): ParSeq[OsmEntity] =
      entitiesByKey(entities, Highway.toString, specificTagValues)

    def entitiesByKey(
        entities: ParSeq[OsmEntity],
        osmKey: String,
        specificTagValues: Option[Set[String]] = None
    ): ParSeq[OsmEntity] =
      specificTagValues match {
        case Some(tagValues) =>
          entities.filter(_.hasKeyValuesPairOr(osmKey, tagValues))
        case None =>
          entities.filter(_.hasKey(osmKey))
      }

  }

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
