/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

sealed abstract class CommonOsmKey {
  val key: String
  override def toString: String = key
}

/** Some cherry picked osm keys that are often used. Taken from
  * https://taginfo.openstreetmap.org/keys
  */
object CommonOsmKey {

  case class Amenity(override val key: String = "amenity") extends CommonOsmKey

  case class Building(override val key: String = "building")
      extends CommonOsmKey

  case class Highway(override val key: String = "highway") extends CommonOsmKey

  case class Landuse(override val key: String = "landuse") extends CommonOsmKey

  case class Name(override val key: String = "name") extends CommonOsmKey

  case class Power(override val key: String = "power") extends CommonOsmKey

  case class Surface(override val key: String = "surface") extends CommonOsmKey

  case class Boundary(override val key: String = "boundary")
      extends CommonOsmKey

}
