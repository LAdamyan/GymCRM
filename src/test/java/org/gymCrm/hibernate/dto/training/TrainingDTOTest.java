package org.gymCrm.hibernate.dto.training;

import org.gymCrm.hibernate.model.TrainingType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDTOTest {
    @Test
    public void testTrainingDTOConstructorWithTrainingType() {
        // Create a sample TrainingDTO object using the constructor with TrainingType
        String trainingName = "Java Basics";
        Date trainingDate = new Date();
        TrainingType trainingType = new TrainingType(".ONLINE");
        int duration = 60;
        String traineeName = "John Doe";

        TrainingDTO trainingDTO = new TrainingDTO(trainingName, trainingDate, trainingType, duration, traineeName);

        // Assert that the fields are correctly set
        assertEquals(trainingName, trainingDTO.getTrainingName(), "Training name should match");
        assertEquals(trainingDate, trainingDTO.getTrainingDate(), "Training date should match");
        assertEquals(trainingType.getTypeName(), trainingDTO.getTrainingType(), "Training type should match");
        assertEquals(duration, trainingDTO.getDuration(), "Duration should match");
        assertEquals(traineeName, trainingDTO.getTraineeName(), "Trainee name should match");
    }

    @Test
    public void testTrainingDTOConstructorWithNullTrainingType() {

        String trainingName = "Java Basics";
        Date trainingDate = new Date();
        TrainingType trainingType = null;
        int duration = 60;
        String traineeName = "John Doe";

        TrainingDTO trainingDTO = new TrainingDTO(trainingName, trainingDate, trainingType, duration, traineeName);

        assertNull(trainingDTO.getTrainingType(), "Training type should be null");
    }

    @Test
    public void testTrainingDTONoArgsConstructor() {

        TrainingDTO trainingDTO = new TrainingDTO();

        assertNull(trainingDTO.getTrainingName(), "Training name should be null");
        assertNull(trainingDTO.getTrainingDate(), "Training date should be null");
        assertNull(trainingDTO.getTrainingType(), "Training type should be null");
        assertEquals(0, trainingDTO.getDuration(), "Duration should be 0");
        assertNull(trainingDTO.getTraineeName(), "Trainee name should be null");
    }
}