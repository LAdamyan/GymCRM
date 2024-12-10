package org.gymCrm.service;

import org.gymCrm.dao.TrainerDAO;
import org.gymCrm.model.Trainer;
import org.gymCrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainerServiceImplTest {

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testSaveTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");

        doNothing().when(trainerDAO).create(trainer);
        trainerService.saveTrainer(trainer);

        verify(trainerDAO, times(1)).create(trainer);
    }

    @Test
    void testUpdateTrainer_ExistingTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setFirstName("Jane");
        trainer.setLastName("Doe");

        when(trainerDAO.selectById(1)).thenReturn(trainer);
        doNothing().when(trainerDAO).update(trainer);

        trainerService.updateTrainer(trainer);

        verify(trainerDAO, times(1)).update(trainer);
    }

    @Test
    void testUpdateTrainer_NonExistingTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(99);
        trainer.setFirstName("Unknown");
        trainer.setLastName("Person");

        when(trainerDAO.selectById(99)).thenReturn(null);

        trainerService.updateTrainer(trainer);

        verify(trainerDAO, times(0)).update(trainer);
        verify(trainerDAO, times(1)).selectById(99);
    }


    @Test
    void testGetTrainerById_Found() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");

        when(trainerDAO.selectById(1)).thenReturn(trainer);

        Trainer result = trainerService.getTrainerById(1);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(trainerDAO, times(1)).selectById(1);
    }

    @Test
    void testGetTrainerById_NotFound() {
        when(trainerDAO.selectById(99)).thenReturn(null);

        Trainer result = trainerService.getTrainerById(99);

        assertNull(result);
        verify(trainerDAO, times(1)).selectById(99);
    }
}
