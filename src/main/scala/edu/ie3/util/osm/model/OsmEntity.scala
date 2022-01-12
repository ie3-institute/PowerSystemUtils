/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.geo.GeoUtils.buildPolygon
import edu.ie3.util.osm.model.OsmEntity.MetaInformation
import edu.ie3.util.osm.model.OsmEntity.Relation.RelationMember
import org.locationtech.jts.geom.{Coordinate, Point, Polygon}
import tech.units.indriya.ComparableQuantity
import edu.ie3.util.geo.RichGeometries._

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

  final def hasKeysValuesPairOr(
      keyTagValues: Map[String, Set[String]]
  ): Boolean = keyTagValues.exists { case (key, tagValues) =>
    hasKeyValuesPairOr(key, tagValues)
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
    lazy val coordinate: Point = new Coordinate(longitude, latitude).toPoint
  }

  sealed trait Way extends OsmEntity {
    val id: Long
    val nodes: Seq[Long]
    val tags: Map[String, String]
    val metaInformation: Option[MetaInformation]
  }

  object Way {

    final case class OpenWay(
        override val id: Long,
        override val nodes: Seq[Long],
        override val tags: Map[String, String],
        override val metaInformation: Option[MetaInformation]
    ) extends Way

    final case class ClosedWay(
        override val id: Long,
        override val nodes: Seq[Long],
        override val tags: Map[String, String],
        override val metaInformation: Option[MetaInformation]
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
