package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl<User> userDetailsService;

    @Mock
    private UserRepository<User> userRepository;

    private Trainee testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new Trainee();
        testUser.setUsername("john.doe");
        testUser.setPassword("password1");
        testUser.setActive(true);

    }

    @Test
    void testAuthenticateSuccess() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        boolean result = userDetailsService.authenticate(testUser.getUsername(), testUser.getPassword());

        assertTrue(result);
    }

    @Test
    void testAuthenticateInvalidPassword() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        boolean result = userDetailsService.authenticate(testUser.getUsername(), "wrongPassword");

        assertFalse(result);
    }

    @Test
    void testAuthenticateUserNotFound() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        boolean result = userDetailsService.authenticate(testUser.getUsername(),testUser.getPassword());

        assertFalse(result);
    }

}