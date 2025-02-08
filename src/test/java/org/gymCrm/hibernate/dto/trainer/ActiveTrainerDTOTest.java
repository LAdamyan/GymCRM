package org.gymCrm.hibernate.dto.trainer;

import org.gymCrm.hibernate.model.TrainingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActiveTrainerDTOTest {
    @Test
    public void testActiveTrainerDTOConstructor() {

        String username = "trainer1";
        String firstName = "John";
        String lastName = "Doe";
        TrainingType specialization = new TrainingType();
        specialization.setTypeName("Yoga");

        ActiveTrainerDTO activeTrainerDTO = new ActiveTrainerDTO(username, firstName, lastName, specialization);

        assertEquals(username, activeTrainerDTO.getUsername());
        assertEquals(firstName, activeTrainerDTO.getFirstName());
        assertEquals(lastName, activeTrainerDTO.getLastName());
        assertEquals(specialization, activeTrainerDTO.getSpecialization());
    }

    @Test
    public void testActiveTrainerDTOSettersAndGetters() {
        ActiveTrainerDTO activeTrainerDTO = new ActiveTrainerDTO();

        activeTrainerDTO.setUsername("trainer1");
        activeTrainerDTO.setFirstName("Jane");
        activeTrainerDTO.setLastName("Smith");
        TrainingType specialization = new TrainingType();
        specialization.setTypeName("Strength Training");
        activeTrainerDTO.setSpecialization(specialization);

        assertEquals("trainer1", activeTrainerDTO.getUsername());
        assertEquals("Jane", activeTrainerDTO.getFirstName());
        assertEquals("Smith", activeTrainerDTO.getLastName());
        assertEquals(specialization, activeTrainerDTO.getSpecialization());
    }

    @Test
    public void testActiveTrainerDTOEmptyInitialization() {
        ActiveTrainerDTO activeTrainerDTO = new ActiveTrainerDTO();

        assertNull(activeTrainerDTO.getUsername());
        assertNull(activeTrainerDTO.getFirstName());
        assertNull(activeTrainerDTO.getLastName());
        assertNull(activeTrainerDTO.getSpecialization());
    }
}