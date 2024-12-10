package org.gymCrm.storage;

import org.gymCrm.model.Trainee;
import org.gymCrm.model.Trainer;
import org.gymCrm.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageTest {

    private InMemoryStorage inMemoryStorage;

    @BeforeEach
    void setUp(){
        inMemoryStorage= new InMemoryStorage();
    }

    @Test
    void getTraineeMap() {
        Map<Integer, Trainee>traineeMap = inMemoryStorage.getTraineeMap();
        assertNotNull(traineeMap,"TraineeMap should not be null");
        assertTrue(traineeMap.isEmpty(),"TraineeMap should be empty");
    }

    @Test
    void getTrainerMap() {
        Map<Integer, Trainer> trainerMap = inMemoryStorage.getTrainerMap();
        assertNotNull(trainerMap,"TrainerMap should not be null");
        assertTrue(trainerMap.isEmpty(),"TrainerMap should be empty");
    }

    @Test
    void getTrainingMap() {
        Map<Long, Training>trainingMap = inMemoryStorage.getTrainingMap();
        assertNotNull(trainingMap,"TrainingMap should not be null");
        assertTrue(trainingMap.isEmpty(),"TrainingMap should be empty");
    }

}