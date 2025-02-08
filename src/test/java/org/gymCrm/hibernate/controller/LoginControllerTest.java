package org.gymCrm.hibernate.controller;

import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.service.impl.UserDetailsServiceImpl;
import org.gymCrm.hibernate.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserDetailsServiceImpl<User> userDetailsService;

    @Mock
    private UserServiceImpl<User> userServiceImpl;

    private Trainee testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new Trainee();
        testUser.setUsername("john.doe");
        testUser.setPassword("password1");

    }

    @Test
    void testLoginSuccess() {

        when(userDetailsService.authenticate("john.doe", "password1")).thenReturn(true);

        ResponseEntity<String> response = loginController.login("john.doe", "password1");

        assertEquals("200 OK", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(userDetailsService).authenticate("john.doe", "password1");
    }

    @Test
    void testLoginFailure() {
        when(userDetailsService.authenticate("john.doe", "wrongPassword")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                loginController.login("john.doe", "wrongPassword"));

        assertEquals(401, exception.getStatusCode().value());
        assertEquals("Invalid username or password", exception.getReason());
    }

    @Test
    void testChangePasswordSuccess() {
        when(userDetailsService.authenticate("john.doe", "password1")).thenReturn(true);
        when(userServiceImpl.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        ResponseEntity<String> response = loginController.changePassword("john.doe", "password1", "newPassword");

        assertEquals("Password changed successfully", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(userServiceImpl).changePassword("john.doe", "newPassword");
    }
    @Test
    void testChangePasswordUserNotFound() {
        when(userDetailsService.authenticate("john.doe", "password1")).thenReturn(true);
        when(userServiceImpl.findByUsername("john.doe")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                loginController.changePassword("john.doe", "password1", "newPassword"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());

    }
    @Test
    void testChangePasswordInvalidOldPassword() {
        when(userDetailsService.authenticate("john.doe", "wrongPassword")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                loginController.changePassword("john.doe", "wrongPassword", "newPassword"));

        assertEquals(401, exception.getStatusCode().value());
        assertEquals("Invalid old password", exception.getReason());
    }

}