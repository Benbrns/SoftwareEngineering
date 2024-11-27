package be.ucll.se.groep02backend.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep02backend.user.model.PublicUser;
import be.ucll.se.groep02backend.user.service.UserService;
import be.ucll.se.groep02backend.user.service.UserServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import be.ucll.se.groep02backend.config.ApplicationConfig;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "User")
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping("/admin/all")
    public List<PublicUser> getUsers() throws UserServiceException {

        return userService.getAllUsers(ApplicationConfig.getAuthenticatedUser());

    }

    @Operation(summary = "Add role")
    @PutMapping("role/add/{role}")
    public String addRole(@PathVariable String role) throws UserServiceException {
        return userService.addRole(role, ApplicationConfig.getAuthenticatedUser());
    }

    @Operation(summary = "Get user")
    @GetMapping()
    public PublicUser getUser() throws UserServiceException{
        return userService.getUser(ApplicationConfig.getAuthenticatedUser());
    }

    


}
