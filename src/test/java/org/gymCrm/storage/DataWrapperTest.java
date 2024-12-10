package org.gymCrm.storage;

import org.gymCrm.model.Trainee;
import org.gymCrm.model.Trainer;
import org.gymCrm.model.Training;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataWrapperTest {

    @Test
    void testDataWrapper_SetAndGetTrainees() {
        DataWrapper dataWrapper = new DataWrapper();
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        List<Trainee> trainees = Arrays.asList(trainee1, trainee2);

        dataWrapper.setTrainees(trainees);

        assertEquals(trainees, dataWrapper.getTrainees());
    }

    @Test
    void testDataWrapper_SetAndGetTrainers() {
        DataWrapper dataWrapper = new DataWrapper();
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        List<Trainer> trainers = Arrays.asList(trainer1, trainer2);

        dataWrapper.setTrainers(trainers);

        assertEquals(trainers, dataWrapper.getTrainers());
    }

    @Test
    void testDataWrapper_SetAndGetTrainings() {
        DataWrapper dataWrapper = new DataWrapper();
        Training training1 = new Training();
        Training training2 = new Training();
        List<Training> trainings = Arrays.asList(training1, training2);

        dataWrapper.setTrainings(trainings);

        assertEquals(trainings, dataWrapper.getTrainings());
    }
}
