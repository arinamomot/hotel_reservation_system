package cvut.ear.hotelreservationsystem.exception;

public class PersistenceException extends Exception {

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
