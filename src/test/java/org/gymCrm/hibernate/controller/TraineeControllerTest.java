package org.gymCrm.hibernate.controller;

import org.gymCrm.hibernate.dto.TraineeDTO;
import org.gymCrm.hibernate.dto.TraineeProfileDTO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.service.TraineeService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class TraineeControllerTest {

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private TraineeService traineeService;

    @Mock
    private UserDetailsService<Trainee> userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetailsService.authenticate("authUser", "authPass")).thenReturn(true);
    }

    @Test
    void testRegisterTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");

        Mockito.doAnswer(invocation -> {
            Trainee arg = invocation.getArgument(0);
            arg.setUsername("jdoe");
            arg.setPassword("secure123");
            return null;
        }).when(traineeService).saveTrainee(any(Trainee.class));

        ResponseEntity<Map<String, String>> response = traineeController.registerTrainee(traineeDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jdoe", response.getBody().get("username"));
        assertEquals("secure123", response.getBody().get("password"));

    }

    @Test
    void testGetTraineeProfile_Success() {
        String username = "jdoe";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.of(trainee));

        ResponseEntity<TraineeProfileDTO> response = traineeController.getTraineeProfile(
                username, "authUser", "authPass");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    @Test
    void testGetTraineeProfile_NotFound() {
        String username = "jdoe";

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> traineeController.getTraineeProfile(
                username, "authUser", "authPass"));

    }

    @Test
    void testDeleteTrainee_Success() {
        String username = "jdoe";

        Mockito.doNothing().when(traineeService).deleteTrainee(username);

        ResponseEntity<Void> response = traineeController.deleteTrainee(username, "authUser", "authPass");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteTrainee_Unauthorized() {
        String username = "jdoe";

        when(userDetailsService.authenticate("authUser", "wrongPass")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> traineeController.deleteTrainee(
                username, "authUser", "wrongPass"));
    }

    @Test
    void testChangeTraineeStatus() {
        String username = "jdoe";
        boolean isActive = true;

        Mockito.doNothing().when(traineeService).changeTraineeActiveStatus(username, "secure123", isActive);

        ResponseEntity<Void> response = traineeController.changeTraineeStatus(
                username, "secure123", isActive, "authUser", "authPass");

        assertEquals(200, response.getStatusCodeValue());
    }


}