/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.dep.interfaces;

import tec.uom.se.ComparableQuantity;

/**
 * Rate of {@link javax.measure.quantity.Power} and {@link javax.measure.quantity.Area}
 *
 * @deprecated As of release 1.4, replaced by {@link
 *     edu.ie3.util.quantities.interfaces.PowerDensity}
 */
@Deprecated
public interface PowerDensity extends ComparableQuantity<PowerDensity> {}
