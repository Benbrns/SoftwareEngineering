package be.ucll.se.groep02backend.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import be.ucll.se.groep02backend.config.email.EmailService;
import be.ucll.se.groep02backend.user.model.PublicUser;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;
import be.ucll.se.groep02backend.user.model.UserInput;
import be.ucll.se.groep02backend.user.repo.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final EmailService emailService;

    // user objects mapped to the PUBLICUser object
    public PublicUser toPublicData(User user) {
        return new PublicUser(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getBirthDate(), user.getNationalRegisterNumber(), user.getLicenseNumber(), user.getRoles());
    }
    public PublicUser toPublicData(User user, String token) {
        PublicUser publicUser = new PublicUser(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getBirthDate(), user.getNationalRegisterNumber(), user.getLicenseNumber(), user.getRoles());
        publicUser.setToken(token);
        return publicUser;
    }


    public List<PublicUser> removeSensitiveData(List<User> users) {
        List<PublicUser> publicUsers = new ArrayList<>();
        for (User u : users) {
            publicUsers.add(toPublicData(u));
        }
        return publicUsers;
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public List<PublicUser> getAllUsers(User user) {
        List<User> users = repository.findAll();
        return removeSensitiveData(users);
    }

    public PublicUser getUser(User user) {
        return toPublicData(user);
    }

    public User createUser(UserInput userInput) throws UserServiceException, MessagingException, IOException {
        Optional<User> foundOtherUser = repository.findByEmail(userInput.getEmail());
        if (foundOtherUser.isPresent()) {
            throw new UserServiceException("User", "User already exists");
        }
        User newUser = User.builder()
                .firstName(userInput.getFirstName())
                .lastName(userInput.getLastName())
                .email(userInput.getEmail())
                .password(passwordEncoder.encode(userInput.getPassword()))
                .phoneNumber(userInput.getPhoneNumber())
                .birthDate(userInput.getBirthDate())
                .nationalRegisterNumber(userInput.getNationalRegisterNumber())
                .licenseNumber(userInput.getLicenseNumber())
                .build();
        
        if (userInput.getIsRenter()) {
            newUser.addAuthority(Role.RENTER);
        }
        if (userInput.getIsOwner()) {
            newUser.addAuthority(Role.OWNER);
        }
        
        emailService.sendWelcomeEmail(newUser);
        return repository.save(newUser);

    }

    public User loginUser(String email, String password) {
        User user = repository.findByEmail(email).orElse(null);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public String addRole(String role, User authenticatedUser) throws UserServiceException {
        if (role.equalsIgnoreCase("ADMIN")) {
            if (authenticatedUser.getRoles().contains(Role.ADMIN)) {
                throw new UserServiceException("User", "User already has this role: " + Role.ADMIN);
            } else {
                authenticatedUser.addAuthority(Role.ADMIN);
                repository.save(authenticatedUser);
                return "Role added";
            }
        } else if (role.equalsIgnoreCase("RENTER")) {
            if (authenticatedUser.getRoles().contains(Role.RENTER)) {
                throw new UserServiceException("User", "User already has this role: " + Role.RENTER);
            } else {
                authenticatedUser.addAuthority(Role.RENTER);
                repository.save(authenticatedUser);
                return "Role added";
            }
        } else if (role.equalsIgnoreCase("OWNER")) {
            if (authenticatedUser.getRoles().contains(Role.OWNER)) {
                throw new UserServiceException("User", "User already has this role: " + Role.OWNER);
            } else {
                authenticatedUser.addAuthority(Role.OWNER);
                repository.save(authenticatedUser);
                return "Role added";
            }
        } else if (role.equalsIgnoreCase("ACCOUNTANT")) {
            if (authenticatedUser.getRoles().contains(Role.ACCOUNTANT)) {
                throw new UserServiceException("User", "User already has this role: " + Role.ACCOUNTANT);
            } else {
                authenticatedUser.addAuthority(Role.ACCOUNTANT);
                repository.save(authenticatedUser);
                return "Role added";
            }
        } else {
            throw new UserServiceException("User", "Role not found");
        }
    }

}
