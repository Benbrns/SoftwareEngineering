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

import be.ucll.se.groep02backend.rent.model.domain.Rent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RentTest {

    private LocalDate validStartDate = LocalDate.parse("2024-06-20");
    private LocalDate validEndDate = LocalDate.parse("2024-07-23");

    private Rent rentOne;

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
        rentOne = new Rent(validStartDate, validEndDate);
    }

    @Test
    public void givenValidStartDateEndDate_whenCreatingRent_thenRentIsCreatedWithThatStartDateEndDate() {
        assertNotNull(rentOne);
        assertEquals(validStartDate, rentOne.getStartDate());
        assertEquals(validEndDate, rentOne.getEndDate());
        Set<ConstraintViolation<Rent>> violations = validator.validate(rentOne);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenInvalidStartDate_whenRentIsCreated_thenStartDateViolationMessageIsThrown() {
        // when
        LocalDate inValidStartDate = null;
        Rent rent = new Rent(inValidStartDate, validEndDate);

        // then
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rent> violation = violations.iterator().next();
        assertEquals("Start date is required", violation.getMessage());
        assertEquals("startDate", violation.getPropertyPath().toString());
        assertEquals(inValidStartDate, violation.getInvalidValue());

    }

    @Test
    public void givenStartDateNotInFuture_whenRentIsCreated_thenStartDateViolationMessageIsThrown() {
        // when
        LocalDate inValidStartDate = LocalDate.parse("2023-05-23");
        Rent rent = new Rent(inValidStartDate, validEndDate);

        // then
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rent> violation = violations.iterator().next();
        assertEquals("Start date is invalid, it has to be in the future", violation.getMessage());
        assertEquals("startDate", violation.getPropertyPath().toString());
        assertEquals(inValidStartDate, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidEndDate_whenRentIsCreated_thenEndDateViolationMessageIsThrown() {
        // when
        LocalDate inValidEndDate = null;
        Rent rent = new Rent(validStartDate, inValidEndDate);

        // then
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rent> violation = violations.iterator().next();
        assertEquals("End date is required", violation.getMessage());
        assertEquals("endDate", violation.getPropertyPath().toString());
        assertEquals(inValidEndDate, violation.getInvalidValue());

    }

    @Test
    public void givenEndDateNotInFuture_whenRentIsCreated_thenEndDateViolationMessageIsThrown() {
        // when
        LocalDate inValidEndDate = LocalDate.parse("2023-05-23");
        Rent rent = new Rent(validStartDate, inValidEndDate);

        // then
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertEquals(1, violations.size());
        ConstraintViolation<Rent> violation = violations.iterator().next();
        assertEquals("End date is invalid, it has to be in the future", violation.getMessage());
        assertEquals("endDate", violation.getPropertyPath().toString());
        assertEquals(inValidEndDate, violation.getInvalidValue());
    }
    
}
