/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.interfaces;

import tech.units.indriya.ComparableQuantity;

/**
 * Irradiance is the radiant exitance emitted or received by a surface per unit area.
 *
 * <p>The SI unit of radiant exitance is the watt per square metre (W/m<sup>2</sup>), while that of
 * spectral exitance in frequency is the watt per square metre per hertz
 * (W·m<sup>-2</sup>·Hz<sup>-1</sup>) and that of spectral exitance in wavelength is the watt per
 * square metre per metre (W·m<sup>-3</sup>)—commonly the watt per square metre per nanometre
 * (W·m<sup>-2</sup>·nm<sup>-1</sup>).
 */
public interface Irradiance extends ComparableQuantity<Irradiance> {}
