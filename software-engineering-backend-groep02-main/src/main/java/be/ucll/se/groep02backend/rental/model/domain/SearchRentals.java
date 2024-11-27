package be.ucll.se.groep02backend.rental.model.domain;

import java.time.LocalDate;

public class SearchRentals {

    private String email;
    private LocalDate startDate;
    private LocalDate endDate;
    private String brand;
    private String city;
    private boolean foldingRearSeat = false;
    private short numberOfChildSeats;
    private short numberOfSeats;
    private boolean towBar = false;
    private String type;

    public String getEmail() {
        return this.email;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEnddate() {
        return this.endDate;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getCity() {
        return this.city;
    }

    public boolean getFoldingRearSeat() {
        return this.foldingRearSeat;
    }

    public short getNumberOfChildSeats() {
        return this.numberOfChildSeats;
    }

    public short getNumberOfSeats() {
        return this.numberOfSeats;
    }

    public boolean getTowBar() {
        return this.towBar;
    }

    public String getType() {
        return this.type;
    }

    public void SetEmail(String email) {
        this.email = email;
    }

    public void setStartDate(LocalDate starDate){
        this.startDate = starDate;
    }

    public void setEnddate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFoldingRearSeat(boolean foldingRearSeat) {
        this.foldingRearSeat = foldingRearSeat;
    }

    public void setNumberOfChildSeats(short numberOfChildSeats) {
        this.numberOfChildSeats = numberOfChildSeats;
    }

    public void setNumberOfSeats(short numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setTowBar(boolean towBar) {
        this.towBar = towBar;
    }

    public void setType(String type) {
        this.type = type;
    }
}
