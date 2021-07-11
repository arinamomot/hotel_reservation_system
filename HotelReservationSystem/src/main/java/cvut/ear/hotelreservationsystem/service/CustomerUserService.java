package cvut.ear.hotelreservationsystem.service;

import cvut.ear.hotelreservationsystem.dao.ReservationDao;
import cvut.ear.hotelreservationsystem.dao.RoomDao;
import cvut.ear.hotelreservationsystem.dao.UserDao;
import cvut.ear.hotelreservationsystem.exception.Exception;
import cvut.ear.hotelreservationsystem.exception.InUseException;
import cvut.ear.hotelreservationsystem.exception.InvalidTimespanException;
import cvut.ear.hotelreservationsystem.exception.ReservationDateException;
import cvut.ear.hotelreservationsystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CustomerUserService {
    private final UserDao dao;
    private final UserService userService;
    private final RoomDao roomDao;
    private final HotelService hotelService;
    private final ReservationDao reservationDao;

    @Autowired
    public CustomerUserService(UserDao dao, ReservationDao reservationDao, UserService userService, RoomDao roomDao, HotelService hotelService) {
        this.dao = dao;
        this.reservationDao = reservationDao;
        this.roomDao = roomDao;
        this.userService = userService;
        this.hotelService = hotelService;
    }

    @Transactional
    public void createCustomerUser(CustomerUser user) throws java.lang.Exception {
        Objects.requireNonNull(user);
        Objects.requireNonNull(user.getAddress());
        Objects.requireNonNull(user.getDateOfBirth());
        Objects.requireNonNull(user.getPhoneNumber());
        Objects.requireNonNull(user.getAddress().getCity());
        Objects.requireNonNull(user.getAddress().getCountry());
        Objects.requireNonNull(user.getAddress().getHouseNumber());
        Objects.requireNonNull(user.getAddress().getPostcode());
        Objects.requireNonNull(user.getAddress().getStreet());
        userService.create(user);
    }

    @Transactional
    public void removeCustomerUser(CustomerUser user) {
        Objects.requireNonNull(user);
        List<Reservation> allReservations = reservationDao.findAll();
        List<Reservation> userActiveReservations = allReservations.stream()
                .filter(r -> r.getCustomer() == user)
                .filter(r -> r.getCheckInDate().isAfter(LocalDate.now()))
                .filter(r -> r.getStatus() == ReservationStatus.APPROVED  || r.getStatus() == ReservationStatus.WAITING)
                .collect(Collectors.toList());
        if (userActiveReservations.isEmpty()) {
            userService.remove(user);
        } else {
            throw new InUseException("Current user has active reservations and cannot be removed");
        }
    }

    @Transactional
    public void createReservation(CustomerUser customer, Reservation reservation) {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(reservation);
        reservation.setReservationCreatedDate(LocalDateTime.now());
        List<Room> reservationRooms = reservation.getRooms();

        if (reservationRooms.isEmpty()) {
            throw new Exception("Reservation must contain some rooms");
        }

        if (!userService.exists(customer.getEmail())) {
            throw new Exception("User must exist");
        }

        List<Room> actualRooms = new ArrayList<>();

        for (Room room : reservationRooms) {
            Room roomToAdd = roomDao.find(room.getId());
            if (!actualRooms.contains(roomToAdd)) {
                actualRooms.add(roomToAdd);
            }
        }

        reservation.setRooms(actualRooms);

        reservationRooms = actualRooms;

        reservation.setCustomer(customer);

        List<Room> availableRooms = hotelService.getAvailableHotelRooms(hotelService.getHotel(reservationRooms.get(0).getHotel().getId()), reservation.getCheckInDate(), reservation.getCheckOutDate());


        boolean roomsAreFree = true;
        for (Room room : reservationRooms) {
            if (!availableRooms.contains(room)) {
                roomsAreFree = false;
                break;
            }
        }
        if (!roomsAreFree) {
            throw new Exception("Reservation rooms must be available");
        }

        int reservationRoomsFreeSpace = reservation.getRooms().stream().mapToInt(Room::getNumberOfBeds).sum();

        if (reservationRoomsFreeSpace < reservation.getNumberOfAccommodated()) {
            throw new Exception("Rooms must have enough beds for all accommodated");
        }

        if (reservation.getCheckInDate().isAfter(reservation.getCheckOutDate()) || reservation.getCheckInDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new InvalidTimespanException("Reservation timespan must be in the future and checkin date must be before checkout date");
        }

        reservation.setStatus(ReservationStatus.WAITING);

        reservationDao.persist(reservation);
    }

    @Transactional
    public void removeReservation(CustomerUser customer, Reservation reservation) {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(reservation);
        if (reservation.getCheckInDate().isAfter(LocalDate.now()) && (reservation.getStatus() == ReservationStatus.WAITING || reservation.getStatus() == ReservationStatus.APPROVED)) {
            reservation.setStatus(ReservationStatus.DELETED);
            reservationDao.update(reservation);
        } else {
            throw new ReservationDateException("Reservation " + reservation.getId().toString() + " cannot be altered after its SignIn date");
        }
    }

}
