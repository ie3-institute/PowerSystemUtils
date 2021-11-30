/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import java.time.ZonedDateTime
import java.util.UUID

/** * Abstract class to depict OpenStreetMap entities.
  */
abstract class OsmEntity {

  def uuid: UUID

  def osmId: Int

  def lastEdited: ZonedDateTime

  def tags: Map[String, String]

  def containsKeyValuePair(key: String, value: String): Boolean = {
    tags.get(key) match {
      case Some(tagValue) => tagValue == value
      case None           => false
    }
  }

  def containsKeyValuePair(key: String, values: Set[String]): Boolean = {
    tags.get(key) match {
      case Some(tagValue) => values.contains(tagValue)
      case None           => false
    }
  }

}
