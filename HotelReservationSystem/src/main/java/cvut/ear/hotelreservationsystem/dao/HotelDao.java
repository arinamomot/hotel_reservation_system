package cvut.ear.hotelreservationsystem.dao;

import cvut.ear.hotelreservationsystem.exception.PersistenceException;
import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.Reservation;
import cvut.ear.hotelreservationsystem.model.Room;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class HotelDao extends BaseDao<Hotel> {

    public HotelDao() {
        super(Hotel.class);
    }

    @Override
    public List<Hotel> findAll() {
        return em.createQuery("Select h From Hotel h Where not h.disabled", Hotel.class).getResultList();
    }

    @Override
    public void remove(Hotel entity) {
        Objects.requireNonNull(entity);
        try {
            entity.setDisabled(true);
            em.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
