package be.ucll.se.groep02backend.rental.model.domain;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import be.ucll.se.groep02backend.car.model.Car;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicRental {
    

    private long id;

    @JsonIgnoreProperties("rentals")
    private Car car;
    
    private Long hasRents;

    private LocalDate startDate;
    
    private LocalDate endDate;

    private String ownerEmail;
    
    private String street;

    private int streetNumber;
    

    private int postal;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Base price is required")
    private float basePrice;

    @NotNull(message = "Price per Kilometer is required")
    private float pricePerKm;

    @NotNull(message = "Fuel penalty price is required")
    private float fuelPenaltyPrice;

    @NotNull(message = "Price per day is required")
    private float pricePerDay;


    public PublicRental(Rental rental, String ownerEmail) {
        this.id = rental.id;
        this.car = rental.getCar();
        this.hasRents = rental.getRents().stream().count();
        this.startDate = rental.getStartDate();
        this.endDate = rental.getEndDate();
        this.ownerEmail = ownerEmail;
        this.street = rental.getStreet();
        this.streetNumber = rental.getStreetNumber();
        this.postal = rental.getPostal();
        this.city = rental.getCity();
        this.basePrice = rental.getBasePrice();
        this.pricePerKm = rental.getPricePerKm();
        this.fuelPenaltyPrice = rental.getFuelPenaltyPrice();
        this.pricePerDay = rental.getPricePerDay();
    }

   
}