package be.ucll.se.groep02backend.user.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Builder;



public class PublicUser {
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate birthDate;

    private String nationalRegisterNumber;

    private String licenseNumber;

    private Set<Role> roles = new HashSet<>();

    private String token;


    public PublicUser(String email, String firstName, String lastName, String phoneNumber, LocalDate birthDate, String nationalRegisterNumber, String licenseNumber, Set<Role> roles) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.nationalRegisterNumber = nationalRegisterNumber;
        this.licenseNumber = licenseNumber;
        this.roles = roles;
        
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getNationalRegisterNumber() {
        return nationalRegisterNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    

}
