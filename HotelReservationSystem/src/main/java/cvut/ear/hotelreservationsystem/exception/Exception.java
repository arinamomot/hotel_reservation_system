package cvut.ear.hotelreservationsystem.exception;

/**
 * Base for all application-specific exceptions.
 */
public class Exception extends RuntimeException {

    public Exception() {
    }

    public Exception(String message) {
        super(message);
    }

    public Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public Exception(Throwable cause) {
        super(cause);
    }
}
