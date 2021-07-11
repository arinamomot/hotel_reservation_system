package cvut.ear.hotelreservationsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Reservation.findByStatus",
                query = "Select r From Reservation r Where r.status = :status"),
        @NamedQuery(name = "Reservation.findByUser",
                query = "Select r From Reservation r Where r.customer = :user"),
        @NamedQuery(name = "Reservation.findAllBetweenDate",
                query = "Select r " +
                        "From Reservation r " +
                        "Where (r.checkOutDate >= :dateFrom and r.checkInDate <= :dateTo) and not r.status = cvut.ear.hotelreservationsystem.model.ReservationStatus.DECLINED and not r.status = cvut.ear.hotelreservationsystem.model.ReservationStatus.DELETED")
})
public class Reservation extends AbstractEntity{
    @Basic(optional = false)
    @Column(nullable = false)
    private int numberOfAccommodated;

    @Basic(optional = false)
    @Column(nullable = false)
    private LocalDateTime reservationCreatedDate;

    @Basic(optional = false)
    @Column(nullable = false)
    private LocalDate checkInDate;

    @Basic(optional = false)
    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    private CustomerUser customer;

    @ManyToMany()
    @JoinTable(
            name = "reservation_room",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    List<Room> rooms;

    public int getNumberOfAccommodated() {
        return numberOfAccommodated;
    }

    public void setNumberOfAccommodated(int numberOfAccommodated) {
        this.numberOfAccommodated = numberOfAccommodated;
    }

    public LocalDateTime getReservationCreatedDate() {
        return reservationCreatedDate;
    }

    public void setReservationCreatedDate(LocalDateTime reservationCreatedDate) {
        this.reservationCreatedDate = reservationCreatedDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

//    @JsonManagedReference("room-reservation")
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void addRoom(Room room){
        Objects.requireNonNull(room);
        if (rooms == null) {
            rooms = new ArrayList<Room>();
        }
        rooms.add(room);
    }

    public void removeRoom(Room room){
        Objects.requireNonNull(room);
        if (rooms == null) {
            return;
        }
        rooms.removeIf(c -> Objects.equals(c.getId(), room.getId()));
    }

//    @JsonManagedReference("customer-reservation")
    public CustomerUser getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerUser customer) {
        this.customer = customer;
    }
}
