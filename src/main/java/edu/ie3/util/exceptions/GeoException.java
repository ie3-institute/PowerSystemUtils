/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.exceptions;

public class GeoException extends Exception {
  public GeoException(String message) {
    super(message);
  }

  public GeoException(String message, Throwable cause) {
    super(message, cause);
  }
}
