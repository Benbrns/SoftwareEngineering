package be.ucll.se.groep02backend.rental.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.car.service.CarService;
import be.ucll.se.groep02backend.car.service.CarServiceException;
import be.ucll.se.groep02backend.config.ApplicationConfig;
import be.ucll.se.groep02backend.rental.model.domain.PublicRental;
import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.rental.model.domain.SearchRentals;
import be.ucll.se.groep02backend.rental.service.RentalService;
import be.ucll.se.groep02backend.rental.service.RentalServiceException;
import be.ucll.se.groep02backend.user.service.UserServiceException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Rental")
@RestController
@RequestMapping("/rental")
public class RentalRestController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private CarService carService;

    @GetMapping
    public List<PublicRental> getRentals() {
        return rentalService.getAllPublicRentals();
    }

    @GetMapping("/get/car/")
    public Car getCarbyRentalId(@RequestParam("rentalId") Long rentalId) {
        Rental rental = rentalService.findRental(rentalId);
        return carService.findCarByRentalId(rental.id);
    }
    
    @PostMapping("/add/")
    public Rental addRental(@RequestBody @Valid Rental rental, @RequestParam(value = "carId", required = false) Long carId) throws RentalServiceException, UserServiceException ,  CarServiceException {
        if (carId == null) {
        throw new RentalServiceException("rental", "carId must be provided in the URL");
        }
        return rentalService.addRental(rental, carId,  ApplicationConfig.getAuthenticatedUser());
    }

    @PostMapping("/search/")
    public List<Rental> searchForRentals(@RequestBody SearchRentals search) throws RentalServiceException, CarServiceException {
        return rentalService.searchRentals(search);
    }

    @DeleteMapping("/delete/")
    public Rental deleteRental(@RequestParam("rentalId") Long rentalId) throws RentalServiceException, UserServiceException{
        return rentalService.deleteRental(rentalId, ApplicationConfig.getAuthenticatedUser());
    }

}
