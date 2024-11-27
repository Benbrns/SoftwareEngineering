package be.ucll.se.groep02backend.car.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.car.repo.CarRepository;
import be.ucll.se.groep02backend.notification.service.NotificationService;
import be.ucll.se.groep02backend.notification.service.NotificationServiceException;
import be.ucll.se.groep02backend.rent.model.domain.Rent;
import be.ucll.se.groep02backend.rent.repo.RentRepository;
import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.rental.repo.RentalRepository;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private NotificationService notificationService;

    public CarService() {
    }

    public List<Car> getAllCars(User user) throws CarServiceException {
        if (user.getRoles().contains(Role.ADMIN)) {
            return carRepository.findAll();
        } else if (user.getRoles().contains(Role.OWNER)) {
            return carRepository.findAllCarsByUser(user);
        } else {
            throw new CarServiceException("role", "User is not an owner.");
        }
    }

    public Car addCar(Car car, User user) throws CarServiceException {
        if (user.getRoles().contains(Role.OWNER) || user.getRoles().contains(Role.ADMIN)) {
            car.setUser(user);
            return carRepository.save(car);
        } else {
            throw new CarServiceException("role", "User is not an owner.");
        }
    }

    public Car findCarByRentalId(Long rentalId) {
        return carRepository.findCarByRentalsId(rentalId);
    }

    public boolean deleteCar(Long id, User user) throws CarServiceException, NotificationServiceException {
        if (!user.getRoles().contains(Role.OWNER) && !user.getRoles().contains(Role.ADMIN)) {
            throw new CarServiceException("role", "User is not an owner.");
        } else {
            if (user.getRoles().contains(Role.ADMIN)) {
                Car car = carRepository.findCarById(id);
                if (car == null) {
                    throw new CarServiceException("car", "Car with given id: "+ id +" does not exist.");
                }
                Set<Rental> rentals = car.getRentals();
                for (Rental rental : rentals) {
                    Set<Rent> rents = rental.getRents();
                    for (Rent rent : rents) {
                        notificationService.deleteNotification(rent);
                        rentRepository.delete(rent);
                    }
                    rentalRepository.delete(rental);
                }
                carRepository.delete(car);
                return true;
            } else {
                Car cars = carRepository.findCarById(id);
                List<Car> userCars = carRepository.findAllCarsByUser(user);

                if (cars != null && !userCars.isEmpty()) {
                    for (Car car : userCars) {
                        if (Objects.equals(car.id, cars.id)) {
                            Set<Rental> rentals = cars.getRentals();
                            for (Rental rental : rentals) {
                                Set<Rent> rents = rental.getRents();
                                for (Rent rent : rents) {
                                    notificationService.deleteNotification(rent);
                                    rentRepository.delete(rent);
                                }
                                rentalRepository.delete(rental);
                            }
                            carRepository.delete(cars);
                            return true;
                        }
                    }
                }
                throw new CarServiceException("user", "Car is not owned by user or does not exist.");
            }
        }
    }

    // public List<Car> getAllCarsByUser(User user) throws CarServiceException {
    // if (user.getRoles().contains(Role.OWNER) ||
    // user.getRoles().contains(Role.ADMIN)) {
    // return carRepository.findAllCarsByUser(user);
    // } else {
    // throw new CarServiceException("role", "User is not an owner.");
    // }

    // }
}
