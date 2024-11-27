package be.ucll.se.groep02backend.notification.service;

import java.util.Optional;

import be.ucll.se.groep02backend.rent.model.domain.PublicRent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Data
@AllArgsConstructor
public class PublicNotification {
    private long id;
    private PublicRent rent;
    private Optional<Boolean> ownerViewed;
    private Optional<Boolean> renterViewed;

    

    // public PublicNotification(long id, PublicRent rent, Optional<Boolean> ownerViewed, Optional<Boolean> renterViewed) {
    //     this.id = id;
    //     this.rent = rent;
    //     this.ownerViewed = ownerViewed;
    //     this.renterViewed = renterViewed;
    // }
    

    // Getters and setters
}

