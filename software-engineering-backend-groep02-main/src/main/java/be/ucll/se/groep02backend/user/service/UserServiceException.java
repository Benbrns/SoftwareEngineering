package be.ucll.se.groep02backend.user.service;

public class UserServiceException extends Exception {
    private String field;

    public String getField() {
        return field;
    }

    public UserServiceException(String field, String message){
        super(message);
        this.field = field;
    }

}


