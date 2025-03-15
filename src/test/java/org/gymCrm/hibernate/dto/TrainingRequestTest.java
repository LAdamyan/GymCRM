package org.gymCrm.hibernate.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrainingRequestTest {

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
        assertTrue(request.isActive());
        assertEquals(LocalDate.of(2023, 10, 10), request.getTrainingDate());
        assertEquals(60, request.getTrainingDuration());
    }

    @Test
    void testAllArgsConstructor() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                "trainer1", "John", "Doe", true, LocalDate.of(2023, 10, 10), 60
        );

        assertEquals("trainer1", request.getTrainerUsername());
        assertEquals("John", request.getTrainerFirstName());
        assertEquals("Doe", request.getTrainerLastName());
        assertTrue(request.isActive());
        assertEquals(LocalDate.of(2023, 10, 10), request.getTrainingDate());
        assertEquals(60, request.getTrainingDuration());
    }

    @Test
    void testNoArgsConstructor() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();

        assertNull(request.getTrainerUsername());
        assertNull(request.getTrainerFirstName());
        assertNull(request.getTrainerLastName());
        assertFalse(request.isActive());
        assertNull(request.getTrainingDate());
        assertEquals(0, request.getTrainingDuration());
    }

}