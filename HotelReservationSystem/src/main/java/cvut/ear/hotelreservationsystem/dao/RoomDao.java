package cvut.ear.hotelreservationsystem.dao;

import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.Room;
import org.springframework.stereotype.Repository;

import javax.persistence.OrderBy;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;

@Repository
public class RoomDao extends BaseDao<Room> {

    public RoomDao() {
        super(Room.class);
    }

    @Override
    public List<Room> findAll() {
        return em.createQuery("Select r From Room r Where not r.disabled", Room.class).getResultList();
    }

    @Override
    public void remove(Room entity) {
        Objects.requireNonNull(entity);
        try {
            entity.setDisabled(true);
            em.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Room> findAllByHotel(Hotel hotel) {
        Objects.requireNonNull(hotel);
        return em.createNamedQuery("Room.findByHotel", Room.class).setParameter("hotel", hotel)
                .getResultList();
    }
}
