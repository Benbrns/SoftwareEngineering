package be.ucll.se.groep02backend.complaint.service;

public class ComplaintServiceException extends Exception {
    private String field;

    public String getField() {
        return field;
    }

    public ComplaintServiceException(String field, String message){
        super(message);
        this.field = field;
    }
    
}
