/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities;

import javax.measure.Quantity;

import tec.uom.se.ComparableQuantity;
import tec.uom.se.quantity.Quantities;


/**
 * Offers useful methods to handle {@link Quantity}s
 */
public class QuantityUtil {
    private QuantityUtil() {
        throw new IllegalStateException("Utility classes cannot be instantiated.");
    }

    /**
     * Converts a given quantity to a comparable quantity
     *
     * @param quantity Input quantity
     * @param <Q>      Type of input quantity
     * @return The same value and unit, but as a {@link ComparableQuantity}
     */
    public static <Q extends Quantity<Q>> ComparableQuantity<Q> makeComparable(Quantity<Q> quantity) {
        return Quantities.getQuantity(quantity.getValue(), quantity.getUnit());
    }

    /**
     * Compares two {@link Quantity}s, if they are considerably equal. This is foremost important for
     * {@link tec.uom.se.quantity.DoubleQuantity}s. The comparison is made on the absolute difference
     * of both quantities' value. Both quantities are converted into a's unit before the comparison.
     *
     * @param a                    First quantity to compare
     * @param b                    Second quantity to compare
     * @param absQuantityTolerance Permissible absolute tolerance
     * @param <Q>                  Type of Quantity
     * @return true, if both quantities' values differ less then the given tolerance else false
     */
    public static <Q extends Quantity<Q>> boolean considerablyAbsEqual(Quantity<Q> a,
                                                                       Quantity<Q> b,
                                                                       double absQuantityTolerance) {

        // if units differ, return false
        if(!a.getUnit().equals(b.getUnit()))
            return false;

        double aVal = a.getValue().doubleValue();
        double bVal = b.to(a.getUnit()).getValue().doubleValue();

        return Math.abs(aVal - bVal) <= absQuantityTolerance;
    }

    /**
     * Compares two {@link Quantity}s, if they are considerably equal. This is foremost important for
     * {@link tec.uom.se.quantity.DoubleQuantity}s. The comparison is made on the relative difference
     * of both quantities' value with regard to a's value. Both quantities are converted into a's unit
     * before the comparison.
     *
     * @param a                    First quantity to compare
     * @param b                    Second quantity to compare
     * @param relQuantityTolerance Permissible relative tolerance
     * @param <Q>                  Type of Quantity
     * @return true, if both quantities' values differ less then the given tolerance else false
     */
    public static <Q extends Quantity<Q>> boolean considerablyRelEqual(Quantity<Q> a,
                                                                       Quantity<Q> b,
                                                                       double relQuantityTolerance) {

        // if units differ, return false
        if(!a.getUnit().equals(b.getUnit()))
            return false;

        double aVal = a.getValue().doubleValue();
        double bVal = b.to(a.getUnit()).getValue().doubleValue();

        return (Math.abs(aVal - bVal) / Math.abs(aVal)) <= relQuantityTolerance;
    }
}
