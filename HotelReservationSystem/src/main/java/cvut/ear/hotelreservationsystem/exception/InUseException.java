package cvut.ear.hotelreservationsystem.exception;

/**
 * Indicates that a resource is in use and cannot be safely deleted.
 */
public class InUseException extends Exception {

    public InUseException(String message) {
        super(message);
    }

    public InUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
