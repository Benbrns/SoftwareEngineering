package be.ucll.se.groep02backend.rental.model.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.rent.model.domain.Rent;
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
import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity
@Table(name = "rental")
public class Rental {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "rental", fetch = FetchType.EAGER)
    private Set<Rent> rents;

    @NotNull(message="Start date is required")
    @FutureOrPresent(message="Start date is invalid, it has to be in the future")
    // Date format change because spring wont allow POST 
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @NotNull(message="End date is required")
    @Future(message = "End date is invalid, it has to be in the future")
    // Date format change because spring wont allow POST
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    @NotBlank(message = "Street is required")
    private String street;

    private int streetNumber;

    private int postal;

    @NotBlank(message = "City is required")
    private String city;

    @Min(value = 0, message = "Base price is required")
    private Float basePrice;

    @Min(value = 0, message = "Price per Kilometer is required")
    private Float pricePerKm;

    @Min(value = 0, message = "Fuel penalty price is required")
    private Float fuelPenaltyPrice;

    @Min(value = 0, message = "Price per day is required")
    private Float pricePerDay;


    public Rental() {
    }

    public Rental(LocalDate startDate, LocalDate endDate, String street, int streetNumber, int postal, String city, Float basePrice, Float pricePerKm, Float fuelPenaltyPrice, Float pricePerDay) {
        setStartDate(startDate);
        setEndDate(endDate);
        setStreet(street);
        setStreetNumber(streetNumber);
        setPostal(postal);
        setCity(city);
        setBasePrice(basePrice);
        setPricePerKm(pricePerKm);
        setFuelPenaltyPrice(fuelPenaltyPrice);
        setPricePerDay(pricePerDay);
    }

    public Rental(LocalDate startDate, LocalDate endDate, String street, int streetNumber, int postal, String city, Float basePrice, Float pricePerKm, Float fuelPenaltyPrice, Float pricePerDay, Car car) {
        setStartDate(startDate);
        setEndDate(endDate);
        setStreet(street);
        setStreetNumber(streetNumber);
        setPostal(postal);
        setCity(city);
        setBasePrice(basePrice);
        setPricePerKm(pricePerKm);
        setFuelPenaltyPrice(fuelPenaltyPrice);
        setPricePerDay(pricePerDay);
        setCar(car);
    }

    // Getters 
    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public String getStreet() {
        return this.street;
    }

    public int getStreetNumber() {
        return this.streetNumber;
    }

    public int getPostal() {
        return this.postal;
    }

    public String getCity() {
        return this.city;
    }

    public Float getBasePrice() {
        return this.basePrice;
    }

    public Float getPricePerKm() {
        return this.pricePerKm;
    }

    public Float getFuelPenaltyPrice() {
        return this.fuelPenaltyPrice;
    }

    public Float getPricePerDay() {
        return this.pricePerDay;
    }


    // Setters
    public void setStartDate(LocalDate starDate){
        this.startDate = starDate;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setPostal(int postal) {
        this.postal = postal;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public void setPricePerKm(float pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public void setFuelPenaltyPrice(float fuelPenaltyPrice) {
        this.fuelPenaltyPrice = fuelPenaltyPrice;
    }

    public void setPricePerDay(float pricePerDay) {
        this.pricePerDay = pricePerDay;
    }



    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
        car.addRental(this);
    }

    public void removeCar() {
        this.car = null;
        car.removeRental(this);
    }

    public Set<Rent> getRents(){
        if(rents == null){
            rents = new HashSet<>();
        }
        return rents;
    }

    public void addRent(Rent rent){
        this.getRents().add(rent);
    }

    public void removeRent(Rent rent){
        this.rents.remove(rent);
    }

    public void removeAllRents(){
        this.rents = new HashSet<>();
    }

}