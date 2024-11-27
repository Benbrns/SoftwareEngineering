package be.ucll.se.groep02backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.se.groep02backend.car.model.Car;
import be.ucll.se.groep02backend.car.service.CarServiceException;
import be.ucll.se.groep02backend.notification.model.Notification;
import be.ucll.se.groep02backend.notification.service.NotificationService;
import be.ucll.se.groep02backend.notification.service.NotificationServiceException;
import be.ucll.se.groep02backend.rent.model.domain.PublicRent;
import be.ucll.se.groep02backend.rent.model.domain.Rent;
import be.ucll.se.groep02backend.rent.model.domain.RentStatus;
import be.ucll.se.groep02backend.rent.repo.RentRepository;
import be.ucll.se.groep02backend.rent.service.RentService;
import be.ucll.se.groep02backend.rent.service.RentServiceException;
import be.ucll.se.groep02backend.rental.model.domain.Rental;
import be.ucll.se.groep02backend.rental.repo.RentalRepository;
import be.ucll.se.groep02backend.rental.service.RentalServiceException;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;

@ExtendWith(MockitoExtension.class)
public class RentServiceTest {
    
    @Mock
    RentRepository rentRepository;

    @Mock
    RentalRepository rentalRepository;

    @InjectMocks
    RentService rentService;

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

    private Car carOne = new Car("Ferrari", "488 GTB", "Super Car", "IT123",  (short) 2,  (short) 0, false, false);
    private Car carTwo = new Car("Audi", "A4", "Brake", "IT123",  (short) 2,  (short) 0, false, false);

    Rental rentalOne = new Rental(LocalDate.now(), LocalDate.now().plusDays(5), "Main Street", 123, 1234, "Cityville", 100.0f, 0.5f, 10.0f, 50.0f);
    Rental rentalTwo = new Rental(LocalDate.now(), LocalDate.now().plusDays(7), "Park Avenue", 456, 5678, "Townsville", 150.0f, 0.7f, 15.0f, 60.0f);

    Rent rentOne = new Rent(1, carTwo, rentalOne, LocalDate.now(), LocalDate.now().plusDays(5), RentStatus.PENDING, userOne);
    Rent rentTwo = new Rent(2, carOne, rentalTwo, LocalDate.now(), LocalDate.now().plusDays(7), RentStatus.PENDING, userOne);
    Rent rentThree = new Rent(3, carTwo, rentalOne, LocalDate.now(), LocalDate.now().plusDays(3), RentStatus.PENDING, userOne);
    Rent rentFour = new Rent(4, carOne, rentalOne, LocalDate.now(), LocalDate.now().plusDays(10), RentStatus.PENDING, userOne);

    Notification notification = new Notification(false, false, rentOne);


    @Test
    public void givenNoRents_whenSearchForAllRents_ThenErrorIsThrown() throws CarServiceException, RentServiceException {
        // when
        RentServiceException ex = Assertions.assertThrows(RentServiceException.class, ()->rentService.getAllRents());

        // then
        assertEquals("rent", ex.getField());  
        assertEquals("There are no rents", ex.getMessage());
    }

    @Test
    public void givenNoRents_whenGetRentsByEmail_ThenErrorIsThrown() {
        // given
        when(rentRepository.findRentsByUserEmail(userOne.getEmail())).thenReturn(new ArrayList<>());

        // when
        RentServiceException ex = Assertions.assertThrows(RentServiceException.class, () -> rentService.getRentsByEmail(userOne.getEmail(), userOne));

        // then
        assertEquals("rent", ex.getField());  
        assertEquals("There are no rents for this email", ex.getMessage());
    }

    @Test
    public void givenAdminAndNoRents_whenGetRentsByEmail_ThenErrorIsThrown() {
        // given
        userOne.addAuthority(Role.ADMIN);  
        when(rentRepository.findRentsByUserEmail(userOne.getEmail())).thenReturn(new ArrayList<>());

        // when
        RentServiceException ex = Assertions.assertThrows(RentServiceException.class, () -> rentService.getRentsByEmail(userOne.getEmail(), userOne));

        // then
        assertEquals("rent", ex.getField());  
        assertEquals("There are no rents for this email", ex.getMessage());
    }

    @Test
    public void givenRentsExistAndUserIsAdmin_whenGetRentsByEmail_ThenRentsAreReturned() throws RentServiceException {
        // given
        List<Rent> rents = new ArrayList<>();
        rents.add(rentOne);
        rents.add(rentTwo);
        userOne.addAuthority(Role.ADMIN); 
        when(rentRepository.findRentsByUserEmail(userOne.getEmail())).thenReturn(rents);

        // when
        List<Rent> result = rentService.getRentsByEmail(userOne.getEmail(), userOne);

        // then
        assertEquals(rents.size(), result.size());
    }

    @Test
    public void givenRentsExistAndUserIsNotAdmin_whenGetRentsByEmail_ThenRentsAreReturned() throws RentServiceException {
        // given
        List<Rent> rents = new ArrayList<>();
        rents.add(rentOne);
        rents.add(rentTwo);
        when(rentRepository.findRentsByUserEmail(userOne.getEmail())).thenReturn(rents);

        // when
        List<Rent> result = rentService.getRentsByEmail(userOne.getEmail(), userOne);

        // then
        assertEquals(rents.size(), result.size());
    }

    @Test
    public void givenUserNotRenterOrAdmin_whenaddRent_ThenErrorIsThrown() {
        // given
        userOne.addAuthority(Role.ACCOUNTANT); 

        // when
        RentServiceException ex = Assertions.assertThrows(RentServiceException.class, () -> rentService.addRent(rentOne, 1L, userOne));

        // then
        assertEquals("role", ex.getField());  
        assertEquals("User must be a renter or admin to rent a car", ex.getMessage());
    }

