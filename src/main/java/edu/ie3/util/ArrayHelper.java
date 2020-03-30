/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util;

import org.apache.commons.math3.complex.Complex;

/**
 * Contains convenience methos for arrays
 *
 * @author roemer
 */
public class ArrayHelper {

  private ArrayHelper() {}

  /**
   * Return true, if the given object is contained in the given array
   *
   * @param arr Array to check
   * @param obj Object to be checked
   * @return true, if the value lays inside the interval
   */
  public static boolean arrayContainsValue(Object[] arr, Object obj) {
    for (Object arrObj : arr) if (arrObj.equals(obj)) return true;

    return false;
  }

  /**
   * Return true, if the given int is contained in the given array
   *
   * @param arr Array to check
   * @param value Object to be checked
   * @return true, if the value lays inside the interval
   */
  public static boolean arrayContainsValue(int[] arr, int value) {
    for (int arrVal : arr) if (arrVal == value) return true;

    return false;
  }

  /**
   * Element wise addition of two double arrays
   *
   * @param a Array of double values for the first summand
   * @param b Array of double values for the second summand
   * @return c Double array with the summation of a and b
   */
  public static double[] add(double[] a, double[] b) {
    assert a.length == b.length : "Both arrays may have the same dimension.";
    double[] c = new double[a.length];
    for (int i = 0; i < c.length; i++) {
      c[i] = a[i] + b[i];
    }
    return c;
  }

  /**
   * Element wise addition of two double arrays
   *
   * @param a Array of {@link Complex} values for the first summand
   * @param b Array of {@link Complex} values for the second summand
   * @return c Double array with the summation of a and b
   */
  public static Complex[] add(Complex[] a, Complex[] b) {
    assert a.length == b.length : "Both arrays may have the same dimension.";
    Complex[] c = new Complex[a.length];
    for (int i = 0; i < c.length; i++) {
      c[i] = a[i].add(b[i]);
    }
    return c;
  }

  /**
   * Element wise subtraction
   *
   * @param a Left hand side Array of double values
   * @param b Right hand side Array of double values
   * @return The Array of double values built by subtraction
   */
  public static double[] subtract(double[] a, double[] b) {
    assert a.length == b.length : "Both arrays may have the same dimension.";
    double[] c = new double[a.length];
    for (int i = 0; i < c.length; i++) {
      c[i] = a[i] - b[i];
    }
    return c;
  }

  /**
   * Element wise subtraction
   *
   * @param a Left hand side Array of {@link Complex} values
   * @param b Right hand side Array of {@link Complex} values
   * @return The Array of {@link Complex} values built by subtraction
   */
  public static Complex[] subtract(Complex[] a, Complex[] b) {
    assert a.length == b.length : "Both arrays may have the same dimension.";
    Complex[] c = new Complex[a.length];
    for (int i = 0; i < c.length; i++) {
      c[i] = a[i].subtract(b[i]);
    }
    return c;
  }

  /**
   * Builds the exponential value of each of the Array elements
   *
   * @param a Array of {@link Complex} values to build the exponential value
   * @param e Exponent
   * @return The Array of {@link Complex} values
   */
  public static double[] pow(Double[] a, double e) {
    double[] c = new double[a.length];
    for (int cnt = 0; cnt < c.length; cnt++) {
      c[cnt] = Math.pow(a[cnt], e);
    }
    return c;
  }
}
