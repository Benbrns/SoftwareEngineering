package be.ucll.se.groep02backend.car.service;

public class CarServiceException extends Exception {
    private String field;

    public String getField() {
        return field;
    }

    public CarServiceException(String field, String message){
        super(message);
        this.field = field;
    }

}


