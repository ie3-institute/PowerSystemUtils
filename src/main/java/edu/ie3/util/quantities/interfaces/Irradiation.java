/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.interfaces;

import tech.units.indriya.ComparableQuantity;

/**
 * Interface to describe the radiant exposure quantity. It is the radiant energy received by a
 * surface per unit area, or equivalently {@link Irradiance} of a surface integrated over time of
 * irradiation. It's SI unit is J/m<sup>2</sup>.
 *
 * @deprecated replaced by {@link EnergyDensity}
 */
@Deprecated(since = "1.5", forRemoval = true)
public interface Irradiation extends ComparableQuantity<Irradiation> {}
