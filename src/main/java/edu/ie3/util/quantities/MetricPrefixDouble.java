/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities;

import edu.ie3.util.exceptions.QuantityException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import tech.units.indriya.unit.TransformedUnit;

/**
 * Offers a way to create units with double conversion, i.e. without involving BigDecimals or
 * BigIntegers. This saves time when converting units, while losing a tiny bit of accuracy.
 */
public class MetricPrefixDouble {

  private MetricPrefixDouble() {
    throw new IllegalStateException("This is a utility class and not meant to be instantiated.");
  }

  @SuppressWarnings({"java:S3011", "PMD.AvoidAccessibilityAlteration"})
  public static <Q extends Quantity<Q>> Unit<Q> prefix(MetricPrefix prefix, Unit<Q> unit) {
    try {
      // dirty hack: Since DoubleMultiplyConverter is package-private, we need reflections to call
      // it
      Class<?> clazz = Class.forName("tech.units.indriya.function.DoubleMultiplyConverter");
      Method construct = clazz.getDeclaredMethod("of", double.class);
      // altering accessibility, warnings are suppressed
      construct.setAccessible(true);

      double factor = Math.pow(prefix.getValue(), prefix.getExponent());
      UnitConverter converter = (UnitConverter) construct.invoke(null, factor);

      Unit<Q> newUnit = unit.transform(converter);
      if (newUnit instanceof TransformedUnit<Q> tu) {
        // manually add symbol, because Unit.transform does not do it
        final String symbol = prefix.getSymbol() + unit.getSymbol();
        newUnit =
            new TransformedUnit<>(
                symbol, tu.getParentUnit(), tu.getSystemUnit(), tu.getConverter());
      }

      return newUnit;
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new QuantityException(
          "Creation of unit " + unit + " with prefix " + prefix + "failed.", e);
    }
  }
}
