package cvut.ear.hotelreservationsystem.service;

import cvut.ear.hotelreservationsystem.dao.ReservationDao;
import cvut.ear.hotelreservationsystem.dao.UserDao;
import cvut.ear.hotelreservationsystem.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserDao dao;
    private final ReservationDao reservationDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao dao, PasswordEncoder passwordEncoder, ReservationDao reservationDao) {
        this.dao = dao;
        this.reservationDao = reservationDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Transactional
    public User findById(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public List<User> findAll() {
        return dao.findAll();
    }

    @Transactional
    public boolean exists(String email) {
        Boolean test = dao.findByEmail(email) != null;
        return test;
    }

    @Transactional
    public void create(User user) throws Exception {
        Objects.requireNonNull(user);
        if (exists(user.getEmail())) {
            throw new Exception("User " + user.getEmail() + " already exists");
        }
        user.encodePassword(passwordEncoder);
        dao.persist(user);
    }

    @Transactional
    public void remove(User user) {
        Objects.requireNonNull(user);
        dao.remove(user);
    }

}
