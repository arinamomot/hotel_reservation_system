package cvut.ear.hotelreservationsystem.rest.handler;

import cvut.ear.hotelreservationsystem.exception.*;
//import cvut.ear.hotelreservationsystem.security.SecurityUtils;
import cvut.ear.hotelreservationsystem.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.PersistenceContext;
import javax.persistence.PessimisticLockException;
import javax.servlet.http.HttpServletRequest;
import java.lang.Exception;

/**
 * Exception handlers for REST controllers.
 * <p>
 * The general pattern should be that unless an exception can be handled in a more appropriate place it bubbles up to a
 * REST controller which originally received the request. There, it is caught by this handler, logged and a reasonable
 * error message is returned to the user.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static void logException(RuntimeException ex) {
        LOG.error("Exception caught:", ex);
    }

    private static ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorInfo> persistenceException(HttpServletRequest request, PersistenceException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e.getCause()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> resourceNotFound(HttpServletRequest request, NotFoundException e) {
        // Not necessary to log NotFoundException, they may be quite frequent and do not represent an issue with the application
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> insufficientAmount(HttpServletRequest request, Exception e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InUseException.class)
    public ResponseEntity<ErrorInfo> validation(HttpServletRequest request, InUseException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> accessDenied(HttpServletRequest request, AccessDeniedException e) {
        // Spring Boot throws Access Denied when trying to access a secured method with anonymous authentication token
        // We want to let such exception out, so that it is handled by the authentication entry point (which returns 401)
        if (SecurityUtils.isAuthenticatedAnonymously()) {
            throw e;
        }
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(javax.persistence.PersistenceException.class)
    public ResponseEntity<ErrorInfo> persistenceJavaxExceptionException(HttpServletRequest request, javax.persistence.PersistenceException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(cvut.ear.hotelreservationsystem.exception.Exception.class)
    public ResponseEntity<ErrorInfo> cartAccessException(HttpServletRequest request, cvut.ear.hotelreservationsystem.exception.Exception e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidTimespanException.class)
    public ResponseEntity<ErrorInfo> invalidTimespanException(HttpServletRequest request, InvalidTimespanException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReservationDateException.class)
    public ResponseEntity<ErrorInfo> reservationDateException(HttpServletRequest request, ReservationDateException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

}
