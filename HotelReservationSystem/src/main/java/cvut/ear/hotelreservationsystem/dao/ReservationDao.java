package cvut.ear.hotelreservationsystem.dao;

import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.Reservation;
import cvut.ear.hotelreservationsystem.model.ReservationStatus;
import cvut.ear.hotelreservationsystem.model.Room;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationDao extends BaseDao<Reservation> {

    public ReservationDao() {
        super(Reservation.class);
    }

    @Override
    public List<Reservation> findAll() {
        return em.createQuery("Select r From Reservation r Where not r.status = cvut.ear.hotelreservationsystem.model.ReservationStatus.DELETED", Reservation.class).getResultList();
    }

    @Override
    public void remove(Reservation entity) {
        Objects.requireNonNull(entity);
        try {
            entity.setStatus(ReservationStatus.DELETED);
            em.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Reservation> findAllBetweenDate(LocalDate dateFrom, LocalDate dateTo) {
        Objects.requireNonNull(dateFrom);
        Objects.requireNonNull(dateTo);
        return em.createNamedQuery("Reservation.findAllBetweenDate", Reservation.class)
                .setParameter("dateFrom", dateFrom).setParameter("dateTo", dateTo)
                .getResultList();
    }
}
