package cvut.ear.hotelreservationsystem.dao;

import cvut.ear.hotelreservationsystem.HotelreservationsystemApplication;
import cvut.ear.hotelreservationsystem.environment.Generator;
import cvut.ear.hotelreservationsystem.exception.PersistenceException;
import cvut.ear.hotelreservationsystem.model.Reservation;
import cvut.ear.hotelreservationsystem.model.ReservationStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = HotelreservationsystemApplication.class)
public class ReservationDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ReservationDao sut;


    @Test
    public void persistSavesSpecifiedInstance() {
        final Reservation reservation = Generator.generateReservation();
        sut.persist(reservation);
        assertNotNull(reservation.getId());

        final Reservation result = em.find(Reservation.class, reservation.getId());
        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
        assertEquals(reservation.getStatus(), reservation.getStatus());
    }

    @Test
    public void findRetrievesInstanceByIdentifier() {
        final Reservation reservation = Generator.generateReservation();
        em.persistAndFlush(reservation);
        assertNotNull(reservation.getId());

        final Reservation result = sut.find(reservation.getId());
        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
        assertEquals(reservation.getStatus(), reservation.getStatus());
    }

    @Test
    public void findAllRetrievesAllInstancesOfType() {
        final Reservation reservation01 = Generator.generateReservation();
        em.persistAndFlush(reservation01);
        final Reservation reservation02 = Generator.generateReservation();
        em.persistAndFlush(reservation02);

        final List<Reservation> result = sut.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(reservation01.getId())));
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(reservation02.getId())));
    }

    @Test
    public void updateUpdatesExistingInstance() {
        final Reservation reservation = Generator.generateReservation();
        em.persistAndFlush(reservation);

        final Reservation update = new Reservation();
        update.setId(reservation.getId());
        final ReservationStatus newStatus = ReservationStatus.APPROVED;
        update.setStatus(newStatus);
        sut.update(update);

        final Reservation result = sut.find(reservation.getId());
        assertNotNull(result);
        assertEquals(reservation.getStatus(), reservation.getStatus());
    }

    @Test
    public void removeRemovesSpecifiedInstance() {
        final Reservation reservation = Generator.generateReservation();
        em.persistAndFlush(reservation);
        assertNotNull(em.find(Reservation.class, reservation.getId()));
        em.detach(reservation);

        sut.remove(reservation);
        assertSame(em.find(Reservation.class, reservation.getId()).getStatus(), ReservationStatus.DELETED);
    }

    @Test(expected = PersistenceException.class)
    public void exceptionOnPersistInWrappedInPersistenceException() {
        final Reservation reservation = Generator.generateReservation();
        em.persistAndFlush(reservation);
        em.remove(reservation);
        sut.update(reservation);
    }

    @Test
    public void existsReturnsTrueForExistingIdentifier() {
        final Reservation reservation = Generator.generateReservation();
        em.persistAndFlush(reservation);
        assertTrue(sut.exists(reservation.getId()));
        assertFalse(sut.exists(-1));
    }

//    @Test
//    public void findAllBetweenDateReturnsReservationsBetweenDates() {
//        final LocalDate dateFrom = LocalDate.of(2020, 1, 1);
//        final LocalDate dateTo = LocalDate.of(2020, 1, 20);;
//
//        generateReservations(dateFrom.plusDays(1), dateTo.minusDays(1), 10);
//        final List<Reservation> result = dao.findAllBetweenDate(dateFrom, dateTo);
//        assertEquals(10, result.size());
//
//        reservations.sort(Comparator.comparing(Reservation::getId));
//        result.sort(Comparator.comparing(Reservation::getId));
//
//        for (int i = 0; i < reservations.size(); i++) {
//            assertEquals(reservations.get(i).getId(), result.get(i).getId());
//        }
//    }


//    private void generateReservations(LocalDate dateFrom, LocalDate dateTo, int amount) {
//        for (int i = 0; i < amount; i++) {
//            final Reservation r = Generator.generateReservation();
//            r.setCheckInDate(dateFrom);
//            r.setCheckOutDate(dateTo);
//            final Room room = Generator.generateRoom();
//            r.addRoom(room);
//            try {
//                em.persist(r);
//            } catch (NullPointerException ignored){}
//        }
//    }


}
