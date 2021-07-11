package cvut.ear.hotelreservationsystem.service;

import cvut.ear.hotelreservationsystem.dao.ReservationDao;
import cvut.ear.hotelreservationsystem.dao.UserDao;
import cvut.ear.hotelreservationsystem.exception.Exception;
import cvut.ear.hotelreservationsystem.exception.InUseException;
import cvut.ear.hotelreservationsystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminUserService {
    private final UserDao dao;
    private final ReservationDao reservationDao;
    private final UserService userService;
    private final HotelService hotelService;

    @Autowired
    public AdminUserService(UserDao dao, ReservationDao reservationDao, UserService userService, HotelService hotelService) {
        this.dao = dao;
        this.reservationDao = reservationDao;
        this.userService = userService;
        this.hotelService = hotelService;
    }

    @Transactional
    public void createAdminUser(AdminUser user) throws java.lang.Exception {
        Objects.requireNonNull(user);
        Objects.requireNonNull(user.getEmail());
        Objects.requireNonNull(user.getPassword());
        Objects.requireNonNull(user.getFirstName());
        Objects.requireNonNull(user.getLastName());
        userService.create(user);
    }

    @Transactional
    public void assignHotel(int userId, int hotelId) {
        User userCheck = userService.findById(userId);
        if (userCheck instanceof CustomerUser) {
            throw new Exception("Non-admin user cannot have assigned hotel");
        }
        AdminUser user = (AdminUser) userCheck;
        Hotel hotel = hotelService.getHotel(hotelId);
        Objects.requireNonNull(user);
        Objects.requireNonNull(hotel);
        user.setHotel(hotel);
        dao.update(user);
    }

    @Transactional
    public void clearHotel(int userId) {
        User userCheck = userService.findById(userId);
        if (userCheck instanceof CustomerUser) {
            throw new Exception("Non-admin user cannot have assigned hotel");
        }
        AdminUser user = (AdminUser) userCheck;
        Objects.requireNonNull(user);
        user.setHotel(null);
        dao.update(user);
    }

    @Transactional
    public void removeAdminUser(CustomerUser user) {
        Objects.requireNonNull(user);
        userService.remove(user);
    }

    @Transactional
    public void confirmReservation(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservation.setStatus(ReservationStatus.APPROVED);
        reservationDao.update(reservation);
    }

    @Transactional
    public void refuseReservation(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservation.setStatus(ReservationStatus.DECLINED);
        reservationDao.update(reservation);
    }
}
