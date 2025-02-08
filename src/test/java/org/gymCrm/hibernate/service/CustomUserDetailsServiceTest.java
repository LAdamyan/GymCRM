package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Trainee mockTrainee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockTrainee = new Trainee();
        mockTrainee.setUsername("testUser");
        mockTrainee.setPassword("password123");
        mockTrainee.setActive(true);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockTrainee));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {

        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
        verify(userRepository, times(1)).findByUsername(username);
    }
}