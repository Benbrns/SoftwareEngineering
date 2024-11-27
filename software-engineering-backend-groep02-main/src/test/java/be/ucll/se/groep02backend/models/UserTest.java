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

import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserTest {

    private String validFirstName = "John";
    private String validLastName = "Doe";
    private String validEmail = "johndoe@ucll.com";
    private String validPassword = "john1234";
    private String validPhoneNumber = "0123456789";
    private LocalDate validBirthDate = LocalDate.parse("1998-12-23");
    private String validNationalRegistrationNumber = "00.00.00-000.00";
    private String validLicenseNumber = "0123456789";

    private User userOne;
    
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
        userOne = new User(0, validFirstName, validLastName, validEmail, validPassword, validPhoneNumber, validBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);
    }

    @Test
    public void givenValidFirstNameLastNameEmailPasswordPhoneNumberBirthDateNationalRegistrationNumberLicenseNumberWhenCreatingUser_thenUserIsCreatedWithThatFirstNameLastNameEmailPasswordPhoneNumberBirthDateNationalRegistrationNumberLicenseNumber() {
        assertNotNull(userOne);
        assertEquals(validFirstName, userOne.getFirstName());
        assertEquals(validLastName, userOne.getLastName());
        assertEquals(validEmail, userOne.getEmail());
        assertEquals(validPassword, userOne.getPassword());
        assertEquals(validPhoneNumber, userOne.getPhoneNumber());
        assertEquals(validBirthDate, userOne.getBirthDate());
        assertEquals(validNationalRegistrationNumber, userOne.getNationalRegisterNumber());
        assertEquals(validLicenseNumber, userOne.getLicenseNumber());
        Set<ConstraintViolation<User>> violations = validator.validate(userOne);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenInvalidFirstName_whenUserIsCreated_thenFirstNameViolationMessageIsThrown() {
        // when
        String inValidFirstName = "";
        User user = new User(0, inValidFirstName, validLastName, validEmail, validPassword, validPhoneNumber, validBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("FirstName is required", violation.getMessage());
        assertEquals("firstName", violation.getPropertyPath().toString());
        assertEquals(inValidFirstName, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidLastName_whenUserIsCreated_thenLastNameViolationMessageIsThrown() {
        // when
        String inValidLastName = "";
        User user = new User(0, validFirstName, inValidLastName, validEmail, validPassword, validPhoneNumber, validBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("LastName is required", violation.getMessage());
        assertEquals("lastName", violation.getPropertyPath().toString());
        assertEquals(inValidLastName, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidEmail_whenUserIsCreated_thenEmailViolationMessageIsThrown() {
        // when
        String inValidEmail = "";
        User user = new User(0, validFirstName, validLastName, inValidEmail, validPassword, validPhoneNumber, validBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email is required", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals(inValidEmail, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidEmailInTheWrongFormat_whenUserIsCreated_thenEmailViolationMessageIsThrown() {
        // when
        String inValidEmail = "johnucll.com";
        User user = new User(0, validFirstName, validLastName, inValidEmail, validPassword, validPhoneNumber, validBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email value is invalid, it has to be of the following format xxx@yyy.zzz", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals(inValidEmail, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidPassword_whenUserIsCreated_thenPasswordViolationMessageIsThrown() {
        // when
        String inValidPassword = "";
        User user = new User(0, validFirstName, validLastName, validEmail, inValidPassword, validPhoneNumber, validBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Password is required", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
        assertEquals(inValidPassword, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidPhoneNumber_whenUserIsCreated_thenPhoneNumberViolationMessageIsThrown() {
        // when
        String inValidPhoneNumber = null;
        User user = new User(0, validFirstName, validLastName, validEmail, validPassword, inValidPhoneNumber, validBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Phone number is required", violation.getMessage());
        assertEquals("phoneNumber", violation.getPropertyPath().toString());
        assertEquals(inValidPhoneNumber, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidPhoneNumberInWrongFormat_whenUserIsCreated_thenPhoneNumberViolationMessageIsThrown() {
        // when
        String inValidPhoneNumber = "00000";
        User user = new User(0, validFirstName, validLastName, validEmail, validPassword, inValidPhoneNumber, validBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Invalid phone number format", violation.getMessage());
        assertEquals("phoneNumber", violation.getPropertyPath().toString());
        assertEquals(inValidPhoneNumber, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidBirthDate_whenUserIsCreated_thenBirthDateViolationMessageIsThrown() {
        // when
        LocalDate inValidBirthDate = null;
        User user = new User(0, validFirstName, validLastName, validEmail, validPassword, validPhoneNumber, inValidBirthDate, validNationalRegistrationNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Birth date is required", violation.getMessage());
        assertEquals("birthDate", violation.getPropertyPath().toString());
        assertEquals(inValidBirthDate, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidNationalRegisterNumber_whenUserIsCreated_thenNationalRegisterNumberViolationMessageIsThrown() {
        // when
        String inValidNationalRegisterNumber = null;
        User user = new User(0, validFirstName, validLastName, validEmail, validPassword, validPhoneNumber, validBirthDate, inValidNationalRegisterNumber, validLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Identification number of national register is required", violation.getMessage());
        assertEquals("nationalRegisterNumber", violation.getPropertyPath().toString());
        assertEquals(inValidNationalRegisterNumber, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidLicenseNumber_whenUserIsCreated_thenLicenseNumberViolationMessageIsThrown() {
        // when
        String inValidLicenseNumber = null;
        User user = new User(0, validFirstName, validLastName, validEmail, validPassword, validPhoneNumber, validBirthDate, validNationalRegistrationNumber, inValidLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Driving license number is required", violation.getMessage());
        assertEquals("licenseNumber", violation.getPropertyPath().toString());
        assertEquals(inValidLicenseNumber, violation.getInvalidValue());

    }

    @Test
    public void givenInvalidLicenseNumberWrongFormat_whenUserIsCreated_thenLicenseNumberViolationMessageIsThrown() {
        // when
        String inValidLicenseNumber = "000";
        User user = new User(0, validFirstName, validLastName, validEmail, validPassword, validPhoneNumber, validBirthDate, validNationalRegistrationNumber, inValidLicenseNumber, null, null, null);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Driving license number is not in the right format!", violation.getMessage());
        assertEquals("licenseNumber", violation.getPropertyPath().toString());
        assertEquals(inValidLicenseNumber, violation.getInvalidValue());

    }
    
    @Test
    public void testAddAuthority() {
        User user = new User();
        Role role = Role.ADMIN;
        user.addAuthority(role);
        assertTrue(user.getRoles().contains(role));
    }
    
}
