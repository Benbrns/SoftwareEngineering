package be.ucll.se.groep02backend.rent.model.domain;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.user.model.User;
// JPA imports
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
// Validation imports
import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "rent")
public class Rent {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date is invalid, it has to be in the future")
    // Date format change because spring wont allow POST
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date is invalid, it has to be in the future")
    // Date format change because spring wont allow POST
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private boolean checkInStatus;

    private RentStatus status;

    public Rent() {
    }

    public Rent(LocalDate startDate, LocalDate endDate) {
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public Rent(long id, Car car, Rental rental, LocalDate startDate, LocalDate endDate, RentStatus status, User user) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
        this.rental = rental;
        this.checkInStatus = false;
    }

    // Getters
    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public RentStatus getStatus() {
        return this.status;
    }

    public LocalDate getCheckInDate() {
        return this.checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return this.checkOutDate;
    }

    public boolean getCheckInStatus() {
        return this.checkInStatus;
    }

    // Setters
    public void setStartDate(LocalDate starDate) {
        this.startDate = starDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(RentStatus status) {
        this.status = status;
    }

    public void setCheckInDate(LocalDate date) {
        this.checkInDate = date;
    }

    public void setCheckOutDate(LocalDate date) {
        this.checkOutDate = date;
    }

    public void setCheckInStatus(boolean status) {
        this.checkInStatus = status;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
        rental.addRent(this);
    }

    public void removeRental() {
        this.rental = null;
        rental.removeRent(this);
    }

	public void setUser(User user) {
        this.user = user;
	}

    public User getUser() {
        return user;
    }

    public Car getCar() {
        return rental.getCar();
    }

}
