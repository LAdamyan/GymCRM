package org.gymCrm.hibernate.dto.trainer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTrainerDTOTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidDTO() {
        UpdateTrainerDTO dto = new UpdateTrainerDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setActive(true);

        Set<ConstraintViolation<UpdateTrainerDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    public void testInvalidDTO_FirstNameMissing() {
        UpdateTrainerDTO dto = new UpdateTrainerDTO();
        dto.setLastName("Doe");
        dto.setActive(true);

        Set<ConstraintViolation<UpdateTrainerDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size());
        assertEquals("First name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidDTO_LastNameMissing() {
        UpdateTrainerDTO dto = new UpdateTrainerDTO();
        dto.setFirstName("John");
        dto.setActive(true);

        Set<ConstraintViolation<UpdateTrainerDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(1, violations.size());
        assertEquals("Last name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidDTO_AllFieldsMissing() {
        UpdateTrainerDTO dto = new UpdateTrainerDTO();

        Set<ConstraintViolation<UpdateTrainerDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation errors");
        assertEquals(2, violations.size());
    }
}