package be.ucll.se.groep02backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.se.groep02backend.car.model.Car;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CarTest {

    // valid values
    private static final String validBrand = "Ferrari";
    private static final String validModel = "488 GTB";
    private static final String validType = "Super car";
    private static final String validLicensePlate = "IT123";
    private static final short validNumberOfSeats = 2;
    private static final short validNumberOfChildSeats = 0;
    private static final boolean validFoldingRearSeat = false;
    private static final boolean validTowBar = false;

    private Car carOne;

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
        carOne = new Car(validBrand, validModel, validType, validLicensePlate, validNumberOfSeats, validNumberOfChildSeats, validFoldingRearSeat, validTowBar);
    }

    // valid test
    @Test
    public void givenValidBrand_whenCreatingCar_thenCarIsCreatedWithBrand() {
        assertNotNull(carOne);
        assertEquals(validBrand, carOne.getBrand());
        Set<ConstraintViolation<Car>> violations = validator.validate(carOne);
        assertTrue(violations.isEmpty());
    }

    // Invalid test
    @Test
    public void givenInvalidBrand_whenCreatingCar_thenBrandViolationMessageIsThrown() {
        // when
        String invalidBrand = "";
        Car car = new Car(invalidBrand, validModel, validType, validLicensePlate, validNumberOfSeats, validNumberOfChildSeats, validFoldingRearSeat, validTowBar);

        // then
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Brand is required", violation.getMessage());
        assertEquals("brand", violation.getPropertyPath().toString());
        assertEquals(invalidBrand, violation.getInvalidValue());
    }

    // valid test
    @Test
    public void givenValidModel_whenCreatingCar_thenCarIsCreatedWithModel() {
        assertNotNull(carOne);
        assertEquals(validModel, carOne.getModel());
        Set<ConstraintViolation<Car>> violations = validator.validate(carOne);
        assertTrue(violations.isEmpty());
    }

    // Invalid test
    @Test
    public void givenInvalidModel_whenCreatingCar_thenModelViolationMessageIsThrown() {}

    // valid test
    @Test
    public void givenValidType_whenCreatingCar_thenCarIsCreatedWithType() {
        assertNotNull(carOne);
        assertEquals(validType, carOne.getType());
        Set<ConstraintViolation<Car>> violations = validator.validate(carOne);
        assertTrue(violations.isEmpty());
    }

    // Invalid test
    @Test
    public void givenInvalidType_whenCreatingCar_thenTypeViolationMessageIsThrown() {
        // when
        String invalidType = "";
        Car car = new Car(validBrand, validModel, invalidType, validLicensePlate, validNumberOfSeats, validNumberOfChildSeats, validFoldingRearSeat, validTowBar);

        // then
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Type is required", violation.getMessage());
        assertEquals("type", violation.getPropertyPath().toString());
        assertEquals(invalidType, violation.getInvalidValue());
    }

    // valid test
    @Test
    public void givenValidLicensePlate_whenCreatingCar_thenCarIsCreatedWithLicensePlate() {
        assertNotNull(carOne);
        assertEquals(validLicensePlate, carOne.getLicensePlate());
        Set<ConstraintViolation<Car>> violations = validator.validate(carOne);
        assertTrue(violations.isEmpty());
    }

    // Invalid test
    @Test
    public void givenInvalidLicensePlate_whenCreatingCar_thenLicensePlateViolationMessageIsThrown() {
        // when
        String invalidLicensePlate = "";
        Car car = new Car(validBrand, validModel, validType, invalidLicensePlate, validNumberOfSeats, validNumberOfChildSeats, validFoldingRearSeat, validTowBar);

        // then
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("License plate is required", violation.getMessage());
        assertEquals("licensePlate", violation.getPropertyPath().toString());
        assertEquals(invalidLicensePlate, violation.getInvalidValue());
    }

    // valid test
    @Test
    public void givenValidNumberOfSeats_whenCreatingCar_thenCarIsCreatedWithNumberOfSeats() {
        assertNotNull(carOne);
        assertEquals(validNumberOfSeats, carOne.getNumberOfSeats());
        Set<ConstraintViolation<Car>> violations = validator.validate(carOne);
        assertTrue(violations.isEmpty());
    }

    // Invalid test
    @Test
    public void givenInvalidNumberOfSeats_whenCreatingCar_thenBrandViolationMessageIsThrown() {}

    // valid test
    @Test
    public void givenValidNumberOfChildSeats_whenCreatingCar_thenCarIsCreatedWithNumberOfChildSeats() {
        assertNotNull(carOne);
        assertEquals(validNumberOfChildSeats, carOne.getNumberOfChildSeats());
        Set<ConstraintViolation<Car>> violations = validator.validate(carOne);
        assertTrue(violations.isEmpty());
    }

    // Invalid test
    @Test
    public void givenInvalidNumberOfChildSeats_whenCreatingCar_thenBrandViolationMessageIsThrown() {}

    // valid test
    @Test
    public void givenValidFoldingRearSeat_whenCreatingCar_thenCarIsCreatedWithFoldingRearSeat() {
        assertNotNull(carOne);
        assertEquals(validFoldingRearSeat, carOne.getFoldingRearSeat());
        Set<ConstraintViolation<Car>> violations = validator.validate(carOne);
        assertTrue(violations.isEmpty());
    }

    // valid test
    @Test
    public void givenValidTowBar_whenCreatingCar_thenCarIsCreatedWithTowBar() {
        assertNotNull(carOne);
        assertEquals(validTowBar, carOne.getTowBar());
        Set<ConstraintViolation<Car>> violations = validator.validate(carOne);
        assertTrue(violations.isEmpty());
    }
    
}
