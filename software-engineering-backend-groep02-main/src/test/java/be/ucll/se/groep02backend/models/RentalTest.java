package be.ucll.se.groep02backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.se.groep02backend.rental.model.domain.Rental;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RentalTest {

    private LocalDate  validStartDate = LocalDate.parse("2024-06-20");
    private LocalDate  validEndDate = LocalDate.parse("2024-07-23");
    private String validStreet = "Steenlaan";
    private int validStreetNumber = 10;
    private int validPostal = 2343;
    private String validCity = "Leuven";
    private Float validBasePrice = (float) 100;
    private Float validPricePerKm = (float) 5;
    private Float validFuelPenaltyPrice = (float) 4;
    private Float validPricePerDay = (float) 20;

    private Rental rentalOne;

    // validator
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @BeforeEach
    public void setUp() {
        rentalOne = new Rental(validStartDate, validEndDate, validStreet, validStreetNumber, validPostal, validCity, validBasePrice, validPricePerKm, validFuelPenaltyPrice, validPricePerDay);
    }

    @Test
    public void givenValidStartDateEndDateStreetStreetNumberPostalCityBasePricePricePerKmFuelPenaltyPricePricePerDay_whenCreatingRental_thenRentalIsCreatedWithThatStartDateEndDateStreetStreetNumberPostalCityBasePricePricePerKmFuelPenaltyPricePricePerDay() {
        assertNotNull(rentalOne);
        assertEquals(validStartDate, rentalOne.getStartDate());
        assertEquals(validEndDate, rentalOne.getEndDate());
        assertEquals(validStreet, rentalOne.getStreet());
        assertEquals(validStreetNumber, rentalOne.getStreetNumber());
        assertEquals(validPostal, rentalOne.getPostal());
        assertEquals(validCity, rentalOne.getCity());
        assertEquals(validBasePrice, rentalOne.getBasePrice());
        assertEquals(validPricePerKm, rentalOne.getPricePerKm());
        assertEquals(validFuelPenaltyPrice, rentalOne.getFuelPenaltyPrice());
        assertEquals(validPricePerDay, rentalOne.getPricePerDay());
        Set<ConstraintViolation<Rental>> violations = validator.validate(rentalOne);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenInvalidStartDate_whenRentalIsCreated_thenStartDateViolationMessageIsThrown() {
        // when
        LocalDate inValidStartDate = null;
        Rental rental = new Rental(inValidStartDate, validEndDate, validStreet, validStreetNumber, validPostal, validCity, validBasePrice, validPricePerKm, validFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("Start date is required", violation.getMessage());
        assertEquals("startDate", violation.getPropertyPath().toString());
        assertEquals(inValidStartDate, violation.getInvalidValue());

    }

    @Test
    public void givenStartDateNotInFuture_whenRentalIsCreated_thenStartDateViolationMessageIsThrown() {
        // when
        LocalDate inValidStartDate = LocalDate.parse("2023-05-23");
        Rental rental = new Rental(inValidStartDate, validEndDate, validStreet, validStreetNumber, validPostal, validCity, validBasePrice, validPricePerKm, validFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("Start date is invalid, it has to be in the future", violation.getMessage());
        assertEquals("startDate", violation.getPropertyPath().toString());
        assertEquals(inValidStartDate, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidEndDate_whenRentalIsCreated_thenEndDateViolationMessageIsThrown() {
        // when
        LocalDate inValidEndDate = null;
        Rental rent = new Rental(validStartDate, inValidEndDate, validStreet, validStreetNumber, validPostal, validCity, validBasePrice, validPricePerKm, validFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("End date is required", violation.getMessage());
        assertEquals("endDate", violation.getPropertyPath().toString());
        assertEquals(inValidEndDate, violation.getInvalidValue());

    }

    @Test
    public void givenEndDateNotInFuture_whenRentalIsCreated_thenEndDateViolationMessageIsThrown() {
        // when
        LocalDate inValidEndDate = LocalDate.parse("2024-05-23");
        Rental rent = new Rental(validStartDate, inValidEndDate, validStreet, validStreetNumber, validPostal, validCity, validBasePrice, validPricePerKm, validFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("End date is invalid, it has to be in the future", violation.getMessage());
        assertEquals("endDate", violation.getPropertyPath().toString());
        assertEquals(inValidEndDate, violation.getInvalidValue());
    }

    @Test
    public void givenInValidStreet_whenRentalIsCreated_thenStreetViolationMessageIsThrown() {
        // when
        String inValidStreet = "";
        Rental rent = new Rental(validStartDate, validEndDate, inValidStreet, validStreetNumber, validPostal, validCity, validBasePrice, validPricePerKm, validFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("Street is required", violation.getMessage());
        assertEquals("street", violation.getPropertyPath().toString());
        assertEquals(inValidStreet, violation.getInvalidValue());
    }

    @Test
    public void givenInValidCity_whenRentalIsCreated_thenCityViolationMessageIsThrown() {
        // when
        String inValidCity = "";
        Rental rent = new Rental(validStartDate, validEndDate, validStreet, validStreetNumber, validPostal, inValidCity, validBasePrice, validPricePerKm, validFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("City is required", violation.getMessage());
        assertEquals("city", violation.getPropertyPath().toString());
        assertEquals(inValidCity, violation.getInvalidValue());
    }

    @Test
    public void givenInValidBasePrice_whenRentalIsCreated_thenBasePriceViolationMessageIsThrown() {
        // when
        Float inValidBasePrice = (float) -1;
        Rental rent = new Rental(validStartDate, validEndDate, validStreet, validStreetNumber, validPostal, validCity, inValidBasePrice, validPricePerKm, validFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("Base price is required", violation.getMessage());
        assertEquals("basePrice", violation.getPropertyPath().toString());
        assertEquals(inValidBasePrice, violation.getInvalidValue());
    }

    @Test
    public void givenInValidPricePerKm_whenRentalIsCreated_thenPricePerKmViolationMessageIsThrown() {
        // when
        Float inValidPricePerKm = (float) -1;
        Rental rent = new Rental(validStartDate, validEndDate, validStreet, validStreetNumber, validPostal, validCity, validBasePrice, inValidPricePerKm, validFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("Price per Kilometer is required", violation.getMessage());
        assertEquals("pricePerKm", violation.getPropertyPath().toString());
        assertEquals(inValidPricePerKm, violation.getInvalidValue());
    }

    @Test
    public void givenInValidFuelPenaltyPrice_whenRentalIsCreated_thenFuelPenaltyPriceViolationMessageIsThrown() {
        // when
        Float inValidFuelPenaltyPrice = (float) -1;
        Rental rent = new Rental(validStartDate, validEndDate, validStreet, validStreetNumber, validPostal, validCity, validBasePrice, validPricePerKm, inValidFuelPenaltyPrice, validPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("Fuel penalty price is required", violation.getMessage());
        assertEquals("fuelPenaltyPrice", violation.getPropertyPath().toString());
        assertEquals(inValidFuelPenaltyPrice, violation.getInvalidValue());
    }

    @Test
    public void givenInValidPricePerDay_whenRentalIsCreated_thenPricePerDayViolationMessageIsThrown() {
        // when
        Float inValidPricePerDay = (float) -1;
        Rental rent = new Rental(validStartDate, validEndDate, validStreet, validStreetNumber, validPostal, validCity, validBasePrice, validPricePerKm, validFuelPenaltyPrice, inValidPricePerDay);

        // then
        Set<ConstraintViolation<Rental>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rental> violation = violations.iterator().next();
        assertEquals("Price per day is required", violation.getMessage());
        assertEquals("pricePerDay", violation.getPropertyPath().toString());
        assertEquals(inValidPricePerDay, violation.getInvalidValue());
    }
    
}
