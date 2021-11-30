/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

/** Represents an element of a relation.
  *
  * @param element
  *   the relation element (which can either be a Way, Node or Relation)
  * @param role
  */
final case class RelationElement(element: OsmEntity, role: String)
