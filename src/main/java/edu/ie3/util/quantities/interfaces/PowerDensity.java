/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.interfaces;

import tech.units.indriya.ComparableQuantity;

/**
 * Interface to describe power density.
 *
 * <p>Can for example depict irradiance which is the radiant exitance emitted or received by a
 * surface per unit area. Relevant in the context of pv plants where it describes the power density
 * of the sun's radiation.
 */
public interface PowerDensity extends ComparableQuantity<PowerDensity> {}
