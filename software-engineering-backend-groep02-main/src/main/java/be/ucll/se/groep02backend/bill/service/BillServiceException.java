package be.ucll.se.groep02backend.bill.service;

public class BillServiceException extends Exception{

    private String field;

    public String getField() {
        return field;
    }

    public BillServiceException(String field, String message){
        super(message);
        this.field = field;
    }
    
}
