package cvut.ear.hotelreservationsystem.exception;

/**
 * Indicates that a resource is in use and cannot be safely deleted.
 */
public class ReservationDateException extends Exception {

    public ReservationDateException(String message) {
        super(message);
    }

    public ReservationDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
