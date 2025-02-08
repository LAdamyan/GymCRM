package org.gymCrm.hibernate.dto.trainee;

import org.gymCrm.hibernate.dto.trainer.TrainerProfileDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TraineeSummaryDTOTest {

    @Test
    void testConstructorFromTrainerProfileDTO() {
        TrainerProfileDTO trainerProfileDTO = new TrainerProfileDTO();
        trainerProfileDTO.setUsername("trainer123");
        trainerProfileDTO.setFirstName("John");
        trainerProfileDTO.setLastName("Doe");

        TraineeSummaryDTO traineeSummaryDTO = new TraineeSummaryDTO(trainerProfileDTO);

        assertEquals(trainerProfileDTO.getUsername(), traineeSummaryDTO.getUsername(), "Username should match");
        assertEquals(trainerProfileDTO.getFirstName(), traineeSummaryDTO.getFirstName(), "First name should match");
        assertEquals(trainerProfileDTO.getLastName(), traineeSummaryDTO.getLastName(), "Last name should match");
    }

    @Test
    void testEmptyConstructor() {
        // Act
        TraineeSummaryDTO traineeSummaryDTO = new TraineeSummaryDTO();

        // Assert
        assertNull(traineeSummaryDTO.getUsername(), "Username should be null");
        assertNull(traineeSummaryDTO.getFirstName(), "First name should be null");
        assertNull(traineeSummaryDTO.getLastName(), "Last name should be null");
    }
}
