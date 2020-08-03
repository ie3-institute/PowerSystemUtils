/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.io.xmladapter;

import edu.ie3.util.quantities.dep.PowerSystemUnits;
import javax.measure.Quantity;
import javax.measure.quantity.Length;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import tec.uom.se.quantity.Quantities;

/**
 * @author Kittl
 * @since 26.09.2018
 */
public class LengthAdapter extends XmlAdapter<Double, Quantity<Length>> {
  @Override
  public Quantity<Length> unmarshal(Double v) throws Exception {
    return Quantities.getQuantity(v, PowerSystemUnits.KILOMETRE);
  }

  @Override
  public Double marshal(Quantity<Length> v) throws Exception {
    return v.to(PowerSystemUnits.KILOMETRE).getValue().doubleValue();
  }
}
