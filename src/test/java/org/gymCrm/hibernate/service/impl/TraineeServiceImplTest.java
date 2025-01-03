package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.dao.TraineeDAO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {


    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UserDetailsService<Trainee> userDetailsService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp(){
        trainee = new Trainee();
        trainee.setUsername("lil.adam");
        trainee.setPassword("password");
    }

    @Test
    void saveTrainee_Success(){
        traineeService.saveTrainee(trainee);

        verify(traineeDAO,times(1)).create(trainee);
    }

    @Test
    void getAllTrainees_AuthenticationSuccess(){
        when(userDetailsService.authenticate(anyString(),anyString())).thenReturn(true);
        when(traineeDAO.listAll()).thenReturn(Optional.of(List.of(trainee)));

        Optional<List<Trainee>> result = traineeService.getAllTrainees("Lil","1234");

        assertTrue(result.isPresent());
        assertEquals(1,result.get().size());
        assertEquals(trainee, result.get().get(0));
    }

    @Test
    void getAllTrainees_AuthenticationFailure(){
        when(userDetailsService.authenticate(anyString(),anyString())).thenReturn(false);

        Optional<List<Trainee>> result = traineeService.getAllTrainees("admin", "wrongPassword");

        assertTrue(result.isEmpty());
        verify(userDetailsService, times(1)).authenticate("admin", "wrongPassword");
    }
    @Test
    void getTraineeByUsername_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(),anyString())).thenReturn(true);
        when(traineeDAO.selectByUsername("lil.adam")).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.getTraineeByUsername("lil.adam", "password123");

        assertTrue(result.isPresent());
        assertEquals("lil.adam", result.get().getUsername());
        verify(userDetailsService, times(1)).authenticate("lil.adam", "password123");
        verify(traineeDAO, times(1)).selectByUsername("lil.adam");
    }
    @Test
    void getTraineeByUsername_AuthenticationFailure() {
        when(userDetailsService.authenticate(anyString(),anyString())).thenReturn(false);

        assertThrows(SecurityException.class, () ->
                traineeService.getTraineeByUsername("john.doe", "wrongPassword")
        );

        verify(userDetailsService, times(1)).authenticate("john.doe", "wrongPassword");
        verifyNoInteractions(traineeDAO);
    }

    @Test
    void updateTrainee_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(),anyString())).thenReturn(true);

        traineeService.updateTrainee(trainee,"lil.adam","7777");

        verify(userDetailsService, times(1)).authenticate("lil.adam","7777");
        verify(traineeDAO, times(1)).update(trainee);
    }

    @Test
    void deleteTrainee_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(),anyString())).thenReturn(true);

        traineeService.deleteTrainee("lil.adam","7777");

        verify(userDetailsService, times(1)).authenticate("lil.adam","7777");
        verify(traineeDAO,times(1)).delete("lil.adam");
    }
    @Test
    void changeTraineesPassword_AuthenticationSuccess() {
        when(userDetailsService.authenticate(anyString(),anyString())).thenReturn(true);

        traineeService.changeTraineesPassword("lil.adam","oldPassword","newPassword");

        verify(userDetailsService, times(1)).authenticate("lil.adam","oldPassword");
        verify(traineeDAO, times(1)).changeTraineesPassword("lil.adam","newPassword");

    }
    @Test
    public void testChangeTraineeActiveStatus_TraineeFound_ShouldToggleStatus() {
        String username = "testUser";
        String password = "testPass";
        Trainee trainee = new Trainee();
        trainee.setActive(true);

        when(userDetailsService.authenticate(username, password)).thenReturn(true);
        when(traineeDAO.selectByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.changeTraineeActiveStatus(username, password);

        assertEquals(true, trainee.isActive());
        verify(traineeDAO, times(1)).changeTraineeActiveStatus(username);

    }
    @Test
    void testChangeTraineeActivation_FailedAuthenticationThrowsException() {
        String username = "testUser";
        String password = "testPass";

        when(userDetailsService.authenticate(username, password)).thenReturn(false);

        Exception exception = assertThrows(SecurityException.class, () -> {
            traineeService.changeTraineeActiveStatus(username, password);
        });

        assertEquals("User testUser not authenticated, permission denied!", exception.getMessage());
        verify(traineeDAO, never()).changeTraineeActiveStatus(username);
    }
}