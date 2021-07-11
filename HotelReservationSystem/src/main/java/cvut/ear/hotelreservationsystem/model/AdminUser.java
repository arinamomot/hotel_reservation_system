package cvut.ear.hotelreservationsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("ADMIN")
public class AdminUser extends User {

    @ManyToOne()
    private Hotel hotel;

//    @JsonManagedReference("admin-room")
    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
