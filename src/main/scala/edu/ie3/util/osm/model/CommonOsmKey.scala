/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

/** Some cherry picked osm keys that are often used. Taken from
  * https://taginfo.openstreetmap.org/keys
  */
enum CommonOsmKey(key: String):

  override def toString: String = key

  case Amenity extends CommonOsmKey("amenity")
  case Building extends CommonOsmKey("building")
  case Highway extends CommonOsmKey("highway")
  case Landuse extends CommonOsmKey("landuse")
  case Name extends CommonOsmKey("name")
  case Power extends CommonOsmKey("power")
  case Surface extends CommonOsmKey("surface")
  case Boundary extends CommonOsmKey("boundary")
end CommonOsmKey
