package org.gymCrm.hibernate.dto.training;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TraineeTrainingResponseTest {
    @Test
    public void testTraineeTrainingResponseConstructor() {
        String trainingName = "Yoga Class";
        Date trainingDate = new Date();
        String trainingType = "Yoga";
        int trainingDuration = 60;
        String trainerName = "John Doe";

        TraineeTrainingResponse response = new TraineeTrainingResponse(trainingName, trainingDate, trainingType, trainingDuration, trainerName);

        assertEquals(trainingName, response.getTrainingName());
        assertEquals(trainingDate, response.getTrainingDate());
        assertEquals(trainingType, response.getTrainingType());
        assertEquals(trainingDuration, response.getTrainingDuration());
        assertEquals(trainerName, response.getTrainerName());

    }

    @Test
    public void testTraineeTrainingResponseEquality() {
        TraineeTrainingResponse response1 = new TraineeTrainingResponse("Yoga Class", new Date(), "Yoga", 60, "John Doe");
        TraineeTrainingResponse response2 = new TraineeTrainingResponse("Yoga Class", new Date(), "Yoga", 60, "John Doe");

        assertEquals(response1, response2);
        assertNotSame(response1, response2);
    }

    @Test
    public void testTraineeTrainingResponseHashCode() {
        TraineeTrainingResponse response1 = new TraineeTrainingResponse("Yoga Class", new Date(), "Yoga", 60, "John Doe");
        TraineeTrainingResponse response2 = new TraineeTrainingResponse("Yoga Class", new Date(), "Yoga", 60, "John Doe");

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    public void testTraineeTrainingResponseToString() {
        TraineeTrainingResponse response = new TraineeTrainingResponse("Yoga Class", new Date(), "Yoga", 60, "John Doe");

        String expectedString = "TraineeTrainingResponse(trainingName=Yoga Class, trainingDate=" + response.getTrainingDate() +
                ", trainingType=Yoga, trainingDuration=60, trainerName=John Doe)";

        assertEquals(expectedString, response.toString());

    }
}