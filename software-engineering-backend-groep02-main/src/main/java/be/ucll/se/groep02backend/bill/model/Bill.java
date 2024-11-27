package be.ucll.se.groep02backend.bill.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "bill")
public class Bill {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    private String renterEmail;
    private String ownerEmail;

    private String carBrand;
    private String carModel;
    private String carLicensePlate;
    

    private double distance;

    private Long days;

    private double fuelLevel;
    
    private double total;

    private boolean paid;

    public Bill() {
    }

    public Bill(String renterEmail, String ownerEmail, String carBrand, String carModel, String carLicensePlate, double distance, Long days, double fuelLevel, double total) {
        this.renterEmail = renterEmail;
        this.ownerEmail = ownerEmail;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carLicensePlate = carLicensePlate;
        this.distance = distance;
        this.days = days;
        this.fuelLevel = fuelLevel;
        this.total = total;
        this.paid = false;
    }

    public long getId() {
        return id;
    }

    public String getRenterEmail() {
        return renterEmail;
    }

    public void setRenterEmail(String renterEmail) {
        this.renterEmail = renterEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarLicensePlate() {
        return carLicensePlate;
    }

    public void setCarLicensePlate(String carLicensePlate) {
        this.carLicensePlate = carLicensePlate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        this.days = days;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
    
}
