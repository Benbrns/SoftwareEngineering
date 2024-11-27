package be.ucll.se.groep02backend.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import be.ucll.se.groep02backend.bill.service.BillServiceException;
import be.ucll.se.groep02backend.car.service.CarServiceException;
import be.ucll.se.groep02backend.complaint.service.ComplaintServiceException;
import be.ucll.se.groep02backend.notification.service.NotificationServiceException;
import be.ucll.se.groep02backend.rent.service.RentServiceException;
import be.ucll.se.groep02backend.rental.service.RentalServiceException;
import be.ucll.se.groep02backend.user.service.UserServiceException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ RentalServiceException.class })
    public Map<String, String> handleRentalServiceException(RentalServiceException ex) {
        return createErrorResponse(ex.getField(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ RentServiceException.class })
    public Map<String, String> handleRentServiceException(RentServiceException ex) {
        return createErrorResponse(ex.getField(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ CarServiceException.class })
    public Map<String, String> handleCarServiceException(CarServiceException ex) {
        return createErrorResponse(ex.getField(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ NotificationServiceException.class })
    public Map<String, String> handleNotificationServiceException(NotificationServiceException ex) {
        return createErrorResponse(ex.getField(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ UserServiceException.class })
    public Map<String, String> handleUserServiceException(UserServiceException ex) {
        return createErrorResponse(ex.getField(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ BillServiceException.class })
    public Map<String, String> handleUserServiceException(BillServiceException ex) {
        return createErrorResponse(ex.getField(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ComplaintServiceException.class })
    public Map<String, String> handleUserServiceException(ComplaintServiceException ex) {
        return createErrorResponse(ex.getField(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    

    private Map<String, String> createErrorResponse(String field, String message) {
        Map<String, String> errors = new HashMap<>();
        errors.put(field, message);
        return errors;
    }
}
