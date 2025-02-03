package org.gymCrm.hibernate.dto.trainee;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTraineeTrainersDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidUpdateTraineeTrainersDTO() {
        UpdateTraineeTrainersDTO dto = new UpdateTraineeTrainersDTO(List.of("trainer1", "trainer2"));
        dto.setTraineeUsername("traineeUser");

        Set<ConstraintViolation<UpdateTraineeTrainersDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "There should be no validation violations");
    }


    @Test
    void testTraineeUsernameNotBlank() {
        UpdateTraineeTrainersDTO dto = new UpdateTraineeTrainersDTO(List.of("trainer1", "trainer2"));
        dto.setTraineeUsername("");

        Set<ConstraintViolation<UpdateTraineeTrainersDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail due to empty traineeUsername");
        assertEquals(1, violations.size(), "There should be one validation violation");
        assertEquals("Trainee username is required", violations.iterator().next().getMessage());
    }

    @Test
    void testTrainerUsernamesNotEmpty() {
        UpdateTraineeTrainersDTO dto = new UpdateTraineeTrainersDTO(new ArrayList<>());  // Empty trainer list
        dto.setTraineeUsername("traineeUser");

        Set<ConstraintViolation<UpdateTraineeTrainersDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail due to empty trainerUsernames list");
        assertEquals(1, violations.size(), "There should be one validation violation");
        assertEquals("Trainer list cannot be empty", violations.iterator().next().getMessage());
    }
}