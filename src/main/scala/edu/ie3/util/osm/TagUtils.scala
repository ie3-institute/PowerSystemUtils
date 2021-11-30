/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

object TagUtils {

  object Keys {
    val building = "building"
    val highway = "highway"
    val landuse = "landuse"
  }

  object Values {
    val highWayValues = Set(
      "residential",
      "unclassified",
      "secondary",
      "tertiary",
      "living_street",
      "footway",
      "path",
      "primary",
      "service",
      "cycleway",
      "proposed",
      "bus_stop",
      "steps",
      "track",
      "traffic_signals",
      "turning_cycle"
    )

    // Fixme: check whether to use or delete "greenfield" and "meadow"
    val landUseValues = Set(
      "residential",
      "commercial",
      "retail",
      // "greenfield",
      // "meadow",
      "farmyard"
    )
  }
}
