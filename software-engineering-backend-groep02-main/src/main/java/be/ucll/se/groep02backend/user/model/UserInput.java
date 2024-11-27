package be.ucll.se.groep02backend.user.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInput {
    @ApiModelProperty(value = "Description of your field", example = "example@matteo.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email value is invalid, it has to be of the following format xxx@yyy.zzz")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    
    @NotNull(message = "role renter is required")
    private Boolean isRenter;
    @NotNull(message = "role owner is required")
    private Boolean isOwner;
    

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+\\d{1,3})?[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotNull(message = "Birth date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "Identification number of national register is required")
    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}", message = "Identification number is not in the right format!")
    private String nationalRegisterNumber;

    @NotBlank(message = "license number is required")
    @Pattern(regexp = "\\d{10}", message = "Driving license number is not in the right format!")
    private String licenseNumber;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

   

    public Boolean getIsRenter() {
        return isRenter;
    }

    public void setIsRenter(Boolean isRenter) {
        this.isRenter = isRenter;
    }

    public Boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }

   
    

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationalRegisterNumber() {
        return nationalRegisterNumber;
    }

    public void setNationalRegisterNumber(String nationalRegisterNumber) {
        this.nationalRegisterNumber = nationalRegisterNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
