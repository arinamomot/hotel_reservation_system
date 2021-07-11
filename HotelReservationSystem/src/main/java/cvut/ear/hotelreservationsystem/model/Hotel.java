package cvut.ear.hotelreservationsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Hotel extends AbstractEntity {
    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "hotel", fetch=FetchType.LAZY)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", fetch=FetchType.LAZY)
    private List<AdminUser> administrators;

    @Basic(optional = false)
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean disabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonBackReference("hotel-room")
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @JsonBackReference("admin-room")
    public List<AdminUser> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<AdminUser> administrators) {
        this.administrators = administrators;
    }
}
