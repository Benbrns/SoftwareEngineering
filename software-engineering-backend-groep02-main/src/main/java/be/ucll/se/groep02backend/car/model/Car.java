package be.ucll.se.groep02backend.car.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.user.model.User;
// JPA imports
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
// Validation imports
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "car")
public class Car {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private Set<Rental> rentals;

    public String model;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    private short numberOfSeats;

    private short numberOfChildSeats;

    private boolean foldingRearSeat;

    private boolean towBar;

    public Car() {
    }

    public Car(String brand, String model, String type, String licensePlate, short numberOfSeats,
            short numberOfChildSeats, boolean foldingRearSeat, boolean towBar) {
        setBrand(brand);
        setModel(model);
        setType(type);
        setLicensePlate(licensePlate);
        setNumberOfSeats(numberOfSeats);
        setNumberOfChildSeats(numberOfChildSeats);
        setFoldingRearSeat(foldingRearSeat);
        setTowBar(towBar);
    }

    public Car(String brand, String model, String type, String licensePlate, short numberOfSeats,
            short numberOfChildSeats, boolean foldingRearSeat, boolean towBar, User user) {
        setBrand(brand);
        setModel(model);
        setType(type);
        setLicensePlate(licensePlate);
        setNumberOfSeats(numberOfSeats);
        setNumberOfChildSeats(numberOfChildSeats);
        setFoldingRearSeat(foldingRearSeat);
        setTowBar(towBar);
        setUser(user);
    }

    // Getters
    public User getUser() {
        return this.user;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public String getType() {
        return this.type;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public short getNumberOfSeats() {
        return this.numberOfSeats;
    }

    public short getNumberOfChildSeats() {
        return this.numberOfChildSeats;
    }

    public boolean getFoldingRearSeat() {
        return this.foldingRearSeat;
    }

    public boolean getTowBar() {
        return this.towBar;
    }

    // Setters
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setNumberOfSeats(short numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setNumberOfChildSeats(short numberOfChildSeats) {
        this.numberOfChildSeats = numberOfChildSeats;
    }

    public void setFoldingRearSeat(boolean foldingRearSeat) {
        this.foldingRearSeat = foldingRearSeat;
    }

    public void setTowBar(boolean towBar) {
        this.towBar = towBar;
    }

    public Set<Rental> getRentals() {
        if (rentals == null) {
            rentals = new HashSet<>();
        }
        return rentals;
    }

    public void addRental(Rental rental) {
        this.getRentals().add(rental);
    }

    public void removeRental(Rental rental) {
        this.rentals.remove(rental);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Car car = (Car) obj;
        return Objects.equals(id, car.id);
    }

}
