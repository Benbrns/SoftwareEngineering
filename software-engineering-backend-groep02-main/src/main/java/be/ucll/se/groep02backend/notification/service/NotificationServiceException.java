package be.ucll.se.groep02backend.notification.service;

public class NotificationServiceException extends Exception {
    private String field;

    public String getField() {
        return field;
    }

    public NotificationServiceException(String field, String message){
        super(message);
        this.field = field;
    }

}


