/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.io.xmladapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.Year;

/**
 * @author Kittl
 * @since 26.09.2018
 * @deprecated Will be removed in v2.0
 */
@Deprecated
public class YearAdapter extends XmlAdapter<Integer, Year> {
  @Override
  public Year unmarshal(Integer v) throws Exception {
    return v == null ? null : Year.of(v);
  }

  @Override
  public Integer marshal(Year v) throws Exception {
    return v == null ? null : v.getValue();
  }
}
