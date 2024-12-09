package org.example.config;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.storage.InMemoryStorage;
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
        when(inMemoryStorage.getTraineeMap()).thenReturn(new HashMap<String, Trainee>());

        assertNotNull(appConfig.traineeDAO(inMemoryStorage));
        verify(inMemoryStorage, times(1)).getTraineeMap();
    }

    @Test
    public void testTrainerDAOBeanCreation() {
        when(inMemoryStorage.getTrainerMap()).thenReturn(new HashMap<String, Trainer>());

        assertNotNull(appConfig.trainerDAO(inMemoryStorage));
        verify(inMemoryStorage, times(1)).getTrainerMap();
    }

    @Test
    public void testTrainingDAOBeanCreation() {
        when(inMemoryStorage.getTrainingMap()).thenReturn(new HashMap<String, Training>());

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
