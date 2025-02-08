//package org.gymCrm.hibernate.controller;
//
//import org.gymCrm.hibernate.dto.trainee.TraineeProfileDTO;
//import org.gymCrm.hibernate.dto.trainee.UpdateTraineeTrainersDTO;
//import org.gymCrm.hibernate.dto.trainer.TrainerDTO;
//import org.gymCrm.hibernate.model.Trainee;
//import org.gymCrm.hibernate.model.Trainer;
//import org.gymCrm.hibernate.repo.TrainerRepository;
//import org.gymCrm.hibernate.util.AuthenticationService;
//import org.gymCrm.hibernate.service.impl.TraineeServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@WebMvcTest(TraineeController.class)
//class TraineeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private TraineeController traineeController;
//
//    @MockBean
//    private TraineeServiceImpl traineeServiceImpl;
//
//    @MockBean
//    private TrainerRepository trainerRepository;
//
//
//    @MockBean
//    private AuthenticationService authenticationService;
//
//    private Trainee trainee;
//
//
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        trainee = new Trainee();
//        trainee.setFirstName("Lil");
//        trainee.setLastName("Adamyan");
//        trainee.setUsername("Lil.Adamyan");
//        trainee.setPassword("securePassword");
//        trainee.setActive(true);
//    }
//
//    @Test
//    void testGetTraineeProfile() {
//        String username = "Lil.Adamyan";
//        when(authenticationService.authenticate(anyString(), anyString())).thenReturn(true);
//        when(traineeServiceImpl.selectByUsername(username)).thenReturn(Optional.of(trainee));
//
//        ResponseEntity<TraineeProfileDTO> response = traineeController.getTraineeProfile(username);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(username, response.getBody().getUsername());
//    }
//
//    @Test
//    void testDeleteTrainee() {
//        String username = "Lil.Adamyan";
//        when(authenticationService.authenticate(anyString(), anyString())).thenReturn(true);
//        when(traineeServiceImpl.selectByUsername(username)).thenReturn(Optional.of(trainee));
//
//        ResponseEntity<Void> response = traineeController.deleteTrainee(username);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(traineeServiceImpl).delete(username);
//    }
//
//    @Test
//    void testChangeTraineeActiveStatus() {
//        String username = "Lil.Adamyan";
//        trainee.setActive(true);
//        when(authenticationService.authenticate(anyString(), anyString())).thenReturn(true);
//        when(traineeServiceImpl.selectByUsername(username)).thenReturn(Optional.of(trainee));
//
//        ResponseEntity<Void> response = traineeController.changeTraineeStatus(username, "authPass", false);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(traineeServiceImpl).changeTraineeActiveStatus(username, "authPass", false);
//
//    }
//
//    @Test
//    void testUpdateTraineeTrainers() {
//        String traineeUsername = "Lil.Adamyan";
//        List<String> trainerUsernames = List.of("trainer1", "trainer2");
//
//        when(authenticationService.authenticate(anyString(), anyString())).thenReturn(true);
//        when(traineeServiceImpl.selectByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
//
//        when(trainerRepository.findByUsername("trainer1")).thenReturn(Optional.of(new Trainer()));
//        when(trainerRepository.findByUsername("trainer2")).thenReturn(Optional.of(new Trainer()));
//
//        UpdateTraineeTrainersDTO requestDTO = new UpdateTraineeTrainersDTO(trainerUsernames);
//
//        ResponseEntity<List<TrainerDTO>> response = traineeController.updateTraineeTrainers(traineeUsername, requestDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(traineeServiceImpl).updateTraineeTrainers(eq(traineeUsername), anyList());
//    }
//
//}