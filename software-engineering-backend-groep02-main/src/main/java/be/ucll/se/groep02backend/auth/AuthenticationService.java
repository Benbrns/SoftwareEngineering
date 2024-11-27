package be.ucll.se.groep02backend.auth;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import be.ucll.se.groep02backend.user.service.UserService;
import be.ucll.se.groep02backend.user.service.UserServiceException;
import jakarta.mail.MessagingException;
import be.ucll.se.groep02backend.config.JwtService;
import lombok.RequiredArgsConstructor;
import be.ucll.se.groep02backend.user.model.PublicUser;
import be.ucll.se.groep02backend.user.model.User;
import be.ucll.se.groep02backend.user.model.UserInput;
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    

    public PublicUser register(UserInput user) throws UserServiceException, MessagingException, IOException{
        User createdUser = userService.createUser(user);
        
        var jwtToken = jwtService.generateToken(createdUser);
        PublicUser publicUser = userService.toPublicData(createdUser, jwtToken);
        return publicUser;
    }

    public PublicUser authenticate(AuthenticationRequest request) throws UserServiceException{
        try {
            authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }
        catch (Exception e) {
            throw new UserServiceException("User", "Invalid email or password");
        }
        User user = userService.loginUser(request.getEmail(),request.getPassword());
        if (user == null){
            throw new UserServiceException("User", "Invalid email or password");
        }
        var jwtToken = jwtService.generateToken(user);
        PublicUser publicUser = userService.toPublicData(user, jwtToken);
        return publicUser;

    }

}
