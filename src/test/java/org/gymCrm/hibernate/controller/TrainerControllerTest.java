package org.gymCrm.hibernate.controller;

import org.gymCrm.hibernate.dto.trainer.ActiveTrainerDTO;
import org.gymCrm.hibernate.dto.trainer.TrainerProfileDTO;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.gymCrm.hibernate.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerServiceImpl trainerServiceImpl;


    @Mock
    private UserDetailsService<Trainer> userDetailsService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trainer = new Trainer();
        trainer.setUsername("john.doe");
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setActive(true);
    }
    @Test
    void testRegisterTrainerSuccess() {


    }

    @Test
    void testGetTrainerProfile() {

        String username = "john.doe";
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(trainerServiceImpl.selectByUsername(username)).thenReturn(Optional.of(trainer));

        ResponseEntity<TrainerProfileDTO> response = trainerController.getTrainerProfile(username, "authUser", "authPass");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("john.doe", response.getBody().getUsername());
    }

    @Test
    void testUpdateTrainerProfile() {

    }
    @Test
    void testChangeTrainerStatus() {
        String username = "Lil.Adamyan";
        trainer.setActive(true);

        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(trainerServiceImpl.selectByUsername(username)).thenReturn(Optional.of(trainer));

        ResponseEntity<Void> response = trainerController.changeTrainerStatus(username, "authPass", false, "authUser", "authPass");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trainerServiceImpl).changeTrainerActiveStatus(username, "authPass", false);
        assertTrue(trainer.isActive());
    }

    @Test
    void testGetUnassignedActiveTrainers() {
        String traineeUsername = "Lil.Adamyan";
        List<Long> assignedTrainerIds = List.of(1L, 2L);

        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);

        Trainer trainer1 = new Trainer();
        trainer1.setUsername("trainer3");
        trainer1.setFirstName("Alice");
        trainer1.setLastName("Johnson");

        Trainer trainer2 = new Trainer();
        trainer2.setUsername("trainer4");
        trainer2.setFirstName("Bob");
        trainer2.setLastName("Williams");

        List<Trainer> unassignedTrainers = List.of(trainer1, trainer2);
        when(trainerServiceImpl.getUnassignedTrainers(eq(traineeUsername),  anyString(), anyString()))
                .thenReturn(Optional.of(unassignedTrainers));

        ResponseEntity<List<ActiveTrainerDTO>> response = trainerController.getUnassignedActiveTrainers(traineeUsername, "authUser", "authPassword");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ActiveTrainerDTO> trainerDTOs = response.getBody();

        assertNotNull(trainerDTOs);
        assertFalse(trainerDTOs.isEmpty());
        assertEquals(2, trainerDTOs.size());
        assertEquals("trainer3", trainerDTOs.get(0).getUsername());
    }
}