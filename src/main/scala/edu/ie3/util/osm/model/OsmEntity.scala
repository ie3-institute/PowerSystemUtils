/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.geo.GeoUtils.buildPolygon
import edu.ie3.util.geo.RichGeometries.*
import edu.ie3.util.osm.model.OsmEntity.MetaInformation
import edu.ie3.util.osm.model.OsmEntity.Relation.RelationMember
import org.locationtech.jts.geom.{Coordinate, Point, Polygon}
import tech.units.indriya.ComparableQuantity

import java.time.Instant
import javax.measure.quantity.Area
import scala.annotation.tailrec

sealed trait OsmEntity {
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

  final case class MetaInformation(
      version: Option[Int] = None,
      timestamp: Option[Instant] = None,
      changeSet: Option[Long] = None,
      userId: Option[Int] = None,
      userName: Option[String] = None,
      visible: Option[Boolean] = None
  )

  final case class Node(
      override val id: Long,
      latitude: Double,
      longitude: Double,
      override val tags: Map[String, String],
      override val metaInformation: Option[MetaInformation] = None
  ) extends OsmEntity {
    val coordinate: Point = GeoUtils.buildPoint(longitude, latitude)
  }

  sealed trait Way extends OsmEntity {
    val id: Long
    val nodes: Seq[Long]
    val tags: Map[String, String]
    val metaInformation: Option[MetaInformation]
  }

  object Way {

    final case class OpenWay private[model] (
        override val id: Long,
        override val nodes: Seq[Long],
        override val tags: Map[String, String],
        override val metaInformation: Option[MetaInformation]
    ) extends Way

    final case class ClosedWay private[model] (
        override val id: Long,
        override val nodes: Seq[Long],
        override val tags: Map[String, String],
        override val metaInformation: Option[MetaInformation]
    ) extends Way {
      def toPolygon(nodes: Seq[Node]): Polygon = {
        val coordinates = nodes
          .filter(node => this.nodes.contains(node.id))
          .map(node => new Coordinate(node.longitude, node.latitude))
          .toArray

        buildPolygon(coordinates)
      }

      def calculateArea(nodes: Seq[Node]): ComparableQuantity[Area] =
        toPolygon(nodes).calcAreaOnEarth
    }

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
      override val id: Long,
      members: Seq[RelationMember],
      override val tags: Map[String, String],
      override val metaInformation: Option[MetaInformation] = None
  ) extends OsmEntity

  object Relation {

    enum RelationMemberType:
      case Node, Way, Relation, Unrecognized

    final case class RelationMember(
        id: Long,
        relationType: RelationMemberType,
        role: String
    )

  }
}
