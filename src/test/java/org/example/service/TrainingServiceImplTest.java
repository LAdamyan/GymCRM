package org.example.service;

import org.example.dao.TrainingDAO;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.example.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingServiceImplTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateTraining() {
        Training training = mock(Training.class);
        Trainee trainee = mock(Trainee.class);
        Trainer trainer = mock(Trainer.class);

        when(trainee.getUsername()).thenReturn("traineeUser");
        when(trainer.getUsername()).thenReturn("trainerUser");
        when(training.getTrainee()).thenReturn(trainee);
        when(training.getTrainer()).thenReturn(trainer);
        when(training.getTrainingName()).thenReturn("TrainingSession");

        doNothing().when(trainingDAO).create(training);
        trainingService.createTraining(training);

        verify(trainingDAO, times(1)).create(training);
    }

    @Test
    void testGetTrainingByType_FoundTrainings() {
        TrainingType type = TrainingType.AEROBICS;
        List<Training> mockTrainings = List.of(new Training(), new Training());

        when(trainingDAO.selectByType(type)).thenReturn(mockTrainings);

        List<Training> result = trainingService.getTrainingByType(type);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainingDAO, times(1)).selectByType(type);
    }

    @Test
    void testGetTrainingByType_NoTrainingsFound() {
        TrainingType type = TrainingType.POWERLIFTING;
        List<Training> noTrainings = new ArrayList<>();

        when(trainingDAO.selectByType(type)).thenReturn(noTrainings);

        List<Training> result = trainingService.getTrainingByType(type);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trainingDAO, times(1)).selectByType(type);
    }
}
