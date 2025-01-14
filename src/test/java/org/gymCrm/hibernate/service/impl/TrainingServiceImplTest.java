package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.dao.TrainingDAO;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training mockTraining;
    private String username;
    private String password;

    @BeforeEach
    void setUp() {
        username = "testUser";
        password = "testPassword";
        mockTraining = new Training();
        mockTraining.setId(1);
    }

    @Test
    void createTraining_Success() {
        when(userDetailsService.authenticate(username, password)).thenReturn(true);

        trainingService.createTraining(mockTraining, username, password);

        verify(userDetailsService, times(1)).authenticate(username, password);
        verify(trainingDAO, times(1)).create(mockTraining);
    }

    @Test
    void createTraining_AuthenticationFailure() {
        when(userDetailsService.authenticate(username, password)).thenReturn(false);

        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainingService.createTraining(mockTraining, username, password));

        assertEquals("Authentication failed for user: testUser permission denied", exception.getMessage());
        verify(userDetailsService, times(1)).authenticate(username, password);
        verify(trainingDAO, never()).create(any());
    }

    @Test
    void getTrainingByType_Success() {
        TrainingType trainingTypeEntity = new TrainingType();
        trainingTypeEntity.setId(1);
        trainingTypeEntity.setTypeName("BODYBUILDING");


        Training mockTraining = new Training();
        mockTraining.setId(1);
        mockTraining.setTrainingName("Strength Training");
        mockTraining.setTrainingType(trainingTypeEntity);

        List<Training> trainings = Arrays.asList(mockTraining);

        when(userDetailsService.authenticate(username, password)).thenReturn(true);
        when(trainingDAO.selectByType(trainingTypeEntity)).thenReturn(Optional.of(trainings));

        Optional<List<Training>> result = trainingService.getTrainingByType(trainingTypeEntity, username, password);

        assertTrue(result.isPresent(), "Trainings should be present");
        assertEquals(1, result.get().size(), "There should be one training");
        assertEquals("Strength Training", result.get().get(0).getTrainingName(), "Training name should match");
        assertEquals("BODYBUILDING", result.get().get(0).getTrainingType().getTypeName(), "Training type should match");

        verify(userDetailsService, times(1)).authenticate(username, password);
        verify(trainingDAO, times(1)).selectByType(trainingTypeEntity);
    }

    @Test
    void getTraineeTrainings_Success() {
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer1";

        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName("BODYBUILDING");

        List<Training> trainings = Arrays.asList(mockTraining);

        when(userDetailsService.authenticate(username, password)).thenReturn(true);
        when(trainingDAO.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType))
                .thenReturn(Optional.of(trainings));

        Optional<List<Training>> result = trainingService.getTraineeTrainings(username, password, fromDate, toDate, trainerName, trainingType);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(userDetailsService, times(1)).authenticate(username, password);
        verify(trainingDAO, times(1)).getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
    }

    @Test
    void getTrainerTrainings_Success() {
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "trainee1";

        List<Training> trainings = Arrays.asList(mockTraining);

        when(userDetailsService.authenticate(username, password)).thenReturn(true);
        when(trainingDAO.getTrainerTrainings(username, fromDate, toDate, traineeName))
                .thenReturn(Optional.of(trainings));

        Optional<List<Training>> result = trainingService.getTrainerTrainings(username, password, fromDate, toDate, traineeName);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(userDetailsService, times(1)).authenticate(username, password);
        verify(trainingDAO, times(1)).getTrainerTrainings(username, fromDate, toDate, traineeName);
    }

    @Test
    void getTrainerTrainings_AuthenticationFailure() {
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "trainee1";

        when(userDetailsService.authenticate(username, password)).thenReturn(false);

        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainingService.getTrainerTrainings(username, password, fromDate, toDate, traineeName));

        assertEquals("Authentication failed for user: testUser permission denied", exception.getMessage());
        verify(userDetailsService, times(1)).authenticate(username, password);
        verify(trainingDAO, never()).getTrainerTrainings(any(), any(), any(), any());
    }
}