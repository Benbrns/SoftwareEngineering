package be.ucll.se.groep02backend.auth;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep02backend.user.model.PublicUser;
import be.ucll.se.groep02backend.user.model.UserInput;
import be.ucll.se.groep02backend.user.service.UserServiceException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping()
@Tag(name = "Authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<PublicUser> register(@RequestBody @Valid UserInput user) throws UserServiceException, MessagingException, IOException{
        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<PublicUser> authenticate(@RequestBody @Valid AuthenticationRequest request) throws UserServiceException{
        return ResponseEntity.ok(service.authenticate(request));
    }


    
}
