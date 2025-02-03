package org.gymCrm.hibernate.dto.trainee;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.gymCrm.hibernate.dto.address.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTraineeDTOTest {

    private  Validator validator;

    @BeforeEach
    void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void testValidTraineeDTO() {
        // Arrange
        AddressDTO address = new AddressDTO("123 Main St", "City", "State", "12345");
        UpdateTraineeDTO traineeDTO = new UpdateTraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        traineeDTO.setAddress(address);
        traineeDTO.setActive(true);

        // Act
        Set<ConstraintViolation<UpdateTraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }
    @Test
    void testInvalidTraineeDTO_missingFirstName() {
        // Arrange
        UpdateTraineeDTO traineeDTO = new UpdateTraineeDTO();
        traineeDTO.setLastName("Doe");
        traineeDTO.setBirthDate(LocalDate.of(2000, 1, 1));

        // Act
        Set<ConstraintViolation<UpdateTraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertFalse(violations.isEmpty(), "Validation should fail due to missing first name");
        assertEquals(1, violations.size(), "There should be exactly one validation error");
        assertEquals("First name is required", violations.iterator().next().getMessage(), "Error message should be for missing first name");
    }

    @Test
    void testInvalidTraineeDTO_missingLastName() {
        // Arrange
        UpdateTraineeDTO traineeDTO = new UpdateTraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setBirthDate(LocalDate.of(2000, 1, 1));

        // Act
        Set<ConstraintViolation<UpdateTraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertFalse(violations.isEmpty(), "Validation should fail due to missing last name");
        assertEquals(1, violations.size(), "There should be exactly one validation error");
        assertEquals("Last name is required", violations.iterator().next().getMessage(), "Error message should be for missing last name");
    }

    @Test
    void testInvalidTraineeDTO_missingFirstNameAndLastName() {
        // Arrange
        UpdateTraineeDTO traineeDTO = new UpdateTraineeDTO();

        // Act
        Set<ConstraintViolation<UpdateTraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertFalse(violations.isEmpty(), "Validation should fail due to missing first name and last name");
        assertEquals(2, violations.size(), "There should be two validation errors");
    }

    @Test
    void testTraineeDTO_withInvalidAddress() {
        // Arrange
        AddressDTO address = new AddressDTO("", "", "", "");
        UpdateTraineeDTO traineeDTO = new UpdateTraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        traineeDTO.setAddress(address);
        traineeDTO.setActive(true);

        // Act
        Set<ConstraintViolation<UpdateTraineeDTO>> violations = validator.validate(traineeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "Validation should fail due to invalid address");
    }

}