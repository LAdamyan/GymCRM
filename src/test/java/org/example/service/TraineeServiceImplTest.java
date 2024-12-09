package org.example.service;

import org.example.dao.TraineeDAO;
import org.example.model.Trainee;
import org.example.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TraineeServiceImplTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private Logger logger;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setLogger(traineeService, logger);
    }

    private void setLogger(TraineeServiceImpl traineeService, Logger logger) throws Exception {
        Field loggerField = TraineeServiceImpl.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(traineeService, logger);
    }

    @Test
    void testSaveTrainee() {
        Trainee trainee = new Trainee();
        traineeService.saveTrainee(trainee);

        verify(traineeDAO, times(1)).create(trainee);
        verify(logger, times(1)).info(anyString());
    }

    @Test
    void testGetAllTrainees() {
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        when(traineeDAO.listAll()).thenReturn(Arrays.asList(trainee1, trainee2));

        List<Trainee> trainees = traineeService.getAllTrainees();

        assertEquals(2, trainees.size());
        verify(traineeDAO, times(1)).listAll();
        verify(logger, times(1)).info(anyString());
    }

    @Test
    void testGetTraineeById() {
        Trainee trainee = new Trainee();
        when(traineeDAO.selectById(1)).thenReturn(trainee);

        Trainee result = traineeService.getTraineeById(1);

        assertNotNull(result);
        assertEquals(trainee, result);
        verify(traineeDAO, times(1)).selectById(1);
        verify(logger, times(1)).info(anyString());
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        traineeService.updateTrainee(trainee);

        verify(traineeDAO, times(1)).update(trainee);
        verify(logger, times(1)).info(anyString());
    }

    @Test
    void testDeleteTrainee() {
        traineeService.deleteTrainee(1);

        verify(traineeDAO, times(1)).delete(1);
        verify(logger, times(1)).info(anyString());
    }
}
