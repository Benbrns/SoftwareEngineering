package be.ucll.se.groep02backend.rent.model.domain;

import jakarta.validation.constraints.NotNull;

public class RentCheckOutBody {

    @NotNull(message = "Distance is required")
    private double distance;

    @NotNull(message = "Fuel level is required")
    private int fuelLevel;

    public RentCheckOutBody() {
    }

    public RentCheckOutBody(double distance, int fuelLevel) {
        this.distance = distance;
        this.fuelLevel = fuelLevel;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(int fuelLevel) {
        this.fuelLevel = fuelLevel;
    }
    
}
