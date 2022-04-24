/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.interfaces;

import tech.units.indriya.ComparableQuantity;

/**
 * Interface to describe energy density.
 *
 * <p>Can for example depict irradiation which is the radiant energy received by a surface per unit
 * area. Relevant in the context of pv plants where it describes the energy density of the sun's
 * radiation.
 */
public interface EnergyDensity extends ComparableQuantity<EnergyDensity> {}
