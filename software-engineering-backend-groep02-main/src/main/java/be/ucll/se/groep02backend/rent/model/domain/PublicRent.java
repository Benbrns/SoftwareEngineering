package be.ucll.se.groep02backend.rent.model.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import be.ucll.se.groep02backend.rental.model.domain.Rental;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicRent {

    public long id;

    private String renterEmail;

    @JsonIgnoreProperties("rents")
    private Rental rental;

    private LocalDate startDate;

    private LocalDate endDate;

    private RentStatus status;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean checkInStatus;

    private String ownerEmail;

    public PublicRent(Rent rent, String ownerEmail) {
        this.id = rent.id;
        this.renterEmail = rent.getUser().getEmail();
        this.rental = rent.getRental();
        this.startDate = rent.getStartDate();
        this.endDate = rent.getEndDate();
        this.status = rent.getStatus();
        this.checkInDate = rent.getCheckInDate();
        this.checkOutDate = rent.getCheckOutDate();
        this.checkInStatus = rent.getCheckInStatus();
        this.ownerEmail = ownerEmail;
    }


}
