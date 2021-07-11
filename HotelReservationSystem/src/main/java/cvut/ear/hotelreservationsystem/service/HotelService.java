package cvut.ear.hotelreservationsystem.service;

import cvut.ear.hotelreservationsystem.dao.HotelDao;
import cvut.ear.hotelreservationsystem.dao.ReservationDao;
import cvut.ear.hotelreservationsystem.dao.RoomDao;
import cvut.ear.hotelreservationsystem.exception.InUseException;
import cvut.ear.hotelreservationsystem.exception.NotFoundException;
import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.Reservation;
import cvut.ear.hotelreservationsystem.model.ReservationStatus;
import cvut.ear.hotelreservationsystem.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelService {
    private final HotelDao dao;
    private final ReservationDao reservationDao;
    private final RoomDao roomDao;

    @Autowired
    public HotelService(HotelDao dao, ReservationDao reservationDao, RoomDao roomDao) {
        this.dao = dao;
        this.roomDao = roomDao;
        this.reservationDao = reservationDao;
    }

    @Transactional(readOnly = true)
    public Hotel getHotel(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public List<Hotel> findAllHotels() {
        return dao.findAll();
    }

    @Transactional
    public void addHotel(Hotel hotel) {
        Objects.requireNonNull(hotel);
        Objects.requireNonNull(hotel.getAddress().getCity());
        Objects.requireNonNull(hotel.getAddress().getCountry());
        Objects.requireNonNull(hotel.getAddress().getHouseNumber());
        Objects.requireNonNull(hotel.getAddress().getPostcode());
        Objects.requireNonNull(hotel.getAddress().getStreet());
        dao.persist(hotel);
    }

    @Transactional
    public void modifyHotel(Hotel hotel) {
        Objects.requireNonNull(hotel);
        dao.persist(hotel);
    }

    @Transactional
    public void removeHotel(Hotel hotel) {
        Objects.requireNonNull(hotel);
        List<Room> listOfRooms = dao.find(hotel.getId()).getRooms();

        boolean canDelete = true;
        for (Room room : listOfRooms) {
            if (!room.getDisabled()) {
                canDelete = false;
                break;
            }
        }

        if (canDelete) {
            dao.remove(dao.find(hotel.getId()));
        } else {
            throw new InUseException("Hotel still has some rooms and cannot be removed");
        }
    }

    @Transactional(readOnly = true)
    public Room getHotelRoom(Integer id) {
        return roomDao.find(id);
    }

    @Transactional
    public List<Room> findAllHotelRooms(Hotel hotel) {
        return roomDao.findAllByHotel(hotel);
    }

    @Transactional
    public void addHotelRoom(Hotel hotel, Room roomToAdd) {
        Objects.requireNonNull(hotel);
        Objects.requireNonNull(roomToAdd);
        roomToAdd.setHotel(hotel);
        roomDao.persist(roomToAdd);
    }

    @Transactional
    public void removeHotelRoom(Hotel hotel, Room roomToRemove) {
        Objects.requireNonNull(hotel);
        Objects.requireNonNull(roomToRemove);
        List<Reservation> listOfRoomReservations = roomDao.find(roomToRemove.getId()).getReservations();
        List<Reservation> listOfActiveReservations = listOfRoomReservations.stream()
                .filter(reservation -> reservation.getCheckOutDate().isAfter(LocalDate.now()) || reservation.getCheckOutDate().isEqual(LocalDate.now()))
                .filter(reservation -> reservation.getStatus() == ReservationStatus.APPROVED || reservation.getStatus() == ReservationStatus.WAITING)
                .collect(Collectors.toList());
        if (listOfActiveReservations.isEmpty()) {
            roomDao.remove(roomToRemove);
        } else {
            throw new InUseException("Room " + roomToRemove.getId().toString() + " is in use");
        }
    }

    @Transactional
    public List<Room> getAvailableHotelRooms(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
        List<Room> hotelRooms = hotel.getRooms();
        List<Reservation> reservationsBetweenDates = reservationDao.findAllBetweenDate(dateFrom, dateTo);
//        List<Reservation> reservations = reservationDao.findAll();
//        List<Reservation> reservationsBetweenDates = reservations.stream()
//                .filter(reservation -> reservation.getStatus() == ReservationStatus.APPROVED || reservation.getStatus() == ReservationStatus.WAITING)
//                .filter(reservation -> (reservation.getCheckInDate().isBefore(dateTo) || reservation.getCheckInDate().isEqual(dateTo)) && (reservation.getCheckOutDate().isAfter(dateFrom) || reservation.getCheckOutDate().isEqual(dateFrom)))
//                .collect(Collectors.toList());
        List<Room> takenRooms = new ArrayList<>();
        for (Reservation reservation : reservationsBetweenDates) {
            takenRooms.addAll(reservation.getRooms());
        }
        List<Room> availableHotelRooms = new ArrayList<>();
        for (Room room : hotelRooms) {
            if (!takenRooms.contains(room)) {
                availableHotelRooms.add(room);
            }
        }
        return availableHotelRooms;
    }


}
