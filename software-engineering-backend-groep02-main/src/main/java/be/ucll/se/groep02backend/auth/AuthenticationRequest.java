package be.ucll.se.groep02backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email value is invalid, it has to be of the following format xxx@yyy.zzz")
    private String email;

    @NotBlank(message = "Password is required")
    String password;

}
