package org.gymCrm.hibernate.controller;

import org.gymCrm.hibernate.dto.AddTrainingDTO;
import org.gymCrm.hibernate.dto.TrainingTypeDTO;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTraining_Success() {
        AddTrainingDTO addTrainingDTO = new AddTrainingDTO();
        addTrainingDTO.setTrainingName("Cardio");
        addTrainingDTO.setTrainingDate(new Date());
        addTrainingDTO.setDuration(60);
        addTrainingDTO.setTrainerUsername("trainer1");
        addTrainingDTO.setTraineeUsername("trainee1");

        doNothing().when(trainingService).createTraining(any(Training.class), eq("trainer1"), eq("trainee1"));

        ResponseEntity<?> response = trainingController.addTraining(addTrainingDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(trainingService, times(1)).createTraining(any(Training.class), eq("trainer1"), eq("trainee1"));

    }

    @Test
    void testAddTraining_AccessDenied() {
        AddTrainingDTO addTrainingDTO = new AddTrainingDTO();
        addTrainingDTO.setTrainingName("Cardio");
        addTrainingDTO.setTrainingDate(new Date());
        addTrainingDTO.setDuration(60);
        addTrainingDTO.setTrainerUsername("trainer1");
        addTrainingDTO.setTraineeUsername("trainee1");

        doThrow(new SecurityException("Access denied")).when(trainingService).createTraining(any(Training.class), eq("trainer1"), eq("trainee1"));

        ResponseEntity<?> response = trainingController.addTraining(addTrainingDTO);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Access denied", response.getBody());
    }

    @Test
    void testAddTraining_InternalServerError() {
        AddTrainingDTO addTrainingDTO = new AddTrainingDTO();
        addTrainingDTO.setTrainingName("Cardio");
        addTrainingDTO.setTrainingDate(new Date());
        addTrainingDTO.setDuration(60);
        addTrainingDTO.setTrainerUsername("trainer1");
        addTrainingDTO.setTraineeUsername("trainee1");

        doThrow(new RuntimeException("Unexpected error")).when(trainingService).createTraining(any(Training.class), eq("trainer1"), eq("trainee1"));

        ResponseEntity<?> response = trainingController.addTraining(addTrainingDTO);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to create training", response.getBody());
    }

    @Test
    void testGetTraineeTrainings_Success() {
        String username = "trainee1";
        List<Training> trainings = Arrays.asList(
                new Training("Cardio", new Date(), 60),
                new Training("Strength", new Date(), 90)
        );

        when(trainingService.getTraineeTrainings(eq(username), any(), any(), any(), any())).thenReturn(Optional.of(trainings));

        ResponseEntity<Optional<List<Training>>> response = trainingController.getTraineeTrainings(username, null, null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isPresent());
        assertEquals(2, response.getBody().get().size());
    }

    @Test
    void testGetTraineeTrainings_NotFound() {
        String username = "trainee1";

        when(trainingService.getTraineeTrainings(eq(username), any(), any(), any(), any())).thenReturn(Optional.empty());

        ResponseEntity<Optional<List<Training>>> response = trainingController.getTraineeTrainings(username, null, null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isPresent());
    }
    @Test
    void testGetTrainerTrainings_Success() {
        String username = "trainer1";
        List<Training> trainings = Arrays.asList(
                new Training("Cardio", new Date(), 60),
                new Training("Strength", new Date(), 90)
        );

        when(trainingService.getTrainerTrainings(eq(username), any(), any(), any())).thenReturn(Optional.of(trainings));

        ResponseEntity<Optional<List<Training>>> response = trainingController.getTrainerTrainings(username, null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isPresent());
        assertEquals(2, response.getBody().get().size());
    }

    @Test
    void testGetTrainerTrainings_NotFound() {
        String username = "trainer1";

        when(trainingService.getTrainerTrainings(eq(username), any(), any(), any())).thenReturn(Optional.empty());

        ResponseEntity<Optional<List<Training>>> response = trainingController.getTrainerTrainings(username, null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isPresent());
    }
    @Test
    void testGetTrainingTypes_Success() {
        List<String> trainingTypes = Arrays.asList("Cardio", "Strength", "Yoga");

        when(trainingService.getAllTrainingTypes()).thenReturn(trainingTypes);

        ResponseEntity<List<TrainingTypeDTO>> response = trainingController.getTrainingTypes();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("Cardio", response.getBody().get(0).getTrainingType());
    }


    }