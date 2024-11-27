package be.ucll.se.groep02backend.rent.service;

public class RentServiceException extends Exception {
    private String field;

    public String getField() {
        return field;
    }

    public RentServiceException(String field, String message){
        super(message);
        this.field = field;
    }

}


