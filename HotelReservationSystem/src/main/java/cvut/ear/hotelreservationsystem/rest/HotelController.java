package cvut.ear.hotelreservationsystem.rest;

import cvut.ear.hotelreservationsystem.exception.NotFoundException;
import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.Room;
import cvut.ear.hotelreservationsystem.rest.util.RestUtils;
import cvut.ear.hotelreservationsystem.service.CustomerUserService;
import cvut.ear.hotelreservationsystem.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private static final Logger LOG = LoggerFactory.getLogger(HotelController.class);

    private final HotelService hotelService;

    private final CustomerUserService customerUserService;

    @Autowired
    public HotelController(HotelService hotelService, CustomerUserService customerUserService) {
        this.hotelService = hotelService;
        this.customerUserService = customerUserService;
    }

    /**
     * Gets all hotels
     * @return Returns list of all hotels
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Hotel> getHotels() {
        return hotelService.findAllHotels();
    }

    /**
     * Creates a new hotel. Only an admin can do that.
     * @param hotel Hotel to add
     * @return Returns header for newly created hotel
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createHotel(@RequestBody Hotel hotel) {
        hotelService.addHotel(hotel);
        LOG.debug("Created hotel {}.", hotel);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", hotel.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Gets Hotel object by ID
     * @param id hotel ID
     * @return returns Hotel object
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Hotel getById(@PathVariable Integer id) {
        final Hotel hotel = hotelService.getHotel(id);
        if (hotel == null) {
            throw NotFoundException.create("Hotel", id);
        }
        return hotel;
    }

    /**
     * Returns list of rooms in hotel by hotelId
     * @param id hotel ID
     * @return Returns list of rooms in hotel by hotelId
     */
    @GetMapping(value = "/{id}/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Room> getRoomsByHotel(@PathVariable Integer id) {
        List<Room> rooms =  hotelService.findAllHotelRooms(getById(id));
        //rooms.forEach(room -> room.setHotel(null));
        return rooms;
    }

    /**
     * Gets rooms in hotel by id which can be reserved in timespan between dateFrom and dateTo.
     * @param id hotelId
     * @param dateFrom Date from which we want to have a reservation.
     * @param dateTo Date to which we want to have a reservation.
     * @return Returns list of Rooms
     */
    @GetMapping(value = "/{id}/availableRooms", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Room> getAvailableHotelRooms (@PathVariable Integer id, @RequestParam String dateFrom, @RequestParam String dateTo) {
        LocalDate dateFromD = LocalDate.parse(dateFrom);
        LocalDate dateToD = LocalDate.parse(dateTo);
        List<Room> rooms =  hotelService.getAvailableHotelRooms(getById(id), dateFromD, dateToD);
        //rooms.forEach(room -> room.setHotel(null));
        return rooms;
    }

    /**
     * Adds room to hotel. Only admin user can do this.
     * @param id id of hotel
     * @param room New room
     */
    @PostMapping(value = "/{id}/rooms", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addRoomToHotel(@PathVariable Integer id, @RequestBody Room room) {
        final Hotel hotel = getById(id);
        hotelService.addHotelRoom(hotel, room);
        LOG.debug("Room {} added into hotel {}.", room, hotel);
    }

    /**
     * Removes room from a hotel. Only admin user can do this.
     * @param hotelId hotel id to remove from
     * @param roomId room id to remove
     */
    @DeleteMapping(value = "/{hotelId}/products/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void removeRoomFromHotel(@PathVariable Integer hotelId,
                                          @PathVariable Integer roomId) {
        final Hotel hotel = getById(hotelId);
        final Room toRemove = hotelService.getHotelRoom(roomId);
        if (toRemove == null) {
            throw NotFoundException.create("Product", roomId);
        }
        hotelService.removeHotelRoom(hotel, toRemove);
        LOG.debug("Room {} removed from hotel {}.", toRemove, hotel);
    }

    /**
     * Removes hotel by id.  Only admin user can do this.
     * @param id hotel ID
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void removeHotel(@PathVariable Integer id) {
        final Hotel hotel = getById(id);
        hotelService.removeHotel(hotel);
        LOG.debug("Hotel {} removed", hotel);
    }
}