    @Test
    public void givenRentalNotExist_whenaddRent_ThenErrorIsThrown() {
        // when
        RentServiceException ex = Assertions.assertThrows(RentServiceException.class, () -> rentService.addRent(rentOne, 1L, userOne));

        // then
        assertEquals("role", ex.getField());  
        assertEquals("User must be a renter or admin to rent a car", ex.getMessage());
    }

    @Test
    public void givenRentStartDateBeforeRentalStartDate_whenaddRent_ThenErrorIsThrown() {
        // given
        Rental rental = new Rental(LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "Main Street", 123, 1234, "Cityville", 100.0f, 0.5f, 10.0f, 50.0f);
        lenient().when(rentalRepository.findRentalById(1L)).thenReturn(rental);
        userOne.addAuthority(Role.ADMIN);

        // when
        RentServiceException ex = Assertions.assertThrows(RentServiceException.class, () -> rentService.addRent(rentOne, 1L, userOne));

        // then
        assertEquals("startDate", ex.getField());  
        assertEquals("Rent date is not in rental pêriod", ex.getMessage());
    }

    @Test
    public void givenRentOverlapsWithExistingRents_whenaddRent_ThenErrorIsThrown() {
        // given
        Rental rental = new Rental(LocalDate.now(), LocalDate.now().plusDays(10), "Main Street", 123, 1234, "Cityville", 100.0f, 0.5f, 10.0f, 50.0f);
        rental.addRent(new Rent(2, carOne, rentalOne, LocalDate.now().plusDays(3), LocalDate.now().plusDays(6), RentStatus.PENDING, userOne));
        when(rentalRepository.findRentalById(1L)).thenReturn(rental);
        userOne.addAuthority(Role.ADMIN);

        // when
        RentServiceException ex = Assertions.assertThrows(RentServiceException.class, () -> rentService.addRent(rentOne, 1L, userOne));

        // then
        assertEquals("rent", ex.getField());  
        assertEquals("Cannot rent car", ex.getMessage());
    }

    @Test
    public void givenUserNotRenterOrAdmin_whenCheckoutRent_ThenErrorIsThrown() {
        // given
        Long rentId = 1L;

        // when
        RentServiceException ex = assertThrows(RentServiceException.class, () -> rentService.deleteRent(rentId, userOne));

        // then
        assertEquals("role", ex.getField());  
        assertEquals("User must be a renter or admin to delete a rent", ex.getMessage());
    }

    @Test
    public void givenRentNotFound_whenCheckoutRent_ThenErrorIsThrown() {
        // given
        Long rentId = 1L;
        when(rentRepository.findRentByIdAndUserEmail(rentId, userOne.getEmail())).thenReturn(null);
        userOne.addAuthority(Role.ADMIN);
        // when
        RentServiceException ex = assertThrows(RentServiceException.class, () -> rentService.deleteRent(rentId, userOne));

        // then
        assertEquals("id", ex.getField());  
        assertEquals("Rent with given id does not exist or insufficient rights to delete given rent", ex.getMessage());
    }

    @Test
    public void givenUserNotOwnerOrAdmin_whenUpdateRentStatus_ThenErrorIsThrown() {
        // given
        Long rentId = 1L;
        String status = "confirm";

        // when
        RentServiceException ex = assertThrows(RentServiceException.class, () -> rentService.updateRentStatus(rentId, status, userOne));

        // then
        assertEquals("role", ex.getField());  
        assertEquals("User must be an owner or admin to update a rent", ex.getMessage());
    }

    @Test
    public void givenRentNotFound_whenUpdateRentStatus_ThenErrorIsThrown() {
        // given
        Long rentId = 1L;
        String status = "confirm";
        when(rentRepository.findRentById(rentId)).thenReturn(null);
        userOne.addAuthority(Role.OWNER);
        // when
        RentServiceException ex = assertThrows(RentServiceException.class, () -> rentService.updateRentStatus(rentId, status, userOne));

        // then
        assertEquals("id", ex.getField());  
        assertEquals("Rent with given id does not exist", ex.getMessage());
    }

    @Test
    public void givenValidRentAndStatusConfirm_whenUpdateRentStatus_ThenRentStatusIsConfirmed() throws RentServiceException {
        // given
        Long rentId = 1L;
        String status = "confirm";
        when(rentRepository.findRentById(rentId)).thenReturn(rentOne);
        userOne.addAuthority(Role.OWNER);
        // when
        Rent result = rentService.updateRentStatus(rentId, status, userOne);

        // then
        assertEquals(RentStatus.CONFIRMED, result.getStatus());
    }

    @Test
    public void givenValidRentAndStatusReject_whenUpdateRentStatus_ThenRentStatusIsRejected() throws RentServiceException {
        // given
        Long rentId = 1L;
        String status = "reject";
        when(rentRepository.findRentById(rentId)).thenReturn(rentOne);
        userOne.addAuthority(Role.OWNER);

        // when
        Rent result = rentService.updateRentStatus(rentId, status, userOne);

        // then
        assertEquals(RentStatus.REJECTED, result.getStatus());
    }

    @Test
    public void givenInvalidStatus_whenUpdateRentStatus_ThenErrorIsThrown() {
        // given
        Long rentId = 1L;
        String status = "invalid";
        userOne.addAuthority(Role.OWNER);
        when(rentRepository.findRentById(rentId)).thenReturn(rentOne);

        // when
        RentServiceException ex = assertThrows(RentServiceException.class, () -> rentService.updateRentStatus(rentId, status, userOne));

        // then
        assertEquals("status", ex.getField());  
        assertEquals("Status is not valid", ex.getMessage());
    }
}
