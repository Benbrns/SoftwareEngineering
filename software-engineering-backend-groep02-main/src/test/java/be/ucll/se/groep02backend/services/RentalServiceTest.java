package be.ucll.se.groep02backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.car.repo.CarRepository;
import be.ucll.se.groep02backend.car.service.CarServiceException;
import be.ucll.se.groep02backend.notification.service.NotificationService;
import be.ucll.se.groep02backend.rent.model.domain.Rent;
import be.ucll.se.groep02backend.rent.model.domain.RentStatus;
import be.ucll.se.groep02backend.rent.repo.RentRepository;
import be.ucll.se.groep02backend.rent.service.RentService;
import be.ucll.se.groep02backend.rent.service.RentServiceException;
import be.ucll.se.groep02backend.rental.model.domain.PublicRental;
import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.rental.model.domain.SearchRentals;
import be.ucll.se.groep02backend.rental.repo.RentalRepository;
import be.ucll.se.groep02backend.rental.service.RentalService;
import be.ucll.se.groep02backend.rental.service.RentalServiceException;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    @Mock
    RentRepository rentRepository;

    @Mock
    RentalRepository rentalRepository;

    @Mock
    CarRepository carRepository;

    @InjectMocks
    RentalService rentalService;

    @InjectMocks
    NotificationService notificationService;

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

    private Car carOne = new Car("Ferrari", "488 GTB", "Super Car", "IT123", (short) 2, (short) 0, false, false, userOne);
    private Car carTwo = new Car("Audi", "A4", "Brake", "IT123",  (short) 2,  (short) 0, false, false, userOne);

    Rental rentalOne = new Rental(LocalDate.now(), LocalDate.now().plusDays(5), "Main Street", 123, 1234, "Cityville", 100.0f, 0.5f, 10.0f, 50.0f, carOne);
    Rental rentalTwo = new Rental(LocalDate.now(), LocalDate.now().plusDays(7), "Park Avenue", 456, 5678, "Townsville", 150.0f, 0.7f, 15.0f, 60.0f, carTwo);

    Rent rentOne = new Rent(1, carTwo, rentalOne, LocalDate.now(), LocalDate.now().plusDays(5), RentStatus.PENDING, userOne);
    Rent rentTwo = new Rent(2, carOne, rentalTwo, LocalDate.now(), LocalDate.now().plusDays(7), RentStatus.PENDING, userOne);
    Rent rentThree = new Rent(3, carTwo, rentalOne, LocalDate.now(), LocalDate.now().plusDays(3), RentStatus.PENDING, userOne);
    Rent rentFour = new Rent(4, carOne, rentalOne, LocalDate.now(), LocalDate.now().plusDays(10), RentStatus.PENDING, userOne);

    @Test
    public void given2Rentals_whenSearchForAllRentals_thenAllRentalsAreReturned() {
        // given
        List<Rental> rentals = new ArrayList<>();
        rentals.add(rentalOne);
        rentals.add(rentalTwo);
        when(rentalRepository.findAll()).thenReturn(rentals);
        when(rentalRepository.findUserByRentalsCar(rentalOne)).thenReturn(userOne);
        when(rentalRepository.findUserByRentalsCar(rentalTwo)).thenReturn(userOne);

        // when
        List<PublicRental> publicRentals = rentalService.getAllPublicRentals();

        // then
        assertEquals(2, publicRentals.size());
        assertEquals(userOne.getEmail(), publicRentals.get(0).getOwnerEmail());
        assertEquals(userOne.getEmail(), publicRentals.get(1).getOwnerEmail());
    }

    @Test
    public void given1Rental_whenSearchForThatRental_thenRentalIsReturned() {
        // given
        Long rentalId = 1L;
        Rental rental = new Rental(LocalDate.now(), LocalDate.now().plusDays(5), "Main Street", 123, 1234, "Cityville", 100.0f, 0.5f, 10.0f, 50.0f);
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        // when
        Rental foundRental = rentalService.findRental(rentalId);

        // then
        assertEquals(rental, foundRental);
    }

    @Test
    public void givenValidRentalDetails_whenAddingRental_thenRentalIsAdded() throws RentalServiceException, CarServiceException {
        // given
        Long carId = 1L;  
        userOne.addAuthority(Role.OWNER);      
        when(carRepository.findCarById(carId)).thenReturn(carOne);
        when(carRepository.findAllCarsByUser(userOne)).thenReturn(List.of(carOne));

        // when
        Rental addedRental = rentalService.addRental(rentalOne, carId, userOne);

        // then
        assertEquals(rentalOne, addedRental);
    }

    @Test
    public void givenInvalidStartDate_whenAddingRental_thenRentalServiceExceptionIsThrown() {
        // given
        Long carId = 1L;
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(1);
        Rental rental = new Rental(startDate, endDate, "Main Street", 123, 1234, "Cityville", 100.0f, 0.5f, 10.0f, 50.0f);
        userOne.addAuthority(Role.OWNER);
        lenient().when(carRepository.findCarById(carId)).thenReturn(carOne);

        // when
        RentalServiceException ex = Assertions.assertThrows(RentalServiceException.class, () -> rentalService.addRental(rental, carId, userOne));

        // then
        assertEquals("rental", ex.getField());  
        assertEquals("Start date must be before the end date", ex.getMessage());
    }

    @Test
    public void givenUserNotOwnerOrAdmin_whenAddingRental_thenRentalServiceExceptionIsThrown() {
        // given
        Long carId = 1L;
        lenient().when(carRepository.findCarById(carId)).thenReturn(carOne);

        // when
        RentalServiceException ex = Assertions.assertThrows(RentalServiceException.class, () -> rentalService.addRental(rentalOne, carId, userOne));

        // then
        assertEquals("role", ex.getField());
        assertEquals("User is not an owner.", ex.getMessage());
    }

    @Test
    public void givenUserIsOwnerButCarNotBelongsToUser_whenAddingRental_thenCarServiceExceptionIsThrown() {
        // given
        Long carId = 1L;
        userOne.addAuthority(Role.OWNER);
        when(carRepository.findCarById(carId)).thenReturn(carOne);
        when(carRepository.findAllCarsByUser(userOne)).thenReturn(Collections.emptyList()); 

        // when
        CarServiceException ex = Assertions.assertThrows(CarServiceException.class, () -> rentalService.addRental(rentalOne, carId, userOne));

        // then
        assertEquals("car", ex.getField());
        assertEquals("Car with id: " + carId + " does not belong to user", ex.getMessage());
    }

    @Test
    public void givenNoFilters_whenSearchingRentals_thenReturnsAllRentals() throws RentalServiceException, CarServiceException {
        // given
        SearchRentals search = new SearchRentals();
        when(rentalRepository.findRentalsByCriteria(null, null, null)).thenReturn(List.of(rentalOne, rentalTwo));

        // when
        List<Rental> foundRentals = rentalService.searchRentals(search);

        // then
        assertEquals(2, foundRentals.size());
        assertTrue(foundRentals.contains(rentalOne));
        assertTrue(foundRentals.contains(rentalTwo));
    }

    @Test
    public void givenEmailFilter_whenSearchingRentals_thenReturnsRentalsForUser() throws RentalServiceException, CarServiceException {
        // given
        String userEmail = "johndoe@example.com";
        SearchRentals search = new SearchRentals();
        search.SetEmail(userEmail);
        lenient().when(carRepository.findAllCarsByUserEmail(userEmail)).thenReturn(List.of(carOne));
        when(rentalRepository.findRentalsByCriteria(null, null, null)).thenReturn(List.of(rentalOne));
        carOne.addRental(rentalOne);

        // when
        List<Rental> foundRentals = rentalService.searchRentals(search);

        // then
        assertEquals(1, foundRentals.size());
        assertTrue(foundRentals.contains(rentalOne));
    }

    @Test
    public void givenBrandFilter_whenSearchingRentals_thenReturnsRentalsForBrand() throws RentalServiceException, CarServiceException {
        // given
        String brand = "Ferrari";
        SearchRentals search = new SearchRentals();
        search.setBrand(brand);
        lenient().when(carRepository.existsByBrand(brand)).thenReturn(true);
        lenient().when(carRepository.findAllCarsByBrand(brand)).thenReturn(List.of(carOne));
        when(rentalRepository.findRentalsByCriteria(null, null, null)).thenReturn(List.of(rentalOne, rentalTwo));
        carOne.addRental(rentalOne);

        // when
        List<Rental> foundRentals = rentalService.searchRentals(search);

        // then
        assertEquals(1, foundRentals.size());
        assertTrue(foundRentals.contains(rentalOne));
    }

    @Test
    public void givenOwnerOrAdminDeletesOwnRental_whenDeletingRental_thenRentalIsDeleted() throws RentalServiceException {
        // given
        Long rentalId = 1L;
        userOne.addAuthority(Role.OWNER);
        when(rentalRepository.findRentalById(rentalId)).thenReturn(rentalOne);

        // when
        Rental deletedRental = rentalService.deleteRental(rentalId, userOne);

        // then
        assertEquals(rentalOne, deletedRental);
        verify(rentalRepository).delete(rentalOne);
    }

    @Test
    public void givenNonOwnerDeletesRental_whenDeletingRental_thenThrowsRentalServiceException() {
        // given
        Long rentalId = 1L;
        lenient().when(rentalRepository.findRentalById(rentalId)).thenReturn(rentalOne);

        // when
        RentalServiceException ex = assertThrows(RentalServiceException.class, () -> rentalService.deleteRental(rentalId, userOne));

        // then
        assertEquals("role", ex.getField());
        assertEquals("User is not an owner.", ex.getMessage());
    }

    @Test
    public void givenRentalDoesNotBelongToUser_whenDeletingRental_thenThrowsRentalServiceException() {
        // given
        Long rentalId = 1L;
        userOne.addAuthority(Role.OWNER);
        when(rentalRepository.findRentalById(rentalId)).thenReturn(rentalOne);
        when(carRepository.findAllCarsByUser(userOne)).thenReturn(List.of(carTwo)); // Assuming carTwo does not have rentalOne

        // when
        RentalServiceException ex = assertThrows(RentalServiceException.class, () -> rentalService.deleteRental(rentalId, userOne));

        // then
        assertEquals("rental", ex.getField());
        assertEquals("Rental with given id does not belong to user", ex.getMessage());
    }

    @Test
    public void givenRentalDoesNotExist_whenDeletingRental_thenThrowsRentalServiceException() {
        // given
        Long rentalId = 1L;
        when(rentalRepository.findRentalById(rentalId)).thenReturn(null);
        userOne.addAuthority(Role.OWNER);

        // when
        RentalServiceException ex = assertThrows(RentalServiceException.class, () -> rentalService.deleteRental(rentalId, userOne));

        // then
        assertEquals("id", ex.getField());
        assertEquals("Rental with given id does not exist", ex.getMessage());
    }
}
