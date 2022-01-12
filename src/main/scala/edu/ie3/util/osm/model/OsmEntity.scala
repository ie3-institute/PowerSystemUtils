/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.geo.GeoUtils.buildPolygon
import edu.ie3.util.osm.model.OsmEntity.MetaInformation
import org.locationtech.jts.geom.{Coordinate, Point, Polygon}
import tech.units.indriya.ComparableQuantity
import edu.ie3.util.geo.RichGeometries.*
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.RelationMember
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.RelationMember.{
  ExtendedRelationMember,
  SimpleRelationMember
}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.ClosedWay.SimpleClosedWay
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.OpenWay.SimpleOpenWay

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

  sealed trait SimpleOsmEntity

  sealed trait ExtendedOsmEntity

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
  ) extends OsmEntity
      with ExtendedOsmEntity {
    lazy val coordinate: Point = new Coordinate(longitude, latitude).toPoint
  }

  sealed trait ComposedEntity extends OsmEntity {
    override val id: Long
    override val tags: Map[String, String]
    override val metaInformation: Option[MetaInformation]
  }

  object ComposedEntity {

    sealed trait SimpleComposedOsmEntity
        extends SimpleOsmEntity
        with ComposedEntity {
      val nodes: Seq[Long]
    }

    sealed trait ExtendedComposedOsmEntity
        extends ExtendedOsmEntity
        with ComposedEntity {
      val nodes: Seq[Node]
    }

    sealed trait Way

    object Way {

      sealed trait OpenWay extends Way

      object OpenWay {

        final case class SimpleOpenWay(
            override val id: Long,
            override val nodes: Seq[Long],
            override val tags: Map[String, String],
            override val metaInformation: Option[MetaInformation]
        ) extends OpenWay
            with SimpleComposedOsmEntity

        final case class ExtendedOpenWay(
            override val id: Long,
            override val nodes: Seq[Node],
            override val tags: Map[String, String],
            override val metaInformation: Option[MetaInformation]
        ) extends OpenWay
            with ExtendedComposedOsmEntity

      }

      sealed trait ClosedWay extends Way

      object ClosedWay {

        final case class SimpleClosedWay(
            override val id: Long,
            override val nodes: Seq[Long],
            override val tags: Map[String, String],
            override val metaInformation: Option[MetaInformation]
        ) extends ClosedWay
            with SimpleComposedOsmEntity

        final case class ExtendedClosedWay(
            override val id: Long,
            override val nodes: Seq[Node],
            override val tags: Map[String, String],
            override val metaInformation: Option[MetaInformation]
        ) extends ClosedWay
            with ExtendedComposedOsmEntity

      }

      def apply(
          id: Long,
          nodes: Seq[Long],
          tags: Map[String, String],
          metaInformation: Option[MetaInformation]
      ): Way =
        if (isClosedWay(nodes)) {
          SimpleClosedWay(id, nodes, tags, metaInformation)
        } else {
          SimpleOpenWay(id, nodes, tags, metaInformation)
        }

      def isClosedWay(nodes: Seq[Long]): Boolean =
        nodes.headOption.zip(nodes.lastOption).exists { case (head, last) =>
          head == last
        }

    }

    sealed trait Relation extends ComposedEntity {
      val members: Seq[RelationMember]
    }

    object Relation {

      enum RelationMemberType:
        case Node, Way, Relation, Unrecognized

      sealed trait RelationMember

      object RelationMember {

        final case class SimpleRelationMember(
            id: Long,
            relationType: RelationMemberType,
            role: String
        ) extends RelationMember

        final case class ExtendedRelationMember(
            entity: ExtendedOsmEntity,
            relationType: RelationMemberType,
            role: String
        ) extends RelationMember

      }

      final case class SimpleRelation(
          override val id: Long,
          override val members: Seq[SimpleRelationMember],
          override val tags: Map[String, String],
          override val metaInformation: Option[MetaInformation]
      ) extends Relation
          with SimpleOsmEntity

      final case class ExtendedRelation(
          override val id: Long,
          override val members: Seq[ExtendedRelationMember],
          override val tags: Map[String, String],
          override val metaInformation: Option[MetaInformation]
      ) extends Relation
          with ExtendedOsmEntity

    }

  }

}
