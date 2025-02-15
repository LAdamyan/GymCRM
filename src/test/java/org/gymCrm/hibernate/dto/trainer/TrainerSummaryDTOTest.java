package org.gymCrm.hibernate.dto.trainer;

import org.gymCrm.hibernate.model.TrainingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainerSummaryDTOTest {
    @Test
    public void testConstructorWithAllArgs() {
        // Arrange
        String username = "trainer1";
        String firstName = "John";
        String lastName = "Doe";
        TrainingType specialization = new TrainingType("BodyBuilding") ;// Assuming a valid enum value

        // Act
        TrainerSummaryDTO trainerSummaryDTO = new TrainerSummaryDTO(username, firstName, lastName, specialization);

        // Assert
        assertNotNull(trainerSummaryDTO, "TrainerSummaryDTO should not be null");
        assertEquals(username, trainerSummaryDTO.getUsername(), "Username should match");
        assertEquals(firstName, trainerSummaryDTO.getFirstName(), "First name should match");
        assertEquals(lastName, trainerSummaryDTO.getLastName(), "Last name should match");
        assertEquals(specialization, trainerSummaryDTO.getSpecialization(), "Specialization should match");
    }

    @Test
    public void testConstructorWithSingleArg() {
        // Arrange
        String trainer1 = "trainer1";

        // Act
        TrainerSummaryDTO trainerSummaryDTO = new TrainerSummaryDTO(trainer1);

        // Assert
        assertNotNull(trainerSummaryDTO, "TrainerSummaryDTO should not be null");
        // Since no initialization occurs for the other fields, they should be null or empty
        assertNull(trainerSummaryDTO.getUsername(), "Username should be null");
        assertNull(trainerSummaryDTO.getFirstName(), "First name should be null");
        assertNull(trainerSummaryDTO.getLastName(), "Last name should be null");
        assertNull(trainerSummaryDTO.getSpecialization(), "Specialization should be null");
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        TrainerSummaryDTO trainerSummaryDTO = new TrainerSummaryDTO();

        // Act
        trainerSummaryDTO.setUsername("trainer1");
        trainerSummaryDTO.setFirstName("John");
        trainerSummaryDTO.setLastName("Doe");
        TrainingType specialization = new TrainingType("Yoga") ;

        // Assert
        assertEquals("trainer1", trainerSummaryDTO.getUsername(), "Username should be set correctly");
        assertEquals("John", trainerSummaryDTO.getFirstName(), "First name should be set correctly");
        assertEquals("Doe", trainerSummaryDTO.getLastName(), "Last name should be set correctly");
        assertEquals("Yoga",  specialization.getTypeName(), "Specialization should be set correctly");
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        TrainerSummaryDTO trainer1 = new TrainerSummaryDTO("trainer1", "John", "Doe",new TrainingType("Yoga"));
        TrainerSummaryDTO trainer2 = new TrainerSummaryDTO("trainer1", "John", "Doe", new TrainingType("Yoga"));

        // Assert equals method
        assertEquals(trainer1, trainer2, "TrainerSummaryDTO objects should be equal");

        // Assert hashCode method
        assertEquals(trainer1.hashCode(), trainer2.hashCode(), "Hashcodes should be equal");
    }

    @Test
    public void testToString() {
        // Arrange
        TrainerSummaryDTO trainerSummaryDTO = new TrainerSummaryDTO("trainer1", "John", "Doe", null);

        // Act
        String expectedString = "TrainerSummaryDTO(username=trainer1, firstName=John, lastName=Doe, specialization=null)";
        String actualString = trainerSummaryDTO.toString();

        // Assert
        assertEquals(expectedString, actualString, "toString method should return the expected string representation");
    }
}