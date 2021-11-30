/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import java.time.ZonedDateTime
import java.util.UUID

case class Relation(
    uuid: UUID,
    osmId: Int,
    lastEdited: ZonedDateTime,
    tags: Map[String, String],
    elements: List[RelationElement]
) extends OsmEntity
