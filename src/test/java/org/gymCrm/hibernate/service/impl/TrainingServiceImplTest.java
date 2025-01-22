package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.dao.TrainingDAO;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.model.User;
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

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private UserDetailsService<User> userDetailsService;

    private Training training;
    private TrainingType trainingType;


    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setTypeName("Yoga");

        training = new Training();
        training.setTrainingName("Morning Yoga");
        training.setTrainingDate(new Date());
        training.setDuration(60);
        training.setTrainingType(trainingType);
    }
    @Test
    public void testCreateTraining() {

        trainingService.createTraining(training, "trainerUsername", "trainerPassword");

        verify(trainingDAO, times(1)).create(training);
    }

    @Test
    public void testGetTrainingByType() {
        when(trainingDAO.selectByType(trainingType)).thenReturn(Optional.of(List.of(training)));

        Optional<List<Training>> result = trainingService.getTrainingByType(trainingType, "username", "password");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("Morning Yoga", result.get().get(0).getTrainingName());

    }

    @Test
    public void testGetTraineeTrainings() {
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "Tom";
        String username = "traineeUsername";

        when(trainingDAO.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType))
                .thenReturn(Optional.of(List.of(training)));


        Optional<List<Training>> result = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("Morning Yoga", result.get().get(0).getTrainingName());
    }
    @Test
    public void testGetTrainerTrainings() {
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "Lil.Adamyan";
        String username = "trainerUsername";

        when(trainingDAO.getTrainerTrainings(username, fromDate, toDate, traineeName))
                .thenReturn(Optional.of(List.of(training)));

        Optional<List<Training>> result = trainingService.getTrainerTrainings(username, fromDate, toDate, traineeName);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("Morning Yoga", result.get().get(0).getTrainingName());

    }
    @Test
    public void testGetAllTrainingTypes() {
        when(trainingDAO.getDistinctTrainingTypes()).thenReturn(List.of("Yoga", "Pilates"));

        List<String> result = trainingService.getAllTrainingTypes();

        assertEquals(2, result.size());
        assertTrue(result.contains("Yoga"));
        assertTrue(result.contains("Pilates"));
    }


   }