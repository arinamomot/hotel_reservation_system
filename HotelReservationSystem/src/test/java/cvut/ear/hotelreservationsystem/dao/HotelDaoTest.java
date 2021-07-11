package cvut.ear.hotelreservationsystem.dao;

import cvut.ear.hotelreservationsystem.HotelreservationsystemApplication;
import cvut.ear.hotelreservationsystem.environment.Generator;
import cvut.ear.hotelreservationsystem.exception.PersistenceException;
import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.Reservation;
import cvut.ear.hotelreservationsystem.model.Room;
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
public class HotelDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private HotelDao sut;

    @Test
    public void persistSavesSpecifiedInstance() {
        final Hotel hotel = Generator.generateHotel();
        sut.persist(hotel);
        assertNotNull(hotel.getId());

        final Hotel result = em.find(Hotel.class, hotel.getId());
        assertNotNull(result);
        assertEquals(hotel.getId(), result.getId());
        assertEquals(hotel.getName(), result.getName());
    }

    @Test
    public void findRetrievesInstanceByIdentifier() {
        final Hotel hotel = Generator.generateHotel();
        em.persistAndFlush(hotel);
        assertNotNull(hotel.getId());

        final Hotel result = sut.find(hotel.getId());
        assertNotNull(result);
        assertEquals(hotel.getId(), result.getId());
        assertEquals(hotel.getName(), result.getName());
    }

    @Test
    public void findAllRetrievesAllInstancesOfType() {
        final Hotel hotel01 = Generator.generateHotel();
        em.persistAndFlush(hotel01);
        final Hotel hotel02 = Generator.generateHotel();
        em.persistAndFlush(hotel02);

        final List<Hotel> result = sut.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(hotel01.getId())));
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(hotel02.getId())));
    }

    @Test
    public void updateUpdatesExistingInstance() {
        final Hotel hotel = Generator.generateHotel();
        em.persistAndFlush(hotel);

        final Hotel update = new Hotel();
        update.setId(hotel.getId());
        final String newName = "New hotel name";
        update.setName(newName);
        sut.update(update);

        final Hotel result = sut.find(hotel.getId());
        assertNotNull(result);
        assertEquals(hotel.getName(), result.getName());
    }

    @Test
    public void removeRemovesSpecifiedInstance() {
        final Hotel hotel = Generator.generateHotel();
        em.persistAndFlush(hotel);
        assertNotNull(em.find(Hotel.class, hotel.getId()));
        em.detach(hotel);

        sut.remove(hotel);
        assertTrue(em.find(Hotel.class, hotel.getId()).isDisabled());
    }

//    @Test
//    public void removeDoesNothingWhenInstanceDoesNotExist() {
//        final Hotel hotel = Generator.generateHotel();
//        hotel.setId(123);
//        assertNull(em.find(Hotel.class, hotel.getId()));
//
//        sut.remove(hotel);
//        assertNull(em.find(Hotel.class, hotel.getId()));
//    }

    @Test(expected = PersistenceException.class)
    public void exceptionOnPersistInWrappedInPersistenceException() {
        final Hotel hotel = Generator.generateHotel();
        em.persistAndFlush(hotel);
        em.remove(hotel);
        sut.update(hotel);
    }

    @Test
    public void existsReturnsTrueForExistingIdentifier() {
        final Hotel hotel = Generator.generateHotel();
        em.persistAndFlush(hotel);
        assertTrue(sut.exists(hotel.getId()));
        assertFalse(sut.exists(-1));
    }
}
