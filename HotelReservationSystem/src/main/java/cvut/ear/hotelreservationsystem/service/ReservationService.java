package cvut.ear.hotelreservationsystem.service;

import cvut.ear.hotelreservationsystem.dao.ReservationDao;
import cvut.ear.hotelreservationsystem.dao.RoomDao;
import cvut.ear.hotelreservationsystem.model.CustomerUser;
import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.Reservation;
import cvut.ear.hotelreservationsystem.model.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationDao dao;
    private final RoomDao roomDao;

    @Autowired
    public ReservationService(ReservationDao dao, RoomDao roomDao) {
        this.dao = dao;
        this.roomDao = roomDao;
    }

    @Transactional(readOnly = true)
    public Reservation find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public List<Reservation> findAll() {
        return dao.findAll();
    }

    @Transactional
    public List<Reservation> findAllByUser(CustomerUser user) {
        List<Reservation> reservations = dao.findAll();
        List<Reservation> reservationsByUser = reservations.stream().filter(reservation -> reservation.getCustomer() == user).collect(Collectors.toList());
        return reservationsByUser;
    }

    @Transactional
    public List<Reservation> findAllByHotel(Hotel hotel) {
        List<Reservation> reservations = dao.findAll();
        List<Reservation> reservationsByHotel = reservations.stream().filter(reservation -> reservation.getRooms().get(0).getHotel() == hotel).collect(Collectors.toList());
        return reservationsByHotel;
    }

    @Transactional
    public void update(Reservation reservation) {
        Objects.requireNonNull(reservation);
        dao.update(reservation);
    }

    @Transactional
    public void approve(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservation.setStatus(ReservationStatus.APPROVED);
        dao.update(reservation);
    }

    @Transactional
    public void deny(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservation.setStatus(ReservationStatus.DECLINED);
        dao.update(reservation);
    }

    @Transactional
    public void remove(Reservation reservation) {
        Objects.requireNonNull(reservation);
        dao.remove(reservation);
    }

}
