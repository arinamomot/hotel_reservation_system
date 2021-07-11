package cvut.ear.hotelreservationsystem.service;

import cvut.ear.hotelreservationsystem.dao.HotelDao;
import cvut.ear.hotelreservationsystem.dao.ReservationDao;
import cvut.ear.hotelreservationsystem.dao.UserDao;
import cvut.ear.hotelreservationsystem.environment.Generator;
import cvut.ear.hotelreservationsystem.model.Reservation;
import cvut.ear.hotelreservationsystem.model.Room;
import cvut.ear.hotelreservationsystem.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class HotelServiceTest {
//    @Mock
//    private HotelDao hotelDaoMock;
//    @Mock
//    private ReservationDao reservationDaoMock;
//
//    private HotelService hotelService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        this.hotelService = new HotelService(hotelDaoMock, reservationDaoMock);
//    }
//
//    @Test
//    public void findAllBetweenDateReturnsReservationsBetweenDates() {
//        final LocalDate dateFrom = Generator.randomLocalDateIn();
//        final LocalDate dateTo = Generator.randomLocalDateOut();
//
//        final List<Reservation> reservations = generateReservations();
//        final List<Reservation> result = hotelService.getAvailableHotelRooms(hotel, dateFrom, dateTo);
//        assertEquals(reservations.size(), result.size());
//
//        reservations.sort(Comparator.comparing(Reservation::getId));
//        result.sort(Comparator.comparing(Reservation::getId));
//
//        for (int i = 0; i < reservations.size(); i++) {
//            assertEquals(reservations.get(i).getId(), result.get(i).getId());
//        }
//    }
//
//    private List<Reservation> generateReservations() {
//        final List<Reservation> reservations = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            final Reservation r = Generator.generateReservation();
//            final Room room = Generator.generateRoom();
//            r.addRoom(room);
//            reservations.add(r);
//            try {
//                em.persist(r);
//            } catch (NullPointerException ignored){}
//        }
//        return reservations;
//    }

}
