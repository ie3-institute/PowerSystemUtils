/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.io.xmladapter;

import static tec.uom.se.unit.Units.PERCENT;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import tec.uom.se.quantity.Quantities;

/**
 * @author Kittl
 * @since 27.03.2018
 */
public class PercentQuantityAdapter extends XmlAdapter<Double, Quantity<Dimensionless>> {
  @Override
  public Quantity<Dimensionless> unmarshal(Double v) {
    return Quantities.getQuantity(v, PERCENT);
  }

  @Override
  public Double marshal(Quantity<Dimensionless> v) {
    return v.to(PERCENT).getValue().doubleValue();
  }
}
