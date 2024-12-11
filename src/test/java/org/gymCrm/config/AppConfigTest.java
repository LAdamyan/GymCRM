package org.gymCrm.config;

import org.gymCrm.model.Trainee;
import org.gymCrm.model.Trainer;
import org.gymCrm.model.Training;
import org.gymCrm.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AppConfigTest {
    @Mock
    private InMemoryStorage inMemoryStorage;

    @InjectMocks
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testInMemoryStorageBeanCreation() {
        assertNotNull(appConfig.inMemoryStorage());
    }

    @Test
    public void testTraineeDAOBeanCreation() {
        when(inMemoryStorage.getTraineeMap()).thenReturn(new HashMap<Integer, Trainee>());

        assertNotNull(appConfig.traineeDAO(inMemoryStorage));
        verify(inMemoryStorage, times(1)).getTraineeMap();
    }

    @Test
    public void testTrainerDAOBeanCreation() {
        when(inMemoryStorage.getTrainerMap()).thenReturn(new HashMap<Integer, Trainer>());

        assertNotNull(appConfig.trainerDAO(inMemoryStorage));
        verify(inMemoryStorage, times(1)).getTrainerMap();
    }

    @Test
    public void testTrainingDAOBeanCreation() {
        when(inMemoryStorage.getTrainingMap()).thenReturn(new HashMap<Long ,Training>());

        assertNotNull(appConfig.trainingDAO(inMemoryStorage));
        verify(inMemoryStorage, times(1)).getTrainingMap();
    }

    @Test
    public void testTrainerServiceBeanCreation() {
        assertNotNull(appConfig.trainerService());
    }

    @Test
    public void testTraineeServiceBeanCreation() {
        assertNotNull(appConfig.traineeService());
    }

    @Test
    public void testTrainingServiceBeanCreation() {
        assertNotNull(appConfig.trainingService());
    }
}
