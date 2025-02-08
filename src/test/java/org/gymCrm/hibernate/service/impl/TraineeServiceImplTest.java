package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.endpoint.CustomMetrics;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.repo.TraineeRepository;
import org.gymCrm.hibernate.util.AuthenticationService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserCredentialsUtil userCredentialsUtil;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setId(1);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("john_doe");
        trainee.setPassword("password");
        trainee.setActive(true);
    }

    @Test
    void testCreateTrainee() {

        when(userCredentialsUtil.generateUsername(anyString(), anyString())).thenReturn("john_doe");
        when(traineeRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Map<String, String> result = traineeService.create(trainee);

        assertNotNull(result);
        assertEquals("john_doe", result.get("username"));
        assertNotNull(result.get("password"));
        verify(traineeRepository).save(any(Trainee.class));
    }
    @Test
    void testUpdateTrainee() {

        when(authenticationService.authenticate(trainee.getUsername(), "password")).thenReturn(true);
        when(traineeRepository.existsById(1)).thenReturn(true);

        traineeService.update(trainee, "john_doe", "password");

        verify(traineeRepository).save(trainee);
    }

    @Test
    void testUpdateTraineeWithInvalidAuthentication() {

        when(authenticationService.authenticate(trainee.getUsername(), "wrongPassword")).thenReturn(false);

        SecurityException thrown = assertThrows(SecurityException.class, () -> {
            traineeService.update(trainee, "john_doe", "wrongPassword");
        });
        assertEquals("User john_doe not authenticated, permission denied!", thrown.getMessage());
    }

    @Test
    void testDeleteTrainee() {

        when(authenticationService.authenticate("john_doe", "password")).thenReturn(true);

        traineeService.delete("john_doe", "password");

        verify(traineeRepository).deleteByUsername("john_doe");
    }

    @Test
    void testDeleteTraineeWithInvalidAuthentication() {

        when(authenticationService.authenticate("john_doe", "wrongPassword")).thenReturn(false);

        SecurityException thrown = assertThrows(SecurityException.class, () -> {
            traineeService.delete("john_doe", "wrongPassword");
        });
        assertEquals("User john_doe not authenticated, permission denied!", thrown.getMessage());
    }

    @Test
    void testSelectByUsername() {

        when(traineeRepository.findByUsername("john_doe")).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.selectByUsername("john_doe");

        assertTrue(foundTrainee.isPresent());
        assertEquals("john_doe", foundTrainee.get().getUsername());
    }

    @Test
    void testChangeTraineePassword() {

        when(authenticationService.authenticate("john_doe", "password")).thenReturn(true);
        when(traineeRepository.findByUsername("john_doe")).thenReturn(Optional.of(trainee));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        traineeService.changeTraineePassword("john_doe", "password", "newPassword");

        assertEquals("encodedNewPassword", trainee.getPassword());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void testChangeTraineePasswordWithInvalidAuthentication() {
        when(authenticationService.authenticate("john_doe", "wrongPassword")).thenReturn(false);

        SecurityException thrown = assertThrows(SecurityException.class, () -> {
            traineeService.changeTraineePassword("john_doe", "wrongPassword", "newPassword");
        });
        assertEquals("User john_doe not authenticated, permission denied!", thrown.getMessage());
    }

    @Test
    void testChangeTraineeActiveStatus() {

        when(authenticationService.authenticate("john_doe", "password")).thenReturn(true);
        when(traineeRepository.findByUsername("john_doe")).thenReturn(Optional.of(trainee));

        traineeService.changeTraineeActiveStatus("john_doe", "password", false);

        assertFalse(trainee.isActive());
        verify(traineeRepository).save(trainee);
    }
}