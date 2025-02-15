package org.gymCrm.hibernate.util;

import org.gymCrm.hibernate.exception.AuthenticationException;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setUsername("testUser");
        trainee.setPassword("encodedPassword");
        trainee.setActive(true);
    }

    @Test
    void authenticate_ShouldReturnTrue_WhenCredentialsAreValid() {
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(trainee));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        boolean result = authenticationService.authenticate("testUser", "rawPassword");

        assertTrue(result);
        verify(userService, times(1)).findByUsername("testUser");
        verify(passwordEncoder, times(1)).matches("rawPassword", "encodedPassword");
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        when(userService.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(AuthenticationException.class, () ->
                authenticationService.authenticate("unknownUser", "password"));

        assertEquals("Authentication failed for user: unknownUser", exception.getMessage());
        verify(userService, times(1)).findByUsername("unknownUser");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIsIncorrect() {
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(trainee));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        Exception exception = assertThrows(AuthenticationException.class, () ->
                authenticationService.authenticate("testUser", "wrongPassword"));

        assertEquals("Authentication failed for user: testUser", exception.getMessage());
        verify(userService, times(1)).findByUsername("testUser");
        verify(passwordEncoder, times(1)).matches("wrongPassword", "encodedPassword");
    }
}