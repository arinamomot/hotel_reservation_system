package cvut.ear.hotelreservationsystem.rest;

import cvut.ear.hotelreservationsystem.model.AdminUser;
import cvut.ear.hotelreservationsystem.model.CustomerUser;
import cvut.ear.hotelreservationsystem.model.Reservation;
import cvut.ear.hotelreservationsystem.model.User;
import cvut.ear.hotelreservationsystem.rest.util.RestUtils;
import cvut.ear.hotelreservationsystem.security.model.AuthenticationToken;
import cvut.ear.hotelreservationsystem.service.AdminUserService;
import cvut.ear.hotelreservationsystem.service.CustomerUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class CustomerUserController {

    private final CustomerUserService customerUserService;
    private final AdminUserService adminUserService;

    @Autowired
    public CustomerUserController(CustomerUserService customerUserService, AdminUserService adminUserService) {
        this.customerUserService = customerUserService;
        this.adminUserService = adminUserService;
    }

    /**
     * Registers new customer user
     * @param user CustomerUser object to register
     * @return Returns that the user was created
     * @throws Exception Exception thrown when the username alreade exists
     */
    @PreAuthorize("(true)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody CustomerUser user) throws Exception {
        user.setReservations(new ArrayList<Reservation>());
        customerUserService.createCustomerUser(user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Returns currently logged-in user
     * @param principal auth token
     * @return Returns User object of currently logged-in user
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent(Principal principal) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        return auth.getPrincipal().getUser();
    }
}
