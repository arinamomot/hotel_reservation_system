package cvut.ear.hotelreservationsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Room.findByHotel",
                query = "Select r From Room r Where r.hotel = :hotel and not r.disabled")
})
public class Room extends AbstractEntity {
    @Basic(optional = false)
    @Column(nullable = false)
    private int floor;

    @Basic(optional = false)
    @Column(nullable = false)
    private int number;

    @Basic(optional = false)
    @Column(nullable = false)
    private int numberOfBeds;

    @Basic(optional = false)
    @Column(nullable = false)
    private int pricePerNight;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @ManyToMany(mappedBy = "rooms", fetch=FetchType.LAZY)
    List<Reservation> reservations;

    @ManyToOne()
    private Hotel hotel;

    @Basic(optional = false)
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean disabled;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    @JsonBackReference("room-reservation")
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

//    @JsonManagedReference("hotel-room")
    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}
