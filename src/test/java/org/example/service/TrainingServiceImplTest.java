package org.example.service;

import org.example.dao.TrainingDAO;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.example.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class TrainingServiceImplTest {

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private Logger logger;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setLogger(trainingService, logger);
    }

    private void setLogger(TrainingService trainingService, Logger logger) throws Exception {
        Field loggerField = TrainingServiceImpl.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(trainingService, logger);
    }

    @Test
    void testCreateTraining() {
        Training training = new Training();
        training.setTrainingName("Test Training");

        trainingService.createTraining(training);

        verify(trainingDAO, times(1)).create(training);
        verify(logger, times(1)).info("Created training: " + training);
    }

    @Test
    void testGetTrainingByType() {
        TrainingType type = TrainingType.AEROBICS;
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        trainings.add(new Training());

        when(trainingDAO.selectByType(type)).thenReturn(trainings);

        List<Training> result = trainingService.getTrainingByType(type);

        Assertions.assertEquals(trainings, result);
        verify(logger, times(1)).info("Get trainings by type: " + type);
    }
}
