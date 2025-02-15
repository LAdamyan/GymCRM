package org.gymCrm.hibernate.dto.trainer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDTOTest {

    private Validator validator;

    public TrainerDTOTest() {
        // Initialize the validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testValidTrainerDTO() {
        TrainerDTO trainerDTO = new TrainerDTO("john_doe", "John", "Doe", null);

        Set<ConstraintViolation<TrainerDTO>> violations = validator.validate(trainerDTO);

        assertTrue(violations.isEmpty(), "TrainerDTO should be valid");
    }

    @Test
    public void testInvalidFirstName() {
        TrainerDTO trainerDTO = new TrainerDTO("john_doe", "", "Doe", null);

        Set<ConstraintViolation<TrainerDTO>> violations = validator.validate(trainerDTO);

        assertFalse(violations.isEmpty(), "TrainerDTO should have validation errors");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")
                        && v.getMessage().equals("First name is required")));
    }

    @Test
    public void testInvalidLastName() {
        TrainerDTO trainerDTO = new TrainerDTO("john_doe", "John", "", null);

        Set<ConstraintViolation<TrainerDTO>> violations = validator.validate(trainerDTO);

        assertFalse(violations.isEmpty(), "TrainerDTO should have validation errors");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")
                        && v.getMessage().equals("Last name is required")));
    }

    @Test
    public void testMissingUsername() {
        TrainerDTO trainerDTO = new TrainerDTO(null, "John", "Doe", null);

        Set<ConstraintViolation<TrainerDTO>> violations = validator.validate(trainerDTO);

        assertTrue(violations.isEmpty(), "TrainerDTO should be valid with null username since username is not annotated as required");
    }

    @Test
    public void testInvalidSpecialization() {
        TrainerDTO trainerDTO = new TrainerDTO("john_doe", "John", "Doe", null);

        Set<ConstraintViolation<TrainerDTO>> violations = validator.validate(trainerDTO);

        // No constraint violation for null specialization if it's not explicitly required, assuming no `@NotNull` annotation
        assertTrue(violations.isEmpty(), "TrainerDTO should be valid with null specialization");
    }
}