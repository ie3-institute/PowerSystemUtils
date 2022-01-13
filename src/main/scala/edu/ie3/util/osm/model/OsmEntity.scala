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
import edu.ie3.util.osm.OsmUtils
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.RelationMember
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.RelationMember.{
  ExtendedRelationMember,
  SimpleRelationMember
}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.ClosedWay.{
  ExtendedClosedWay,
  SimpleClosedWay
}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.ExtendedWay
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.OpenWay.{
  ExtendedOpenWay,
  SimpleOpenWay
}
import tech.units.indriya.unit.Units

import java.time.Instant
import javax.measure.quantity.Area
import scala.annotation.tailrec
import scala.util.Try

/** Base trait for all different OSM entities
  */
sealed trait OsmEntity {
  val id: Long
  val tags: Map[String, String]
  val metaInformation: Option[MetaInformation]

  /** Checks whether or not the entity has a given key value pair within its
    * tags.
    *
    * @param key
    *   the key to match
    * @param value
    *   the value to match
    * @return
    *   whether or not it contains the key value pair
    */
  def hasKeyValuePair(key: CommonOsmKey, value: String): Boolean =
    hasKeyValuePair(key.toString, value)

  /** Checks whether or not the entity has a given key value pair within its
    * tags.
    *
    * @param key
    *   the key to match
    * @param value
    *   the value to look for
    * @return
    *   whether or not it contains the key value pair
    */
  def hasKeyValuePair(key: String, value: String): Boolean =
    tags.get(key).contains(value)

  /** Checks whether or not the entity has a tag that matches the given key and
    * one of the set of given values. NOTE: If an empty values set is given it
    * matches any value.
    *
    * @param key
    *   the common osm key to match
    * @param values
    *   the set of values to match against
    * @return
    *   whether or not the entity has a matching key value pair
    */
  def hasKeyValuesPairOr(key: CommonOsmKey, values: Set[String]): Boolean =
    hasKeyValuesPairOr(key.toString, values)

  /** Checks whether or not the entity has a tag that matches the given key and
    * one of the set of given values. NOTE: If an empty values set is given it
    * matches any value.
    *
    * @param key
    *   the common osm key to match
    * @param values
    *   the set of values to match against
    * @return
    *   whether or not the entity has a matching key value pair
    */
  def hasKeyValuesPairOr(key: String, values: Set[String]): Boolean =
    tags.get(key) match {
      case Some(tagValue) if values.nonEmpty =>
        values.contains(tagValue)
      case Some(tagValue) =>
        true
      case None => false
    }

  /** Checks whether or not the entity has a tag that matches one of the given
    * key to value sets in the [[keyTagValues]] Map. NOTE: If an empty values
    * set is given it matches any value for the specific key.
    *
    * @param keyTagValues
    *   mapping from key to possible values to match against
    * @return
    *   whether or not the entity has a matching key value pair
    */
  final def hasKeysValuesPairOr(
      keyTagValues: Map[String, Set[String]]
  ): Boolean = keyTagValues.exists { case (key, tagValues) =>
    hasKeyValuesPairOr(key, tagValues)
  }

  /** Checks if the entities tags contains the given key.
    *
    * @param key
    *   the key to look for
    * @return
    *   whether or not the tags contain the key
    */
  def hasKey(key: CommonOsmKey): Boolean =
    hasKeyValuesPairOr(key, Set.empty)

  /** Checks if the entities tags contains the given key.
    *
    * @param key
    *   the key to look for
    * @return
    *   whether or not the tags contain the key
    */
  def hasKey(key: String): Boolean =
    hasKeyValuesPairOr(key, Set.empty)

}

object OsmEntity {

  /** Osm entity where the entity is tracked
    */
  sealed trait SimpleOsmEntity

  /** */
  sealed trait ExtendedOsmEntity

  /** Common attributes of the osm data model. Explanation taken from:
    * https://wiki.openstreetmap.org/wiki/Elements
    *
    * @param version
    *   The edit version of the object. Newly created objects start at version 1
    *   and the value is incremented by the server when a client uploads a new
    *   version of the object. The server will reject a new version of an object
    *   if the version sent by the client does not match the current version of
    *   the object in the database.
    * @param timestamp
    *   Time of the last modification (e.g. "2016-12-31T23:59:59.999Z").
    * @param changeSet
    *   The changeset number in which the object was created or updated
    *   (supporting 64-bit is recommended in applications for compatibility with
    *   long term evolution of the OSM database, but applications that only
    *   query data without updating them may ignore this informative attribute).
    * @param userId
    *   The numeric identifier of the user who last modified the object. An user
    *   identifier never changes.
    * @param userName
    *   The display name of the user who last modified the object (informative
    *   only and may be empty). A user can change their display name at any time
    *   (existing elements will reflect the new user name without needing any
    *   version change).
    * @param visible
    *   Whether the object is deleted or not in the database, if visible="false"
    *   then the object should only be returned by history calls.
    */
  final case class MetaInformation(
      version: Option[Int] = None,
      timestamp: Option[Instant] = None,
      changeSet: Option[Long] = None,
      userId: Option[Int] = None,
      userName: Option[String] = None,
      visible: Option[Boolean] = None
  )

