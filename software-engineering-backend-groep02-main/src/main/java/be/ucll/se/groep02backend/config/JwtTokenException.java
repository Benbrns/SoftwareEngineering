package be.ucll.se.groep02backend.config;

public class JwtTokenException extends Exception {
    private String field;

    public String getField() {
        return field;
    }

    public JwtTokenException(String field, String message){
        super(message);
        this.field = field;
    }

} 