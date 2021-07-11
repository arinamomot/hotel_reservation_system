package cvut.ear.hotelreservationsystem.dao;

import cvut.ear.hotelreservationsystem.HotelreservationsystemApplication;
import cvut.ear.hotelreservationsystem.environment.Generator;
import cvut.ear.hotelreservationsystem.exception.PersistenceException;
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
public class RoomDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private RoomDao sut;

    @Test
    public void persistSavesSpecifiedInstance() {
        final Room room = Generator.generateRoom();
        sut.persist(room);
        assertNotNull(room.getId());

        final Room result = em.find(Room.class, room.getId());
        assertNotNull(result);
        assertEquals(room.getId(), result.getId());
        assertEquals(room.getNumber(), result.getNumber());
    }

    @Test
    public void findRetrievesInstanceByIdentifier() {
        final Room room = Generator.generateRoom();
        em.persistAndFlush(room);
        assertNotNull(room.getId());

        final Room result = sut.find(room.getId());
        assertNotNull(result);
        assertEquals(room.getId(), result.getId());
        assertEquals(room.getNumber(), result.getNumber());
    }

    @Test
    public void findAllRetrievesAllInstancesOfType() {
        final Room room01 = Generator.generateRoom();
        em.persistAndFlush(room01);
        final Room room02 = Generator.generateRoom();
        em.persistAndFlush(room02);

        final List<Room> result = sut.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(room01.getId())));
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(room02.getId())));
    }

    @Test
    public void updateUpdatesExistingInstance() {
        final Room room = Generator.generateRoom();
        em.persistAndFlush(room);

        final Room update = new Room();
        update.setId(room.getId());
        final int newNumber = 102;
        update.setNumber(newNumber);
        sut.update(update);

        final Room result = sut.find(room.getId());
        assertNotNull(result);
        assertEquals(room.getNumber(), result.getNumber());
    }

    @Test
    public void removeRemovesSpecifiedInstance() {
        final Room room = Generator.generateRoom();
        em.persistAndFlush(room);
        assertNotNull(em.find(Room.class, room.getId()));
        em.detach(room);

        sut.remove(room);
        assertTrue(em.find(Room.class, room.getId()).getDisabled());
    }

    @Test(expected = PersistenceException.class)
    public void exceptionOnPersistInWrappedInPersistenceException() {
        final Room room = Generator.generateRoom();
        em.persistAndFlush(room);
        em.remove(room);
        sut.update(room);
    }

    @Test
    public void existsReturnsTrueForExistingIdentifier() {
        final Room room = Generator.generateRoom();
        em.persistAndFlush(room);
        assertTrue(sut.exists(room.getId()));
        assertFalse(sut.exists(-1));
    }
}