  /** A Point represented by its id and latitude as well as longitude values.
    * Characterizing information about the points are tracked within the tags.
    *
    * @param id
    *   unique (between nodes) identifier
    * @param latitude
    *   latitude value as decimal number
    * @param longitude
    *   longitude values as decimal number
    * @param tags
    *   tags mapping
    * @param metaInformation
    *   additional meta information
    */
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

  /** Groups entities that are made up of other OSM entities. Includes Ways
    * which are made up off multiple Nodes and relations which can be made up
    * off nodes ways or other relations.
    */
  sealed trait ComposedEntity extends OsmEntity {
    override val id: Long
    override val tags: Map[String, String]
    override val metaInformation: Option[MetaInformation]
  }

  object ComposedEntity {

    /** Groups different implementation of OSM Ways.
      */
    sealed trait Way extends ComposedEntity

    /** Groups simple ways. Simple ways are ways for which only the identifier
      * of the nodes they consist of are tracked rather then the node objects.
      */
    sealed trait SimpleWay extends Way with SimpleOsmEntity {
      val nodes: Seq[Long]

      /** Tries building an extended way from the simple way.
        *
        * @param idToNode
        *   mapping of node id to the corresponding node object
        * @return
        *   a Try of an [[ExtendedWay]]
        */
      def asExtended(idToNode: Map[Long, Node]): Try[ExtendedWay] =
        OsmUtils.extendedWay(this, idToNode)

      /** Tries building an extended way from the simple way.
        *
        * @param nodeFunction
        *   function that retrieves a node by passing a corresponding id
        * @return
        *   a Try of an [[ExtendedWay]]
        */
      def asExtended(nodeFunction: Long => Option[Node]): Try[ExtendedWay] =
        OsmUtils.extendedWay(this, nodeFunction)
    }

    /** Groups extended way implementations. Extended ways in contrast to a
      * [[SimpleWay]] track the nodes not by their id but by the corresponding
      * [[Node]] s
      */
    sealed trait ExtendedWay extends Way with ExtendedOsmEntity {
      val nodes: Seq[Node]
    }

    object Way {

      /** Groups open ways. Open ways are by definition not closed so their
        * first and last node are different.
        */
      sealed trait OpenWay extends Way

      object OpenWay {

        /** [[OpenWay]] implementation that resemble OSM ways which are by
          * definition not closed so their first and last node are different.
          *
          * @param id
          *   unique (with respect to all ways) OSM identifier of the way
          * @param nodes
          *   sequence of node ids that make up the way
          * @param tags
          *   tags that store additional characteristic of the way as key-value
          *   pairs
          * @param metaInformation
          *   additional meta information of the data object
          */
        final case class SimpleOpenWay(
            override val id: Long,
            override val nodes: Seq[Long],
            override val tags: Map[String, String],
            override val metaInformation: Option[MetaInformation]
        ) extends OpenWay
            with SimpleWay

        /** [[OpenWay]] implementation that resemble OSM ways which are by
          * definition not closed so their first and last node are different.
          *
          * @param id
          *   unique (with respect to all ways) OSM identifier of the way
          * @param nodes
          *   sequence of nodes that make up the way
          * @param tags
          *   tags that store additional characteristic of the way as key-value
          *   pairs
          * @param metaInformation
          *   additional meta information of the data object
          */
        final case class ExtendedOpenWay(
            override val id: Long,
            override val nodes: Seq[Node],
            override val tags: Map[String, String],
            override val metaInformation: Option[MetaInformation]
        ) extends OpenWay
            with ExtendedWay

      }

      /** Groups closed ways. Closed ways are ways in which the first and last
        * node are identical. They mostly represent areas.
        */
      sealed trait ClosedWay extends Way

      object ClosedWay {

        /** [[ClosedWay]] implementation that resemble OSM ways which are closed
          * so their first and last node are identical. They mostly represent
          * areas.
          *
          * @param id
          *   unique (with respect to all ways) OSM identifier of the way
          * @param nodes
          *   sequence of node ids that make up the way
          * @param tags
          *   tags that store additional characteristic of the way as key-value
          *   pairs
          * @param metaInformation
          *   additional meta information of the data object
          */
        final case class SimpleClosedWay(
            override val id: Long,
            override val nodes: Seq[Long],
            override val tags: Map[String, String],
            override val metaInformation: Option[MetaInformation]
        ) extends ClosedWay
            with SimpleWay

