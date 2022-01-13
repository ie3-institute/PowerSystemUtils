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
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.RelationMember.{
  ExtendedRelationMember,
  SimpleRelationMember
}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.{
  RelationMemberType,
  SimpleRelation
}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.{ExtendedWay, SimpleWay}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.ClosedWay.ExtendedClosedWay
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.ExtendedWay
import edu.ie3.util.osm.model.OsmEntity.{ComposedEntity, Node}
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

  /** Checks if the provided point is covered by the area of the provided way
    *
    * @param point
    *   the point that should be checked
    * @param closedWay
    *   the closed way that may contains the provided point
    * @return
    *   true if the is located within the provided way, false otherwise
    */
  def wayAreaCovers(point: Point, closedWay: ExtendedClosedWay): Boolean =
    closedWay.polygon.covers(point)

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

  def extendedWay(
      simpleWay: SimpleWay,
      nodesToBeConsidered: Map[Long, Node]
  ): Try[ComposedEntity.ExtendedWay] =
    extendedWay(simpleWay, (nodeId: Long) => nodesToBeConsidered.get(nodeId))

  def extendedWay(
      simpleWay: SimpleWay,
      nodesToBeConsidered: Long => Option[Node]
  ): Try[ComposedEntity.ExtendedWay] = {
    val wayNodes = simpleWay.nodes.collect(nodesToBeConsidered(_)).flatten
    if (wayNodes.size == simpleWay.nodes.size) {
      Success(
        ExtendedWay(
          simpleWay.id,
          simpleWay.nodes.collect(nodesToBeConsidered(_)).flatten,
          simpleWay.tags,
          simpleWay.metaInformation
        )
      )
    } else {
      Failure(
        OsmException(
          s"Cannot create extended way from simple way with id '${simpleWay.id}'. " +
            s"Missing nodes: ${simpleWay.nodes.diff(wayNodes.map(_.id))}"
        )
      )
    }
  }

}
