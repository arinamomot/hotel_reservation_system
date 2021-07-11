package cvut.ear.hotelreservationsystem.exception;

/**
 * Indicates that a resource has invalid timespan (created in the past etc.).
 */
public class InvalidTimespanException extends Exception {

    public InvalidTimespanException(String message) {
        super(message);
    }

    public InvalidTimespanException(String message, Throwable cause) {
        super(message, cause);
    }
}
