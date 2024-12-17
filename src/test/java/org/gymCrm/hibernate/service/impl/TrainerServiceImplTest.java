package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.dao.TrainerDAO;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserDetailsService<Trainer> userDetailsService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setUsername("lil.adam");
        trainer.setPassword("password");
    }

    @Test
    void saveTrainer_Success() {
        trainerService.saveTrainer(trainer);

        verify(trainerDAO, times(1)).create(trainer);
    }

    @Test
    void updateTrainer_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);

        trainerService.updateTrainer(trainer, "lil.adam", "7777");

        verify(userDetailsService, times(1)).authenticate("lil.adam", "7777");
        verify(trainerDAO, times(1)).update(trainer);
    }

    @Test
    void getTrainerByUsername_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(trainerDAO.selectByUsername("lil.adam")).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.getTrainerByUsername("lil.adam", "password123");

        assertTrue(result.isPresent());
        assertEquals("lil.adam", result.get().getUsername());
        verify(userDetailsService, times(1)).authenticate("lil.adam", "password123");
        verify(trainerDAO, times(1)).selectByUsername("lil.adam");
    }

    @Test
    void getTrainerByUsername_AuthenticationFailure() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () ->
                trainerService.getTrainerByUsername("john.doe", "wrongPassword")
        );

        verify(userDetailsService, times(1)).authenticate("john.doe", "wrongPassword");
        verifyNoInteractions(trainerDAO);
    }

    @Test
    void changeTrainersPassword_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);

        trainerService.changeTrainersPassword("lil.adam", "oldPassword", "newPassword");

        verify(userDetailsService, times(1)).authenticate("lil.adam", "oldPassword");
        verify(trainerDAO, times(1)).changeTrainersPassword("lil.adam", "newPassword");

    }

    @Test
    void activateTrainer_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);

        trainerService.activateTrainer("lil.adam", "7777");

        verify(userDetailsService, times(1)).authenticate("lil.adam", "7777");
        verify(trainerDAO, times(1)).activateTrainer("lil.adam");
    }

    @Test
    void deactivateTrainer_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);

        trainerService.deactivateTrainer("lil.adam", "7777");

        verify(userDetailsService, times(1)).authenticate("lil.adam", "7777");
        verify(trainerDAO, times(1)).deactivateTrainer("lil.adam");
    }

    @Test
    void getUnassignedTrainers_AuthenticationSuccess() {
        String username = "admin";
        String password = "admin123";
        String traineeUsername = "john.doe";

        Trainer trainer1 = new Trainer();
        trainer1.setUsername("trainer1");

        Trainer trainer2 = new Trainer();
        trainer2.setUsername("trainer2");

        List<Trainer> mockUnassignedTrainers = Arrays.asList(trainer1, trainer2);

        when(userDetailsService.authenticate(username, password)).thenReturn(true);
        when(trainerDAO.getUnassignedTrainers(traineeUsername)).thenReturn(Optional.of(mockUnassignedTrainers));

        Optional<List<Trainer>> result = trainerService.getUnassignedTrainers(username, password, traineeUsername);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals("trainer1", result.get().get(0).getUsername());
        assertEquals("trainer2", result.get().get(1).getUsername());

        verify(userDetailsService, times(1)).authenticate(username, password);
        verify(trainerDAO, times(1)).getUnassignedTrainers(traineeUsername);
    }

    @Test
    void getUnassignedTrainers_AuthenticationFailure() {
        String username = "admin";
        String password = "wrongPassword";
        String traineeUsername = "john.doe";

        when(userDetailsService.authenticate(username, password)).thenReturn(false);

        SecurityException exception = assertThrows(SecurityException.class, () ->
                trainerService.getUnassignedTrainers(username, password, traineeUsername)
        );

        assertEquals("User " + username + " not authenticated, permission denied!", exception.getMessage());

        verify(userDetailsService, times(1)).authenticate(username, password);
        verifyNoInteractions(trainerDAO);
    }

}