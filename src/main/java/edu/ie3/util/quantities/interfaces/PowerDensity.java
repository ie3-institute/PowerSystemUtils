/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.interfaces;

import tech.units.indriya.ComparableQuantity;

/**
 * Rate of {@link javax.measure.quantity.Power} and {@link javax.measure.quantity.Area}
 *
 * @deprecated replaced by {@link Irradiation}
 */
@Deprecated
public interface PowerDensity extends ComparableQuantity<PowerDensity> {}
