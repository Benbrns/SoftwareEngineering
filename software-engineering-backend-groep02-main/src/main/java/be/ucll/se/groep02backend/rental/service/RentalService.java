package be.ucll.se.groep02backend.rental.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.car.repo.CarRepository;
import be.ucll.se.groep02backend.car.service.CarServiceException;
import be.ucll.se.groep02backend.rent.model.domain.Rent;
import be.ucll.se.groep02backend.rent.repo.RentRepository;
import be.ucll.se.groep02backend.rental.model.domain.PublicRental;
import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.rental.model.domain.SearchRentals;
import be.ucll.se.groep02backend.rental.repo.RentalRepository;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private CarRepository carRepository;
    

    public List<PublicRental> getAllPublicRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        List<PublicRental> publicRentals = new ArrayList<>();
        for (Rental rental: rentals) {
            User owner = rentalRepository.findUserByRentalsCar(rental);
            PublicRental publicRental = new PublicRental( rental,  owner.getEmail());
            publicRentals.add(publicRental);
        }
        return publicRentals;
    }

    public Rental findRental(Long id) {
        return rentalRepository.findById(id).orElse(null);
    }

    public Rental addRental(Rental rental, Long carId, User user) throws RentalServiceException, CarServiceException {
        if (!user.getRoles().contains(Role.OWNER) && !user.getRoles().contains(Role.ADMIN)) {
            throw new RentalServiceException("role", "User is not an owner.");
        }
        LocalDate startDate = rental.getStartDate();
        LocalDate endDate = rental.getEndDate();
        if(startDate.isAfter(endDate)){
            throw new RentalServiceException("rental", "Start date must be before the end date");
        } else if (endDate.isBefore(startDate)) {
            throw new RentalServiceException("rental", "End date must be after the start date");
        } 
        Car car = carRepository.findCarById(carId);
        if (user.getRoles().contains(Role.OWNER)) {
            List<Car> userCars = carRepository.findAllCarsByUser(user);
            System.out.println(userCars.size());
            System.out.println(car.getBrand());

            if (!userCars.contains(car)) {
                throw new CarServiceException("car", "Car with id: " + carId + " does not belong to user");
            }
        }
        if (car == null) {
            throw new CarServiceException("car", "Car with id: " + carId + " does not exist");
        }

        rental.setCar(car);
        rentalRepository.save(rental);
        return rental;
    }

    public List<Rental> searchRentals(SearchRentals search) throws RentalServiceException, CarServiceException {
        List<Rental> foundRentals = rentalRepository.findRentalsByCriteria(search.getStartDate(), search.getEnddate(), search.getCity());
        // List<Rental> betweenfinalRentals = new ArrayList<>();
        List<Rental> finalRentals = new ArrayList<>();

        finalRentals = foundRentals.stream().filter(rental -> search.getEmail() == null || rental.getCar().getUser().getEmail().equals(search.getEmail())).filter(rental -> search.getBrand() == null || rental.getCar().getBrand().equals(search.getBrand())).filter(rental -> search.getFoldingRearSeat() == false || rental.getCar().getFoldingRearSeat() == search.getFoldingRearSeat()).filter(rental -> search.getNumberOfChildSeats() == 0 || rental.getCar().getNumberOfChildSeats() == search.getNumberOfChildSeats()).filter(rental -> search.getNumberOfSeats() == 0 || rental.getCar().getNumberOfSeats() == search.getNumberOfSeats()).filter(rental -> search.getTowBar() == false || rental.getCar().getTowBar() == search.getTowBar()).filter(rental -> search.getType() == null || rental.getCar().getType().equals(search.getType())).collect(Collectors.toList());

        if (finalRentals.isEmpty()) {
            throw new RentalServiceException("rental", "There are no rentals for given specifications!");
        }

        return finalRentals;

        // if (search.getEmail() != null) {
        //     List<Car> cars = carRepository.findAllCarsByUserEmail(search.getEmail());

        //     for (Rental rental: foundRentals) {
        //         for (Car car: cars) {
        //             if (car.getRentals().contains(rental)) {
        //                 betweenfinalRentals.add(rental);
        //             }
        //         }
        //     }
        // } else {
        //     betweenfinalRentals.addAll(foundRentals);
        // }
 
        // if (search.getBrand() != null) {
        //     if (!carRepository.existsByBrand(search.getBrand())) {
        //         return finalRentals;
        //     }
        //     List<Car> cars = carRepository.findAllCarsByBrand(search.getBrand());

        //     for (Rental rental: betweenfinalRentals) {
        //         for (Car car: cars) {
        //             if (car.getRentals().contains(rental)) {
        //                 finalRentals.add(rental);
        //             }
        //         }
        //     }
        // } else {
        //     finalRentals.addAll(foundRentals);
        // }

        // if (finalRentals.isEmpty()) {
        //     throw new RentalServiceException("rental", "There are no rentals for given specifications!");
        // } 

        // return finalRentals;
        
    }

    public Rental deleteRental(Long id, User user) throws RentalServiceException{
        if (!user.getRoles().contains(Role.OWNER) && !user.getRoles().contains(Role.ADMIN)) {
            throw new RentalServiceException("role", "User is not an owner.");
        }
        if (user.getRoles().contains(Role.OWNER)) {
            List<Car> userCars = carRepository.findAllCarsByUser(user);
            for (Car car: userCars) {
                if (car.getRentals().contains(rentalRepository.findRentalById(id))) {
                    break;
                }
                throw new RentalServiceException("rental", "Rental with given id does not belong to user");
            }
        }
        Rental rental = rentalRepository.findRentalById(id);
        if(rental == null){
            throw new RentalServiceException("id", "Rental with given id does not exist");
        }
        Set<Rent> rents = rental.getRents();
        rental.removeAllRents();
        rentalRepository.save(rental);
        for(Rent rent: rents){
            rentRepository.delete(rent);
        }
        rentalRepository.delete(rental);
        return rental;
    }
}
