package org.gymCrm.hibernate.dto.trainee;

import org.gymCrm.hibernate.dto.address.AddressDTO;
import org.gymCrm.hibernate.dto.trainer.TrainerSummaryDTO;
import org.gymCrm.hibernate.model.Trainee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TraineeProfileDTOTest {

    @Test
    void testDefaultConstructor() {
        TraineeProfileDTO profileDTO = new TraineeProfileDTO();
        assertNull(profileDTO.getUsername());
        assertNull(profileDTO.getFirstName());
        assertNull(profileDTO.getLastName());
        assertNull(profileDTO.getBirthDate());
        assertNull(profileDTO.getAddress());
        assertFalse(profileDTO.isActive());
        assertNull(profileDTO.getTrainers());
    }
    @Test
    void testAllArgsConstructor() {
        TraineeProfileDTO profileDTO = new TraineeProfileDTO("lil.adamyan", "Lil", "Adamyan",
                LocalDate.of(2000, 1, 1), new AddressDTO(), true, new ArrayList<>());

        assertEquals("lil.adamyan", profileDTO.getUsername());
        assertEquals("Lil", profileDTO.getFirstName());
        assertEquals("Adamyan", profileDTO.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), profileDTO.getBirthDate());
        assertNotNull(profileDTO.getAddress());
        assertTrue(profileDTO.isActive());
        assertNotNull(profileDTO.getTrainers());
    }
    @Test
    void testConstructorFromTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUsername("lil.adamyan");
        trainee.setFirstName("Lil");
        trainee.setLastName("Adamyan");

        TraineeProfileDTO profileDTO = new TraineeProfileDTO(trainee);

        assertEquals("lil.adamyan", profileDTO.getUsername());
        assertEquals("Lil", profileDTO.getFirstName());
        assertEquals("Adamyan", profileDTO.getLastName());
    }
}