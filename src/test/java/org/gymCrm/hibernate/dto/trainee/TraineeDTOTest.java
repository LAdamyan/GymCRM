package org.gymCrm.hibernate.dto.trainee;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.gymCrm.hibernate.dto.address.AddressDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDTOTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidTraineeDTO() {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        traineeDTO.setAddress(new AddressDTO("123 Street", "City", "State", "12345"));
        traineeDTO.setActive(true);

        // Act
        Set<ConstraintViolation<TraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation violations");
    }

    @Test
    void testFirstNameNotBlank() {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("");  // Empty first name
        traineeDTO.setLastName("Doe");

        // Act
        Set<ConstraintViolation<TraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertFalse(violations.isEmpty(), "Validation should fail due to empty first name");
        assertEquals(1, violations.size(), "There should be one validation violation");
        assertEquals("First name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testLastNameNotBlank() {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("");  // Empty last name

        // Act
        Set<ConstraintViolation<TraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertFalse(violations.isEmpty(), "Validation should fail due to empty last name");
        assertEquals(1, violations.size(), "There should be one validation violation");
        assertEquals("Last name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testBirthDateCanBeNull() {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setBirthDate(null);  // Birth date is optional

        // Act
        Set<ConstraintViolation<TraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation violations for birthDate being null");
    }

    @Test
    void testAddressCanBeNull() {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setAddress(null);  // Address is optional

        // Act
        Set<ConstraintViolation<TraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation violations for address being null");
    }

    @Test
    void testIsActiveDefaultsToFalse() {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");

        // Act
        boolean isActive = traineeDTO.isActive();  // Default should be false

        // Assert
        assertFalse(isActive, "The default value of isActive should be false");
    }
}
