package org.gymCrm.hibernate.dto.training;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainingTypeDTOTest {
    @Test
    public void testTrainingTypeDTOConstructorWithArgs() {

        String trainingTypeValue = "Online";
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO(trainingTypeValue);

        assertEquals(trainingTypeValue, trainingTypeDTO.getTrainingType(), "Training type should match");
    }

    @Test
    public void testTrainingTypeDTONoArgsConstructor() {

        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();

        assertNull(trainingTypeDTO.getTrainingType(), "Training type should be null by default");
    }

    @Test
    public void testSetAndGetTrainingType() {

        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();

        String trainingTypeValue = "Offline";
        trainingTypeDTO.setTrainingType(trainingTypeValue);


        assertEquals(trainingTypeValue, trainingTypeDTO.getTrainingType(), "Training type should match the set value");
    }
}
