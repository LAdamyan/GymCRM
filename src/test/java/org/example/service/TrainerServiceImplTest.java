package org.example.service;

import org.example.dao.TrainerDAO;
import org.example.model.Trainer;
import org.example.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class TrainerServiceImplTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private Logger logger;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setLogger(trainerService, logger);
    }

    private void setLogger(TrainerServiceImpl trainerService, Logger logger) throws Exception {
        Field loggerField = TrainerServiceImpl.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(trainerService, logger);
    }

    @Test
    void testSaveTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");

        trainerService.saveTrainer(trainer);

        verify(trainerDAO, times(1)).create(trainer);
        verify(logger, times(1)).info("Trainer John Doe");
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");

        trainerService.updateTrainer(trainer);

        verify(trainerDAO, times(1)).update(trainer);
        verify(logger, times(1)).info("Trainer John Doe");
    }

    @Test
    void testGetTrainerById() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        when(trainerDAO.selectById(1)).thenReturn(trainer);

        Trainer result = trainerService.getTrainerById(1);

        Assertions.assertEquals(trainer, result);
        verify(logger, times(1)).info("Get trainer by id " + 1);
    }
}
