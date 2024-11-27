package be.ucll.se.groep02backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import be.ucll.se.groep02backend.user.model.PublicUser;
import be.ucll.se.groep02backend.user.model.Role;
import be.ucll.se.groep02backend.user.model.User;
import be.ucll.se.groep02backend.user.repo.UserRepository;
import be.ucll.se.groep02backend.user.service.UserService;
import be.ucll.se.groep02backend.user.service.UserServiceException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private final PasswordEncoder passwordEncoder = null;
    
    User userOne = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password123")
                .phoneNumber("1234567890")
                .birthDate(LocalDate.of(1990, 5, 15))
                .nationalRegisterNumber("00.00.00-000.00")
                .licenseNumber("1234567890")
                .build();

    @Test
    public void givenAdminRole_whenAddingRole_thenRoleIsAddedSuccessfully() throws UserServiceException {
        // given
        userOne.addAuthority(Role.OWNER);
        when(userRepository.save(userOne)).thenReturn(userOne);

        // when
        String result = userService.addRole("ADMIN", userOne);

        // then
        assertEquals("Role added", result);
        assertEquals(2, userOne.getRoles().size());
        verify(userRepository).save(userOne);
    }

    @Test
    public void givenAlreadyExistingRole_whenAddingRole_thenThrowsUserServiceException() {
        // given
        userOne.addAuthority(Role.ADMIN);

        // when
        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.addRole("ADMIN", userOne));

        // then
        assertEquals("User", exception.getField());
        assertEquals("User already has this role: ADMIN", exception.getMessage());
    }

    @Test
    public void givenInvalidRole_whenAddingRole_thenThrowsUserServiceException() {
        // given
        userOne.addAuthority(Role.OWNER);

        // when
        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.addRole("INVALID_ROLE", userOne));

        // then
        assertEquals("User", exception.getField());
        assertEquals("Role not found", exception.getMessage());
    }
}
