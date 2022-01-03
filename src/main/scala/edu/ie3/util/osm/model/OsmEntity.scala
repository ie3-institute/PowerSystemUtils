/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.osm.model.OsmEntity.{MetaInformation, OsmEntityType}
import edu.ie3.util.osm.model.OsmEntity.Relation.RelationMember
import org.locationtech.jts.geom.Point

import java.time.Instant
import scala.annotation.tailrec

sealed trait OsmEntity {
  val osmModel: OsmEntityType
  val id: Long
  val tags: Map[String, String]
  val metaInformation: Option[MetaInformation]

  def hasKeyValuePair(key: CommonOsmKey, value: String): Boolean =
    hasKeyValuePair(key.toString, value)

  def hasKeyValuePair(key: String, value: String): Boolean =
    tags.get(key).contains(value)

  def hasKeyValuesPairOr(key: CommonOsmKey, values: Set[String]): Boolean =
    hasKeyValuesPairOr(key.toString, values)

  /** values.isEmpty equals to tagName=*
    *
    * @param key
    * @param values
    * @return
    */
  def hasKeyValuesPairOr(key: String, values: Set[String]): Boolean =
    tags.get(key) match {
      case Some(tagValue) if values.nonEmpty =>
        values.contains(tagValue)
      case Some(tagValue) =>
        true
      case None => false
    }

  @tailrec
  final def hasKeysValuesPairOr(
      keyTagValues: Map[String, Set[String]]
  ): Boolean = {
    keyTagValues.headOption match {
      case Some((key, tagValues)) =>
        // if we have at least one matching key/tagValues pair
        // we can stop the process and return true
        if (hasKeyValuesPairOr(key, tagValues)) {
          true
        } else {
          hasKeysValuesPairOr(keyTagValues - key)
        }
      case None =>
        false
    }
  }

  def hasKey(key: CommonOsmKey): Boolean =
    hasKeyValuesPairOr(key, Set.empty)

  def hasKey(key: String): Boolean =
    hasKeyValuesPairOr(key, Set.empty)

}

object OsmEntity {

  enum OsmEntityType:
    case Node, Way, Relation

  final case class MetaInformation(
      version: Option[Int] = None,
      timestamp: Option[Instant] = None,
      changeSet: Option[Long] = None,
      userId: Option[Int] = None,
      userName: Option[String] = None,
      visible: Option[Boolean] = None
  )

  final case class Node(
      id: Long,
      latitude: Double,
      longitude: Double,
      tags: Map[String, String],
      metaInformation: Option[MetaInformation] = None
  ) extends OsmEntity {
    override val osmModel: OsmEntityType = OsmEntityType.Node
    val coord: Point = GeoUtils.xyToPoint(longitude, latitude)
  }

  sealed trait Way extends OsmEntity {
    val id: Long
    val nodes: Seq[Long]
    val tags: Map[String, String]
    val metaInformation: Option[MetaInformation]

    override val osmModel: OsmEntityType = OsmEntityType.Way
  }

  object Way {

    final case class OpenWay private[model] (
        id: Long,
        nodes: Seq[Long],
        tags: Map[String, String],
        metaInformation: Option[MetaInformation]
    ) extends Way

    final case class ClosedWay private[model] (
        id: Long,
        nodes: Seq[Long],
        tags: Map[String, String],
        metaInformation: Option[MetaInformation]
    ) extends Way

    def apply(
        id: Long,
        nodes: Seq[Long],
        tags: Map[String, String],
        metaInformation: Option[MetaInformation]
    ): Way =
      if (closedWay(nodes)) {
        ClosedWay(id, nodes, tags, metaInformation)
      } else {
        OpenWay(id, nodes, tags, metaInformation)
      }

    def closedWay(nodes: Seq[Long]): Boolean =
      nodes.headOption.zip(nodes.lastOption).exists { case (head, last) =>
        head == last
      }

  }

  final case class Relation(
      id: Long,
      relations: Seq[RelationMember],
      tags: Map[String, String],
      metaInformation: Option[MetaInformation] = None
  ) extends OsmEntity {
    override val osmModel: OsmEntityType = OsmEntityType.Relation
  }

  object Relation {

    enum RelationMemberType:
      case Node, Way, Relation, Unrecognized

    final case class RelationMember(
        id: Long,
        relationTypes: RelationMemberType,
        role: String
    )

  }
}
