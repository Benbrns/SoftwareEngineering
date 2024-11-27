package be.ucll.se.groep02backend.rental.service;

public class RentalServiceException extends Exception {
    private String field;

    public String getField() {
        return field;
    }

    public RentalServiceException(String field, String message){
        super(message);
        this.field = field;
    }

}


