package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.endpoint.CustomMetrics;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.repo.TraineeRepository;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class TraineeServiceImplTest {

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private CustomMetrics customMetrics;

    @Mock
    private UserCredentialsUtil userCredentialsUtil;

    @Mock
    private UserDetailsService<Trainee> userDetailsService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainee = new Trainee();
        trainee.setId(1);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("john_doe");
        trainee.setPassword("password123");
        trainee.setActive(true);
    }

    @Test
    void testCreateTrainee() {
        try (MockedStatic<UserCredentialsUtil> mockedStatic = Mockito.mockStatic(UserCredentialsUtil.class)) {
            mockedStatic.when(() -> UserCredentialsUtil.generateUsername(anyString(), anyString()))
                    .thenReturn("mocked_username");
            when(userCredentialsUtil.generatePassword()).thenReturn("generatedPassword");
            when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

            traineeService.create(trainee);

            assertEquals("mocked_username", trainee.getUsername());
            assertEquals("generatedPassword", trainee.getPassword());
            verify(traineeRepository, times(1)).save(any(Trainee.class));
            verify(customMetrics, times(1)).incrementTraineeRegistration();
        }

    }
    @Test
    void testUpdateTrainee() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(traineeRepository.existsById(anyInt())).thenReturn(true);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.update(trainee, "john_doe", "password123");

        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testDeleteTrainee() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        doNothing().when(traineeRepository).deleteByUsername(anyString());

        traineeService.delete("john_doe", "password123");

        verify(traineeRepository, times(1)).deleteByUsername("john_doe");
    }
    @Test
    void testSelectByUsername() {
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.selectByUsername("john_doe");

        assertTrue(foundTrainee.isPresent());
        assertEquals("john_doe", foundTrainee.get().getUsername());
    }
    @Test
    void testListAllTrainees() {
        List<Trainee> trainees = List.of(trainee);
        when(traineeRepository.findAll()).thenReturn(trainees);

        List<Trainee> result = traineeService.listAll();

        assertEquals(1, result.size());
    }

    @Test
    void testChangeTraineePassword() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(trainee));

        traineeService.changeTraineePassword("john_doe", "password123", "newPassword");

        assertEquals("newPassword", trainee.getPassword());
        verify(traineeRepository, times(1)).save(trainee);
    }
    @Test
    void testChangeTraineeActiveStatus() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(trainee));

        traineeService.changeTraineeActiveStatus("john_doe", "password123", false);

        assertFalse(trainee.isActive());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testUpdateTraineeTrainers() {
        List<Trainer> trainers = new ArrayList<>();
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(trainee));

        traineeService.updateTraineeTrainers("john_doe", trainers);

        assertEquals(trainers, new ArrayList<>(trainee.getTrainers()));
        verify(traineeRepository, times(1)).save(trainee);
    }

}