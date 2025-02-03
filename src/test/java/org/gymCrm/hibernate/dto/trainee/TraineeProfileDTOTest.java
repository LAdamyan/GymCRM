package org.gymCrm.hibernate.dto.trainee;

import org.gymCrm.hibernate.dto.address.AddressDTO;
import org.gymCrm.hibernate.dto.trainer.TrainerSummaryDTO;
import org.gymCrm.hibernate.model.Trainee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TraineeProfileDTOTest {

//    @Test
//    void testTraineeProfileDTOConstructorWithTrainee() {
//        // Arrange
//        Trainee trainee = new Trainee("username123", "John", "Doe", LocalDate.of(2000, 1, 1));
//
//        // Act
//        TraineeProfileDTO traineeProfileDTO = new TraineeProfileDTO(trainee);
//
//        // Assert
//        assertEquals(trainee.getUsername(), traineeProfileDTO.getUsername(), "Username should match");
//        assertEquals(trainee.getFirstName(), traineeProfileDTO.getFirstName(), "First name should match");
//        assertEquals(trainee.getLastName(), traineeProfileDTO.getLastName(), "Last name should match");
//        assertNull(traineeProfileDTO.getBirthDate(), "BirthDate should be null as it's not initialized in the constructor");
//        assertNull(traineeProfileDTO.getAddress(), "Address should be null");
//        assertFalse(traineeProfileDTO.isActive(), "isActive should be false by default");
//        assertTrue(traineeProfileDTO.getTrainers().isEmpty(), "Trainers list should be empty by default");
//    }

    @Test
    void testTraineeProfileDTOConstructorWithFullFields() {
        // Arrange
        AddressDTO address = new AddressDTO("123 Street", "City", "State", "12345");
        TrainerSummaryDTO trainer = new TrainerSummaryDTO("trainer1");
        TraineeProfileDTO traineeProfileDTO = new TraineeProfileDTO("username123", "John", "Doe",
                LocalDate.of(2000, 1, 1), address, true, Collections.singletonList(trainer));

        // Act and Assert
        assertEquals("username123", traineeProfileDTO.getUsername());
        assertEquals("John", traineeProfileDTO.getFirstName());
        assertEquals("Doe", traineeProfileDTO.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), traineeProfileDTO.getBirthDate());
        assertEquals(address, traineeProfileDTO.getAddress());
        assertTrue(traineeProfileDTO.isActive());
        assertEquals(1, traineeProfileDTO.getTrainers().size());
        assertEquals("trainer1", traineeProfileDTO.getTrainers().get(0).getUsername());
    }

    @Test
    void testTraineeProfileDTOEmptyConstructor() {
        // Act
        TraineeProfileDTO traineeProfileDTO = new TraineeProfileDTO();

        // Assert
        assertNull(traineeProfileDTO.getUsername(), "Username should be null");
        assertNull(traineeProfileDTO.getFirstName(), "First name should be null");
        assertNull(traineeProfileDTO.getLastName(), "Last name should be null");
        assertNull(traineeProfileDTO.getBirthDate(), "Birth date should be null");
        assertNull(traineeProfileDTO.getAddress(), "Address should be null");
        assertFalse(traineeProfileDTO.isActive(), "isActive should default to false");
        assertTrue(traineeProfileDTO.getTrainers().isEmpty(), "Trainers list should be empty");
    }
}