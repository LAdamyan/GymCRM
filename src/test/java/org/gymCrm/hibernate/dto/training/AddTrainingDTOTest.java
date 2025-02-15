package org.gymCrm.hibernate.dto.training;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AddTrainingDTOTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidAddTrainingDTO() {

        AddTrainingDTO dto = new AddTrainingDTO();
        dto.setTraineeUsername("traineeUser");
        dto.setTrainerUsername("trainerUser");
        dto.setTrainingName("Java Basics");
        dto.setTrainingDate(new Date());
        dto.setDuration(60);

        Set<ConstraintViolation<AddTrainingDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    public void testInvalidAddTrainingDTO_TraineeUsernameMissing() {

        AddTrainingDTO dto = new AddTrainingDTO();
        dto.setTrainerUsername("trainerUser");
        dto.setTrainingName("Java Basics");
        dto.setTrainingDate(new Date());
        dto.setDuration(60);

        Set<ConstraintViolation<AddTrainingDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size());
        assertEquals("Trainee username is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidAddTrainingDTO_TrainerUsernameMissing() {

        AddTrainingDTO dto = new AddTrainingDTO();
        dto.setTraineeUsername("traineeUser");
        dto.setTrainingName("Java Basics");
        dto.setTrainingDate(new Date());
        dto.setDuration(60);

        Set<ConstraintViolation<AddTrainingDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size());
        assertEquals("Trainer username is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidAddTrainingDTO_TrainingNameMissing() {

        AddTrainingDTO dto = new AddTrainingDTO();
        dto.setTraineeUsername("traineeUser");
        dto.setTrainerUsername("trainerUser");
        dto.setTrainingDate(new Date());
        dto.setDuration(60);

        Set<ConstraintViolation<AddTrainingDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size());
        assertEquals("Training name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidAddTrainingDTO_TrainingDateMissing() {

        AddTrainingDTO dto = new AddTrainingDTO();
        dto.setTraineeUsername("traineeUser");
        dto.setTrainerUsername("trainerUser");
        dto.setTrainingName("Java Basics");
        dto.setDuration(60);

        Set<ConstraintViolation<AddTrainingDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size());
        assertEquals("Training date is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidAddTrainingDTO_DurationMissing() {

        AddTrainingDTO dto = new AddTrainingDTO();
        dto.setTraineeUsername("traineeUser");
        dto.setTrainerUsername("trainerUser");
        dto.setTrainingName("Java Basics");
        dto.setTrainingDate(new Date());


        Set<ConstraintViolation<AddTrainingDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size());
        assertEquals("Training duration is required", violations.iterator().next().getMessage());
    }
}