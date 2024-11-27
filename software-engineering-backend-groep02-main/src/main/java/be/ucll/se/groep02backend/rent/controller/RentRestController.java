package be.ucll.se.groep02backend.rent.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep02backend.config.ApplicationConfig;
import be.ucll.se.groep02backend.rent.model.domain.PublicRent;
import be.ucll.se.groep02backend.rent.model.domain.Rent;
import be.ucll.se.groep02backend.rent.model.domain.RentCheckOutBody;
import be.ucll.se.groep02backend.rent.model.domain.RentStatus;
import be.ucll.se.groep02backend.rent.service.RentService;
import be.ucll.se.groep02backend.rent.service.RentServiceException;
import be.ucll.se.groep02backend.rental.service.RentalServiceException;
import be.ucll.se.groep02backend.user.service.UserServiceException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import be.ucll.se.groep02backend.notification.service.NotificationServiceException;




@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Rent")
@RestController
@RequestMapping("/rent")
public class RentRestController {
    @Autowired
    private RentService rentService;

    // Returns a dict with car as string and a rent object
    @GetMapping
    public List<PublicRent> getAllRents() throws RentServiceException {
        return rentService.getAllRents();
    }
    
    @GetMapping("/email/")
    public List<Rent> getRentsByEmail(@RequestParam String email) throws RentServiceException, UserServiceException{
        return rentService.getRentsByEmail(email, ApplicationConfig.getAuthenticatedUser());
    }

    @PutMapping("/checkIn/")
    public Rent checkInRent(@RequestParam("rentId") Long rentId) throws RentServiceException, UserServiceException {
        return rentService.checkIn(rentId, ApplicationConfig.getAuthenticatedUser());
    }

    @PutMapping("/checkOut/")
    public Rent checkOutRent(@RequestParam("rentId") Long rentId, @RequestBody @Valid RentCheckOutBody body) throws RentServiceException {
        return rentService.checkOut(rentId, null, body.getDistance(), body.getFuelLevel());
    }
    
    @PostMapping("/add/{rentalId}")
    public Rent addRent(@RequestBody @Valid Rent rent, @PathVariable("rentalId") Long rentalId)
            throws RentServiceException, RentalServiceException, UserServiceException, MessagingException, IOException {
        return rentService.addRent(rent, rentalId, ApplicationConfig.getAuthenticatedUser());
    }

    @DeleteMapping("/delete/")
    public Rent deleteRent(@RequestParam("rentId") Long rentId) throws RentServiceException, UserServiceException, NotificationServiceException {
        return rentService.deleteRent(rentId, ApplicationConfig.getAuthenticatedUser());
    }

    @PutMapping("/status/{status}/{id}")
    public String updateRentStatus(@PathVariable Long id, @PathVariable @Valid RentStatus status) throws RentServiceException, UserServiceException, MessagingException, IOException{
        return rentService.updateRentStatus(status, id, ApplicationConfig.getAuthenticatedUser());
    }
    
}
