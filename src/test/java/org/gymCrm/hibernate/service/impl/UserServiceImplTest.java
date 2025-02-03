package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl<Trainee> userService;

    @Mock
    private UserRepository<Trainee> userRepository;

    private Trainee testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new Trainee();
        testUser.setUsername("john.doe");
        testUser.setPassword("password1");
        testUser.setActive(true);
    }


    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        Optional<Trainee>foundUser = userService.findByUsername(testUser.getUsername());

        assertTrue(foundUser.isPresent());
        assertEquals("john.doe",foundUser.get().getUsername());

    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        Optional<Trainee>foundUser = userService.findByUsername(testUser.getUsername());

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testChangePassword() {
        String newPassword = "newPasword123";
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(Trainee.class))).thenReturn(testUser);

        Optional<Trainee>updatedUser = userService.changePassword(testUser.getUsername(),newPassword);

        assertTrue(updatedUser.isPresent());
        assertEquals(newPassword, updatedUser.get().getPassword());
        verify(userRepository).save(testUser);
    }

    @Test
    void testChangePasswordUserNotFound() {

        String newPassword = "newPassword123";
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        Optional<Trainee> updatedUser = userService.changePassword(testUser.getUsername(), newPassword);

        assertFalse(updatedUser.isPresent());
        verify(userRepository, never()).save(any());
    }
}