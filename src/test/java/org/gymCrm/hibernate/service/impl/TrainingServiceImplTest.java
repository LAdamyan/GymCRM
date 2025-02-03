package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.repo.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingRepository trainingRepository;

    private Training training;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        training = new Training();
        training.setTrainingName("Morning Cardio");
        training.setTrainingDate(new Date());
        training.setDuration(60);
        training.setTrainingType(new TrainingType("CARDIO"));
    }

    @Test
    void testCreate() {
        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        trainingService.create(training);

        verify(trainingRepository).save(training);
        assertNotNull(training);
    }

    @Test
    void testSelectByType() {
        TrainingType type = new TrainingType("CARDIO");
        List<Training> expectedTrainings = new ArrayList<>();
        expectedTrainings.add(training);

        when(trainingRepository.findByTrainingType(type)).thenReturn(expectedTrainings);

        Optional<List<Training>> result = trainingService.selectByType(type);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals(training.getTrainingName(), result.get().get(0).getTrainingName());
    }
    @Test
    void testGetTraineeTrainings() {
        String username = "john.doe";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "Trainer Name";
        TrainingType trainingType = new TrainingType("CARDIO");

        List<Training> trainings = new ArrayList<>();
        trainings.add(training);

        when(trainingRepository.findTraineeTrainings(anyString(), any(Date.class), any(Date.class), anyString(), anyString()))
                .thenReturn(trainings);

        Optional<List<Training>> result = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        assertTrue(result.isPresent());
        assertFalse(result.get().isEmpty());
        assertEquals(training.getTrainingName(), result.get().get(0).getTrainingName());
    }
    @Test
    void testGetTrainerTrainings(){
        String username = "Trainer.username";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "Trainee.username";

        List<Training>trainings = new ArrayList<>();
        trainings.add(training);

        when(trainingRepository.findTrainerTrainings(anyString(),any(Date.class),any(Date.class),anyString())).thenReturn(trainings);

        Optional<List<Training>>result = trainingService.getTrainerTrainings(username,fromDate,toDate,traineeName);

        assertTrue(result.isPresent());
        assertFalse(result.get().isEmpty());
        assertEquals(training.getTrainingName(), result.get().get(0).getTrainingName());
    }
}