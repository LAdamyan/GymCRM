package org.gymCrm.hibernate.controller;

import org.gymCrm.hibernate.dto.trainer.TrainerDTO;
import org.gymCrm.hibernate.dto.trainer.TrainerProfileDTO;
import org.gymCrm.hibernate.dto.trainer.UpdateTrainerDTO;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.service.TrainerService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserDetailsService<Trainer> userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetailsService.authenticate("authUser", "authPass")).thenReturn(true);
    }

    @Test
    void testRegisterTrainer_Success() {
        TrainerDTO trainerDTO = new TrainerDTO();
        trainerDTO.setFirstName("John");
        trainerDTO.setLastName("Doe");

        Trainer savedTrainer = new Trainer();
        savedTrainer.setUsername("jdoe");
        savedTrainer.setPassword("secure123");

        Mockito.doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setUsername("jdoe");
            trainer.setPassword("secure123");
            return null;
        }).when(trainerService).saveTrainer(any(Trainer.class));

        ResponseEntity<Map<String, String>> response = trainerController.registerTrainer(trainerDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("jdoe", (response.getBody()).get("username"));
        assertEquals("secure123", (response.getBody()).get("password"));

    }

    @Test
    void testGetTrainerProfile_Success() {
        String username = "jdoe";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.of(trainer));

        ResponseEntity<TrainerProfileDTO> response = trainerController.getTrainerProfile(
                username, "authUser", "authPass");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    @Test
    void testGetTrainerProfile_NotFound() {
        String username = "unknown";

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<TrainerProfileDTO> response = trainerController.getTrainerProfile(
                username, "authUser", "authPass");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateTrainerProfile_Success() {
        String username = "jdoe";
        UpdateTrainerDTO updateDTO = new UpdateTrainerDTO();
        updateDTO.setFirstName("UpdatedJohn");
        updateDTO.setLastName("UpdatedDoe");

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setUsername(username);
        updatedTrainer.setFirstName("UpdatedJohn");
        updatedTrainer.setLastName("UpdatedDoe");

        when(trainerService.updateTrainer(username, updateDTO)).thenReturn(Optional.of(updatedTrainer));

        ResponseEntity<TrainerProfileDTO> response = trainerController.updateTrainerProfile(
                username, updateDTO, "authUser", "authPass");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("UpdatedJohn", response.getBody().getFirstName());
        assertEquals("UpdatedDoe", response.getBody().getLastName());
    }

    @Test
    void testUpdateTrainerProfile_NotFound() {
        String username = "unknown";
        UpdateTrainerDTO updateDTO = new UpdateTrainerDTO();

        when(trainerService.updateTrainer(username, updateDTO)).thenReturn(Optional.empty());

        ResponseEntity<TrainerProfileDTO> response = trainerController.updateTrainerProfile(
                username, updateDTO, "authUser", "authPass");

        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    void testChangeTrainerStatus_Success() {
        String username = "jdoe";

        doNothing().when(trainerService).changeTrainerActiveStatus(username, "secure123", true);

        ResponseEntity<Void> response = trainerController.changeTrainerStatus(
                username, "secure123", true, "authUser", "authPass");

        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    void testChangeTrainerStatus_AuthenticationFailed() {
        String username = "jdoe";

        when(userDetailsService.authenticate("authUser", "authPass")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () ->
                trainerController.changeTrainerStatus(
                        username, "secure123", true, "authUser", "authPass"));

    }
}