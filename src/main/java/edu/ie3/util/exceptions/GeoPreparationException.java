/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.exceptions;

/**
 * Exception that is thrown when something went's wrong during geo handling
 *
 * @author Kittl
 * @since 25.10.2018
 */
public class GeoPreparationException extends Exception {
  private static final long serialVersionUID = 7019017638910623822L;

  public GeoPreparationException() {
    super();
  }

  public GeoPreparationException(String message) {
    super(message);
  }

  public GeoPreparationException(String message, Throwable cause) {
    super(message, cause);
  }

  public GeoPreparationException(Throwable cause) {
    super(cause);
  }

  protected GeoPreparationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
