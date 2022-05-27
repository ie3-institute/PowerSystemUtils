/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

sealed abstract class CommonOsmKey(key: String) {
  override def toString: String = key
}

/** Some cherry picked osm keys that are often used. Taken from
  * https://taginfo.openstreetmap.org/keys
  */
object CommonOsmKey {

  case object Amenity extends CommonOsmKey("amenity")

  case object Building extends CommonOsmKey("building")

  case object Highway extends CommonOsmKey("highway")

  case object Landuse extends CommonOsmKey("landuse")

  case object Name extends CommonOsmKey("name")

  case object Power extends CommonOsmKey("power")

  case object Surface extends CommonOsmKey("surface")

  case object Boundary extends CommonOsmKey("boundary")

}
