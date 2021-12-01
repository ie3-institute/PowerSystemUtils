/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.geo.GeoUtils.DEFAULT_GEOMETRY_FACTORY
import org.locationtech.jts.geom.impl.CoordinateArraySequence
import org.locationtech.jts.geom.{Coordinate, GeometryFactory, LinearRing, Polygon, PrecisionModel}

import java.time.ZonedDateTime
import java.util.UUID
import scala.jdk.CollectionConverters._

object OsmEntities {


  /** Common trait to all OpenStreetMap entities
    */
  sealed trait OsmEntity {
    val uuid: UUID
    val osmId: Int
    val lastEdited: ZonedDateTime
    val tags: Map[String, String]

    /** Checks whether the tags contain a certain key/value pair.
      *
      * @param key
      *   key to look for
      * @param value
      *   associated value
      * @return
      *   Boolean
      */
    def containsKeyValuePair(key: String, value: String): Boolean = {
      tags.get(key) match {
        case Some(tagValue) => tagValue == value
        case None           => false
      }
    }

    /** Checks whether the tags contain a certain key that
      *
      * @param key
      *   key to look for
      * @param values
      *   set of values to look for
      * @return
      *   Boolean
      */
    def containsKeyValuePair(key: String, values: Set[String]): Boolean = {
      tags.get(key) match {
        case Some(tagValue) => values.contains(tagValue)
        case None           => false
      }
    }
  }

  /** Represents an OSM node
    *
    * @param uuid
    *   internal uuid
    * @param osmId
    *   osm specific identifier
    * @param lastEdited
    *   time stamp of the elements last edit
    * @param tags
    *   associated tags
    * @param coordinate
    *   the coordinates of the specific node
    */
  final case class Node(
      uuid: UUID,
      osmId: Int,
      lastEdited: ZonedDateTime,
      tags: Map[String, String],
      coordinate: Coordinate
  ) extends OsmEntity

  /** Common trait to all OSM ways
    */
  sealed trait Way extends OsmEntity {
    def nodes: List[Node]
  }

  /** An open way.
    *
    * @param uuid
    *   internal uuid
    * @param osmId
    *   osm specific identifier
    * @param lastEdited
    *   time stamp of the elements last edit
    * @param tags
    *   associated tags
    * @param nodes
    *   nodes that make up the way
    */
  case class OpenWay(
      uuid: UUID,
      osmId: Int,
      lastEdited: ZonedDateTime,
      tags: Map[String, String],
      nodes: List[Node]
  ) extends Way

  /** A closed way. A way is closed if the first node and its last node are
    * identical.
    *
    * @param uuid
    *   internal uuid
    * @param osmId
    *   osm specific identifier
    * @param lastEdited
    *   time stamp of the elements last edit
    * @param tags
    *   associated tags
    * @param nodes
    *   nodes that make up the way
    */
  case class ClosedWay(
      uuid: UUID,
      osmId: Int,
      lastEdited: ZonedDateTime,
      tags: Map[String, String],
      nodes: List[Node]
  ) extends Way {

    def getCoordinates: List[Coordinate] = {
      nodes.map(_.coordinate)
    }

    def toPolygon: Polygon = {
      val arrayCoordinates = new CoordinateArraySequence(getCoordinates.toArray)
      val linearRing = new LinearRing(arrayCoordinates, DEFAULT_GEOMETRY_FACTORY)
      new Polygon(linearRing, Array[LinearRing](), DEFAULT_GEOMETRY_FACTORY)
    }

  }

  /** An OSM relation.
    *
    * @param uuid
    *   internal uuid
    * @param osmId
    *   osm specific identifier
    * @param lastEdited
    *   time stamp of the elements last edit
    * @param tags
    *   associated tags
    * @param elements
    *   the elements that make up the relation
    */
  case class Relation(
      uuid: UUID,
      osmId: Int,
      lastEdited: ZonedDateTime,
      tags: Map[String, String],
      elements: List[RelationElement]
  ) extends OsmEntity

  /** Represents an element of a relation.
    *
    * @param element
    *   the relation element (which can either be a Way, Node or Relation)
    * @param role
    *   the role of the particular element
    */
  final case class RelationElement(element: OsmEntity, role: String)

}