        /** [[ClosedWay]] implementation that resemble OSM ways which are closed
          * so their first and last node are identical. They mostly represent
          * areas.
          *
          * @param id
          *   unique (with respect to all ways) OSM identifier of the way
          * @param nodes
          *   sequence of nodes that make up the way
          * @param tags
          *   tags that store additional characteristic of the way as key-value
          *   pairs
          * @param metaInformation
          *   additional meta information of the data object
          */
        final case class ExtendedClosedWay(
            override val id: Long,
            override val nodes: Seq[Node],
            override val tags: Map[String, String],
            override val metaInformation: Option[MetaInformation]
        ) extends ClosedWay
            with ExtendedWay {
          lazy val polygon: Polygon =
            GeoUtils.buildPolygon(
              nodes
                .map(node => new Coordinate(node.longitude, node.latitude))
                .toArray
            )

          /** Calculates the are of a way in earth's surface in square metre.
            *
            * @return
            *   the [[ComparableQuantity]] in square metre
            */
          def areaOnEarth(): ComparableQuantity[Area] =
            polygon.calcAreaOnEarth.to(Units.SQUARE_METRE)

          /** Calculates geometric centre of the way.
            *
            * @return
            *   the centroid's [[Coordinate]]
            */
          def centroid(): Coordinate = polygon.getCentroid.getCoordinate

        }
      }

      object SimpleWay {

        def apply(
            id: Long,
            nodes: Seq[Long],
            tags: Map[String, String],
            metaInformation: Option[MetaInformation]
        ): SimpleWay =
          if (isClosedWay(nodes)) {
            SimpleClosedWay(id, nodes, tags, metaInformation)
          } else {
            SimpleOpenWay(id, nodes, tags, metaInformation)
          }
      }

      object ExtendedWay {
        def apply(
            id: Long,
            nodes: Seq[Node],
            tags: Map[String, String],
            metaInformation: Option[MetaInformation]
        ): ExtendedWay =
          if (isClosedWay(nodes)) {
            ExtendedClosedWay(id, nodes, tags, metaInformation)
          } else {
            ExtendedOpenWay(id, nodes, tags, metaInformation)
          }
      }

      /** Checks if a way is closed by assessing if the first and last nodes are
        * identical.
        *
        * @param nodes
        *   a sequence of node ids
        * @return
        *   whether or not the way is closed
        */
      private def isClosedWay(nodes: Seq[Long] | Seq[Node]): Boolean =
        nodes.headOption.zip(nodes.lastOption).exists { case (head, last) =>
          head == last
        }

    }

    /** Groups relation data classes. A relation is one of the core OSM data
      * elements, consists of a group of OSM elements (nodes, ways, relations)
      * and represents a relationship between them.
      */
    sealed trait Relation extends ComposedEntity {
      val members: Seq[RelationMember]
    }

    object Relation {

      /** Enumeration of the different members a relation can consist off.
        */
      enum RelationMemberType:
        case Node, Way, Relation, Unrecognized

      object RelationMemberType {
        def apply(osmEntity: OsmEntity): RelationMemberType =
          osmEntity match {
            case _: Node =>
              Node
            case _: Way =>
              Way
            case _: Relation =>
              Relation
          }

      }

      sealed trait RelationMember

      object RelationMember {

        /** A simple member of a relation which can be either a node a way or a
          * relation. In contrast to [[ExtendedRelationMember]] it holds the id
          * of the specific OSM element rather than a reference to the object.
          *
          * @param id
          *   osm specific identifier of the member
          * @param relationType
          *   the specific type of the member
          * @param role
          *   additional information about its role
          */
        final case class SimpleRelationMember(
            id: Long,
            relationType: RelationMemberType,
            role: String
        ) extends RelationMember

        object SimpleRelationMember {
          def apply(osmEntity: OsmEntity): SimpleRelationMember =
            SimpleRelationMember(
              osmEntity.id,
              RelationMemberType(osmEntity),
              s"converted_${osmEntity.id}"
            )

        }

        /** An extended member of a relation which can be either a node a way or
          * a relation. In contrast to [[SimpleRelationMember]] it holds the
          * reference to the specific OSM element rather than only its
          * identifier.
          *
          * @param entity
          *   the osm entity
          * @param relationType
          *   the specific type of the member
          * @param role
          *   additional information about its role
          */
        final case class ExtendedRelationMember(
            entity: ExtendedOsmEntity,
            relationType: RelationMemberType,
            role: String
        ) extends RelationMember

        object ExtendedRelationMember {
          def apply(
              osmEntity: OsmEntity with ExtendedOsmEntity
          ): ExtendedRelationMember = ExtendedRelationMember(
            osmEntity,
            RelationMemberType(osmEntity),
            s"converted_${osmEntity.id}"
          )
        }
      }

      /** A [[Relation]] implementation that contains [[SimpleRelationMember]]
        * s.
        *
        * @param id
        *   osm specific identifier of the relation
        * @param members
        *   a sequence of all relations
        * @param tags
        *   tags that store additional characteristic of the way as key-value
        *   pairs
        * @param metaInformation
        *   additional meta information of the data object
        */
      final case class SimpleRelation(
          override val id: Long,
          override val members: Seq[SimpleRelationMember],
          override val tags: Map[String, String],
          override val metaInformation: Option[MetaInformation]
      ) extends Relation
          with SimpleOsmEntity

      /** [[Relation]] implementation that contains [[ExtendedRelationMember]]
        * s.
        *
        * @param id
        *   osm specific identifier of the relation
        * @param members
        *   a sequence of all relations
        * @param tags
        *   tags that store additional characteristic of the way as key-value
        *   pairs
        * @param metaInformation
        *   additional meta information of the data object
        */
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
