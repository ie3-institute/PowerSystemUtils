/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.interfaces;

import tec.uom.se.ComparableQuantity;

/** Interface to describe the Quantity of an (solar) Irradiation */
/** @deprecated As of release 1.4, replaced by {@link IrradiationNew} */
@Deprecated
public interface Irradiation extends ComparableQuantity<Irradiation> {}
