package cvut.ear.hotelreservationsystem.rest;

import cvut.ear.hotelreservationsystem.exception.Exception;
import cvut.ear.hotelreservationsystem.exception.NotFoundException;
import cvut.ear.hotelreservationsystem.model.*;
import cvut.ear.hotelreservationsystem.rest.util.RestUtils;
import cvut.ear.hotelreservationsystem.security.model.UserDetails;
import cvut.ear.hotelreservationsystem.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);

    private final HotelService hotelService;

    private final CustomerUserService customerUserService;

    private final AdminUserService adminUserService;
    private final UserService userService;

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(HotelService hotelService, UserService userService, CustomerUserService customerUserService, AdminUserService adminUserService, ReservationService reservationService) {
        this.hotelService = hotelService;
        this.userService = userService;
        this.customerUserService = customerUserService;
        this.adminUserService = adminUserService;
        this.reservationService = reservationService;
    }

    /**
     * Returns all User reservations for CustomerUser, all reservations to approve/deny for AdminUser without assigned hotel and reservations to approve/deny from single hotel fro AdminUsers with assigned hotel
     * @return Returns List of Reservations
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Reservation> getReservations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            UserDetails userDetails = (UserDetails) auth.getDetails();
            AdminUser user = (AdminUser) userService.findByEmail(userDetails.getUser().getEmail());
            if (user.getHotel() != null) {
                return reservationService.findAllByHotel(user.getHotel()).stream().filter(reservation -> reservation.getStatus() == ReservationStatus.WAITING).collect(Collectors.toList());
            } else {
                return reservationService.findAll().stream().filter(reservation -> reservation.getStatus() == ReservationStatus.WAITING).collect(Collectors.toList());
            }
        } else {
            UserDetails userDetails = (UserDetails) auth.getDetails();
            CustomerUser user = (CustomerUser) userService.findByEmail(userDetails.getUser().getEmail());
            return reservationService.findAllByUser(user);
        }
    }

    /**
     * Returns reservation by its ID
     * @param id reservation ID
     * @return Reservation
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Reservation getById(@PathVariable Integer id) {
        final Reservation reservation = reservationService.find(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) || (!reservation.getCustomer().getEmail().equals(((UserDetails) auth.getDetails()).getUser().getEmail()))) {
            throw new Exception("You don't have permission to view reservation " + id);
        } else {
            return reservation;
        }
    }

    /**
     * Creates new reservation. Only CutomerUser can do this.
     * @param reservation new reservation
     * @return Created header
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> createReservation(@RequestBody Reservation reservation) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getDetails();
        CustomerUser user = (CustomerUser) userService.findByEmail(userDetails.getUser().getEmail());
        customerUserService.createReservation(user, reservation);
        LOG.debug("Created reservation {}.", reservation);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", reservation.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Removes reservation. Only its owner od AdminUser can do this.
     * @param id id of reservation to remove
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public void removeReservation(@PathVariable Integer id) {
        final Reservation reservation = reservationService.find(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getDetails();
        if ((auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            || (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) && reservation.getCustomer().getEmail().equals(userDetails.getUser().getEmail()))
        {
            reservationService.remove(reservation);
        } else {
            throw new Exception("You don't have permission to delete reservation " + id);
        }
    }

    /**
     * Approves reservation. Only AdminUser can do this
     * @param id id of reservation to approve
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{id}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public void approveReservation(@PathVariable Integer id) {
        final Reservation reservation = reservationService.find(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new Exception("You don't have permission to modify reservation " + id);
        } else {
            reservationService.approve(reservation);
        }
    }

    /**
     * Denies reservation. Only AdminUser can do this
     * @param id id of reservation to deny
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{id}/deny", produces = MediaType.APPLICATION_JSON_VALUE)
    public void denyReservation(@PathVariable Integer id) {
        final Reservation reservation = reservationService.find(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new Exception("You don't have permission to modify reservation " + id);
        } else {
            reservationService.deny(reservation);
        }
    }


}
