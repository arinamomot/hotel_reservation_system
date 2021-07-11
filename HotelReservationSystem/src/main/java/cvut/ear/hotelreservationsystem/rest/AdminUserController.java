package cvut.ear.hotelreservationsystem.rest;

import cvut.ear.hotelreservationsystem.model.AdminUser;
import cvut.ear.hotelreservationsystem.model.CustomerUser;
import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.User;
import cvut.ear.hotelreservationsystem.rest.util.RestUtils;
import cvut.ear.hotelreservationsystem.security.model.AuthenticationToken;
import cvut.ear.hotelreservationsystem.service.AdminUserService;
import cvut.ear.hotelreservationsystem.service.CustomerUserService;
import cvut.ear.hotelreservationsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private final CustomerUserService customerUserService;
    private final AdminUserService adminUserService;
    private final cvut.ear.hotelreservationsystem.service.UserService userService;

    @Autowired
    public AdminUserController(CustomerUserService customerUserService, AdminUserService adminUserService, cvut.ear.hotelreservationsystem.service.UserService userService) {
        this.customerUserService = customerUserService;
        this.adminUserService = adminUserService;
        this.userService = userService;
    }

    /**
     * Creates new admin account. Can be used only by admins.
     * @param user AdminUser object
     * @throws Exception Throws exception if user already exists
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/addAdmin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addNewAdmin(@RequestBody AdminUser user) throws Exception {
        adminUserService.createAdminUser(user);
    }

    /**
     * Gets currently logged user
     * @param principal Authentication token
     * @return Returns current User object
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent(Principal principal) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        return auth.getPrincipal().getUser();
    }

    /**
     * Returns all users. Requires Admin account.
     * @return Returns list of all User objects in DB
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/allUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Assigns hotel to admin. Viewing reservations then shows only those for that admin.
     * @param userId Admin user ID
     * @param hotelId hotel ID
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping(value = "/hotelAssign")
    public void assignHotel(@RequestParam String userId, @RequestParam String hotelId) {
        int userIdInt = Integer.parseInt(userId);
        int hotelIdInt = Integer.parseInt(hotelId);
        adminUserService.assignHotel(userIdInt, hotelIdInt);
    }

    /**
     * Remove hotel assignation from Admin user
     * @param userId admin user ID
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/hotelAssign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void clearHotel(@RequestParam String userId) {
        int userIdInt = Integer.parseInt(userId);
        adminUserService.clearHotel(userIdInt);
    }
}
