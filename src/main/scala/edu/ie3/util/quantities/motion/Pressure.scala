/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.motion

import edu.ie3.util.quantities.{DynamicViscosity, PascalSeconds}
import squants.motion.Pressure
import squants.time.Time

extension (pressure: Pressure) {
  def *(that: Time): DynamicViscosity = PascalSeconds(
    pressure.toPascals * that.toSeconds
  )
}
