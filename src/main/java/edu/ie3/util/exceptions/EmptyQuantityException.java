package edu.ie3.util.exceptions;

/**
 * Thrown when an operation should be performed on an {@link edu.ie3.util.quantities.EmptyQuantity} which is
 * not allowed.
 *
 * @version 0.1
 * @since 15.09.20
 */
public class EmptyQuantityException extends RuntimeException {

    /**
     * Constructs a {@code EmptyQuantityException} with no detail message.
     */
    public EmptyQuantityException() {
        super();
    }

    /**
     * Constructs a {@code EmptyQuantityException} with the specified
     * detail message.
     *
     * @param message the detail message
     */
    public EmptyQuantityException(String message) {
        super(message);
    }
}
