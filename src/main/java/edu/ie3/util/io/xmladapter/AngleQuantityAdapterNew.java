/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.io.xmladapter;

import edu.ie3.util.quantities.PowerSystemUnits;
import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import tech.units.indriya.quantity.Quantities;

/**
 * Adapter to convert Double into {@link Quantity} type {@link Angle}
 *
 * @author Kittl
 * @since 27.03.2018
 */
public class AngleQuantityAdapterNew extends XmlAdapter<Double, Quantity<Angle>> {
  @Override
  public Quantity<Angle> unmarshal(Double v) throws Exception {
    return Quantities.getQuantity(v, PowerSystemUnits.DEGREE_GEOM);
  }

  @Override
  public Double marshal(Quantity<Angle> v) throws Exception {
    return v.to(PowerSystemUnits.DEGREE_GEOM).getValue().doubleValue();
  }
}
