package cvut.ear.hotelreservationsystem.environment;

import cvut.ear.hotelreservationsystem.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static double randomInt(int min, int max) {
        int res;
        do {
            res = RAND.nextInt(max);
        } while (res < min);
        return res;
    }

    public static LocalDate randomLocalDateIn() {
        long minDay = LocalDate.of(2020, 8, 1).toEpochDay();
        long maxDay = LocalDate.of(2020, 9, 30).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static LocalDate randomLocalDateOut() {
        long minDay = LocalDate.of(2020, 10, 1).toEpochDay();
        long maxDay = LocalDate.of(2020, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static User generateUser() {
        final User user = new User();
        user.setFirstName("FirstName" + randomInt());
        user.setLastName("LastName" + randomInt());
        user.setEmail("LastName" + randomInt() + "@gmail.com");
        user.setPassword(Integer.toString(randomInt()));
        return user;
    }

    public static CustomerUser generateCustomerUser() {
        final CustomerUser user = new CustomerUser();
        user.setFirstName("FirstName" + randomInt());
        user.setLastName("LastName" + randomInt());
        user.setEmail("LastName" + randomInt() + "@gmail.com");
        user.setPassword(Integer.toString(randomInt()));
        user.setDateOfBirth(new Date());
        user.setPhoneNumber("+420" +randomInt());
        return user;
    }

    public static Reservation generateReservation() {
        final Reservation reservation = new Reservation();
        reservation.setNumberOfAccommodated(randomInt());
        reservation.setCheckInDate(randomLocalDateIn());
        reservation.setCheckOutDate(randomLocalDateOut());
        reservation.setReservationCreatedDate(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.WAITING);
        return reservation;
    }

    public static Hotel generateHotel() {
        final Hotel hotel = new Hotel();
        hotel.setName("Hotel" + randomInt());
        return hotel;
    }

    public static Room generateRoom() {
        final Room room = new Room();
        room.setType(RoomType.STANDARD);
        room.setFloor(randomInt());
        room.setNumber(randomInt());
        room.setNumberOfBeds(randomInt());
        room.setPricePerNight(randomInt());
        return room;
    }

    public static Address generateAddress() {
        final Address address = new Address();
        address.setCity("City" + randomInt());
        address.setCountry("Country" + randomInt());
        address.setHouseNumber(Integer.toString(randomInt()));
        address.setPostcode(Integer.toString(randomInt()));
        address.setStreet("Street" + randomInt());
        return address;
    }
}
