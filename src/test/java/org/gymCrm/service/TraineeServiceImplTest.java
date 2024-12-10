package org.gymCrm.service;

import org.gymCrm.dao.TraineeDAO;
import org.gymCrm.model.Trainee;
import org.gymCrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TraineeServiceImplTest {

    @Mock
    private TraineeDAO traineeDAO;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        doNothing().when(traineeDAO).create(trainee);
        traineeService.saveTrainee(trainee);

        verify(traineeDAO, times(1)).create(trainee);
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        when(traineeDAO.selectById(1)).thenReturn(trainee);

        traineeService.updateTrainee(trainee);
        verify(traineeDAO, times(1)).update(trainee);
    }

    @Test
    void testGetAllTrainees() {
        List<Trainee> mockTrainees = List.of(new Trainee(), new Trainee());
        when(traineeDAO.listAll()).thenReturn(mockTrainees);

        List<Trainee> result = traineeService.getAllTrainees();

        assertEquals(2, result.size());
        verify(traineeDAO, times(1)).listAll();
    }

    @Test
    void testGetTraineeById() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        when(traineeDAO.selectById(1)).thenReturn(trainee);

        Trainee result = traineeService.getTraineeById(1);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(traineeDAO, times(1)).selectById(1);
    }
    @Test
    void testDeleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        when(traineeDAO.selectById(1)).thenReturn(trainee);
        doNothing().when(traineeDAO).delete(1);

        traineeService.deleteTrainee(1);
        verify(traineeDAO, times(1)).delete(1);
    }
}
