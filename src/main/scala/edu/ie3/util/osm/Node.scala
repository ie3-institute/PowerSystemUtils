/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import org.locationtech.jts.geom.Point

import java.time.ZonedDateTime
import java.util.UUID

/** Represents an OSM Node
 */
final case class Node(
    uuid: UUID,
    osmId: Int,
    lastEdited: ZonedDateTime,
    tags: Map[String, String],
    coordinates: Point
) extends OsmEntity
