package org.gymCrm.hibernate.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrainerWorkloadRequestTest {

    @Test
    void testGettersAndSetters() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setTrainerUsername("trainer1");
        request.setTrainerFirstName("John");
        request.setTrainerLastName("Doe");
        request.setActive(true);
        request.setTrainingDate(LocalDate.of(2023, 10, 10));
        request.setTrainingDuration(60);

        assertEquals("trainer1", request.getTrainerUsername());
        assertEquals("John", request.getTrainerFirstName());
        assertEquals("Doe", request.getTrainerLastName());
        assertEquals(true, request.isActive());
        assertEquals(LocalDate.of(2023, 10, 10), request.getTrainingDate());
        assertEquals(60, request.getTrainingDuration());
    }
}