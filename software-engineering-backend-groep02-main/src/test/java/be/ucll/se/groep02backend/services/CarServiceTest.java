package be.ucll.se.groep02backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.car.repo.CarRepository;
import be.ucll.se.groep02backend.car.service.CarService;
import be.ucll.se.groep02backend.car.service.CarServiceException;
import be.ucll.se.groep02backend.notification.service.NotificationServiceException;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;
import be.ucll.se.groep02backend.user.service.UserServiceException;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarService carService;

    private Car carOne = new Car("Ferrari", "488 GTB", "Super Car", "IT123", (short) 2, (short) 0, false, false);
    private Car carTwo = new Car("Audi", "A4", "Brake", "IT123", (short) 2, (short) 0, false, false);
    private Car carThree = new Car("BMW", "X5", "Brake", "IT123", (short) 2, (short) 0, false, false);
    private Car carFour = new Car("Lamborghini", "Aventador", "Super Car", "IT123", (short) 2, (short) 0, false, false);

    // private User userOne = new User(1, "John", "Doe", "johndoe@example.com", "password", "1234567890", LocalDate.of(1990, 5, 15), "00.00.00-000.00", "1234567890", null, null, null);

    User userOne = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password123")
                .phoneNumber("1234567890")
                .birthDate(LocalDate.of(1990, 5, 15))
                .nationalRegisterNumber("00.00.00-000.00")
                .licenseNumber("1234567890")
                .build();
    @Test
    public void givenNoCars_whenValidCarAddedWithAdminRole_ThenCarIsAddedAndCarIsReturned() throws CarServiceException {
        // given
        when(carRepository.save(carOne)).thenReturn(carOne);

        userOne.addAuthority(Role.ADMIN);
        // when
        Car added = carService.addCar(carOne, userOne);

        // then
        assertEquals(carOne.getBrand(), added.getBrand());
        assertEquals(carOne.getModel(), added.getModel());
        assertEquals(carOne.getType(), added.getType());
        assertEquals(carOne.getLicensePlate(), added.getLicensePlate());
        assertEquals(carOne.getNumberOfSeats(), added.getNumberOfSeats());
        assertEquals(carOne.getNumberOfChildSeats(), added.getNumberOfChildSeats());
        assertEquals(carOne.getFoldingRearSeat(), added.getFoldingRearSeat());
        assertEquals(carOne.getTowBar(), added.getTowBar());
    }   

    @Test
    public void givenNoCars_whenValidCarAddedWithOwnerRole_ThenCarIsAddedAndCarIsReturned() throws CarServiceException {
        // given
        when(carRepository.save(carOne)).thenReturn(carOne);

        userOne.addAuthority(Role.OWNER);
        // when
        Car added = carService.addCar(carOne, userOne);

        // then
        assertEquals(carOne.getBrand(), added.getBrand());
        assertEquals(carOne.getModel(), added.getModel());
        assertEquals(carOne.getType(), added.getType());
        assertEquals(carOne.getLicensePlate(), added.getLicensePlate());
        assertEquals(carOne.getNumberOfSeats(), added.getNumberOfSeats());
        assertEquals(carOne.getNumberOfChildSeats(), added.getNumberOfChildSeats());
        assertEquals(carOne.getFoldingRearSeat(), added.getFoldingRearSeat());
        assertEquals(carOne.getTowBar(), added.getTowBar());
    }

    @Test
    public void givenNoCars_whenValidCarWithAccountantRole_ThenErrorIsThrown() throws CarServiceException {
        // given
        userOne.addAuthority(Role.ACCOUNTANT);

        // when
        CarServiceException ex = Assertions.assertThrows(CarServiceException.class, ()->carService.addCar(carOne, userOne));

        // then
        assertEquals("role", ex.getField());  
        assertEquals("User is not an owner.", ex.getMessage());
    }

    @Test
    public void givenNoCars_whenValidCarWithRenterRole_ThenErrorIsThrown() throws CarServiceException {
        // given
        userOne.addAuthority(Role.ACCOUNTANT);

        // when
        CarServiceException ex = Assertions.assertThrows(CarServiceException.class, ()->carService.addCar(carOne, userOne));

        // then
        assertEquals("role", ex.getField());  
        assertEquals("User is not an owner.", ex.getMessage());
    }

    @Test
    public void given4Cars_whenSearchForAllCarsWithAdminRole_ThenAllCarsAreReturned() throws CarServiceException {
        // given
        List<Car> carList = new ArrayList<>();
        carList.add(carOne);
        carList.add(carTwo);
        carList.add(carThree);
        carList.add(carFour);
        when(carRepository.findAll()).thenReturn(carList);
        userOne.addAuthority(Role.ADMIN);
        // when
        List<Car> result = carService.getAllCars(userOne);

        // then
        assertEquals(result.size(), 4);
    }

    @Test
    public void given2Cars_whenSearchForAllCarsWithOwnernRole_ThenAllCarsAreReturned() throws CarServiceException {
        // given
        List<Car> carList = new ArrayList<>();
        carList.add(carOne);
        carList.add(carTwo);
        carList.add(carThree);
        carList.add(carFour);
        when(carRepository.findAllCarsByUser(userOne)).thenReturn(carList);
        userOne.addAuthority(Role.OWNER);
        // when
        List<Car> result = carService.getAllCars(userOne);

        // then
        assertEquals(result.size(), 4);
    }

    @Test
    public void givenNoCars_whenSearchForAllCarsWithInvalidRoles_ThenErrorIsThrown() throws CarServiceException {
        // given
        userOne.addAuthority(Role.ACCOUNTANT);

        // when
        CarServiceException ex = Assertions.assertThrows(CarServiceException.class, ()->carService.getAllCars(userOne));

        // then
        assertEquals("role", ex.getField());  
        assertEquals("User is not an owner.", ex.getMessage());
    }

    @Test
    public void given1Car_whenCarIsDeleted_ThenCarIsDeletedAndTrueIsReturned() throws CarServiceException, NotificationServiceException {
        // given
        when(carRepository.findCarById(1L)).thenReturn(carOne);
        userOne.addAuthority(Role.ADMIN);

        // when
        Boolean result = carService.deleteCar(1L, userOne);

        // then
        assertEquals(true, result);
    }

    @Test
    public void given1Car_whenCarIsDeletedWithInvalidRole_ThenErrorIsThrown() throws CarServiceException, NotificationServiceException {
        // given
        userOne.addAuthority(Role.ACCOUNTANT);

        // when
        CarServiceException ex = Assertions.assertThrows(CarServiceException.class, ()->carService.deleteCar(1L, userOne));

        // then
        assertEquals("role", ex.getField());  
        assertEquals("User is not an owner.", ex.getMessage());
    }

    @Test
    public void givenNoCars_whenCarIsDeletedWithInvalidIdAndOwnerRole_ThenErrorIsThrown() throws CarServiceException, NotificationServiceException {
        // given
        userOne.addAuthority(Role.OWNER);

        // when
        CarServiceException ex = Assertions.assertThrows(CarServiceException.class, ()->carService.deleteCar(1L, userOne));

        // then
        assertEquals("user", ex.getField());  
        assertEquals("Car is not owned by user or does not exist.", ex.getMessage());
    }

    @Test
    public void givenNoCars_whenCarIsDeletedWithInvalidIdAndAdminRole_ThenErrorIsThrown() throws CarServiceException, NotificationServiceException {
        // given
        userOne.addAuthority(Role.ADMIN);

        // when
        CarServiceException ex = Assertions.assertThrows(CarServiceException.class, ()->carService.deleteCar(1L, userOne));

        // then
        assertEquals("car", ex.getField());  
        assertEquals("Car with given id: 1 does not exist.", ex.getMessage());
    }
}
