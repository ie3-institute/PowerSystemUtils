/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.geo.RichGeometries.RichCoordinate
import edu.ie3.util.osm.model.OsmEntity.MetaInformation
import edu.ie3.util.osm.model.OsmEntity.Relation.RelationMember
import edu.ie3.util.osm.model.OsmEntity.Relation.RelationMemberType.RelationMemberType
import org.locationtech.jts.geom.{Coordinate, Point}

import java.time.Instant

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
      case Some(_) =>
        true
      case None => false
    }

  /** Checks whether or not the entity has a tag that matches one of the given
    * key to value sets in the keyTagValues Map. NOTE: If an empty values set is
    * given it matches any value for the specific key.
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
  ) extends OsmEntity {
    lazy val coordinate: Point = new Coordinate(longitude, latitude).toPoint
  }

  /** Groups different implementation of OSM Ways.
    */
  sealed trait Way extends OsmEntity {
    val nodes: Seq[Long]
    val version: Option[Int]
  }

  object Way {

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
      * @param version
      *   version number of dataset
      */
    final case class OpenWay(
        override val id: Long,
        override val nodes: Seq[Long],
        override val tags: Map[String, String],
        override val metaInformation: Option[MetaInformation],
        override val version: Option[Int]
    ) extends Way

    /** [[ClosedWay]] implementation that resemble OSM ways which are closed so
      * their first and last node are identical. They mostly represent areas.
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
      * @param version
      *   version number of dataset
      */
    final case class ClosedWay(
        override val id: Long,
        override val nodes: Seq[Long],
        override val tags: Map[String, String],
        override val metaInformation: Option[MetaInformation],
        override val version: Option[Int]
    ) extends Way

    def apply(
        id: Long,
        nodes: Seq[Long],
        tags: Map[String, String],
        metaInformation: Option[MetaInformation],
        version: Option[Int]
    ): Way = {
      if (isClosedWay(nodes)) {
        ClosedWay(id, nodes, tags, metaInformation, version)
      } else {
        OpenWay(id, nodes, tags, metaInformation, version)
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
    def isClosedWay(nodes: Seq[Long]): Boolean =
      nodes.headOption.zip(nodes.lastOption).exists { case (head, last) =>
        head == last
      }
  }

  /** A [[Relation]] implementation.
    *
    * @param id
    *   osm specific identifier of the relation
    * @param members
    *   a sequence of all relations
    * @param tags
    *   tags that store additional characteristic of the way as key-value pairs
    * @param metaInformation
    *   additional meta information of the data object
    */
  final case class Relation(
      override val id: Long,
      members: Seq[RelationMember],
      override val tags: Map[String, String],
      override val metaInformation: Option[MetaInformation]
  ) extends OsmEntity

  object Relation {

    /** Enumeration of the different members a relation can consist of.
      */
    object RelationMemberType extends Enumeration {
      type RelationMemberType = Value

      val Node, Way, Relation, Unrecognized = Value

      def apply(osmEntity: OsmEntity): RelationMemberType =
        osmEntity match {
          case _: Node =>
            RelationMemberType.Node
          case _: Way =>
            RelationMemberType.Way
          case _: Relation =>
            RelationMemberType.Relation
        }
    }

    /** A simple member of a relation which can be either a node a way or a
      * relation.
      *
      * @param id
      *   osm specific identifier of the member
      * @param relationType
      *   the specific type of the member
      * @param role
      *   additional information about its role
      */
    final case class RelationMember(
        id: Long,
        relationType: RelationMemberType,
        role: String
    )
  }
}
