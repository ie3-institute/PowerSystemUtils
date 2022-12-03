/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.exceptions;

public class QuantityException extends RuntimeException {

  /** Constructs a {@code QuantityException} with no detail message. */
  public QuantityException() {
    super();
  }

  /**
   * Constructs a {@code QuantityException} with the specified detail message.
   *
   * @param message the detail message
   */
  public QuantityException(String message) {
    super(message);
  }

  /**
   * Constructs a {@code QuantityException} with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public QuantityException(String message, Throwable cause) {
    super(message, cause);
  }
}
