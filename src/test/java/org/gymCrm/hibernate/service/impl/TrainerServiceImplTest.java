package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.repo.TrainerRepository;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.geo.CustomMetric;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserCredentialsUtil userCredentialsUtil;

    @Mock
    private UserDetailsService<Trainer> userDetailsService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainer = new Trainer();
        trainer.setId(1);
        trainer.setFirstName("Alice");
        trainer.setLastName("Smith");
        trainer.setUsername("alice_smith");
        trainer.setPassword("password123");
        trainer.setActive(true);
        trainer.setSpecialization(new TrainingType("Yoga"));
    }


    @Test
    void testCreateTrainer() {
        try (MockedStatic<UserCredentialsUtil> mockedStatic = Mockito.mockStatic(UserCredentialsUtil.class)) {
            mockedStatic.when(() -> UserCredentialsUtil.generateUsername(anyString(), anyString()))
                    .thenReturn("mocked_username");
            when(userCredentialsUtil.generatePassword()).thenReturn("generatedPassword");
            when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

            trainerService.create(trainer);

            assertEquals("mocked_username", trainer.getUsername()); // Ensure correct username
            assertEquals("generatedPassword", trainer.getPassword());
            verify(trainerRepository, times(1)).save(any(Trainer.class));

        }
    }

    @Test
    void testUpdateTrainer() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(trainerRepository.existsById(anyInt())).thenReturn(true);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        trainerService.update(trainer, "alice_smith", "password123");

        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testSelectByUsername() {
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.selectByUsername("alice_smith");

        assertTrue(foundTrainer.isPresent());
        assertEquals("alice_smith", foundTrainer.get().getUsername());
    }

    @Test
    void testChangeTrainerPassword() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(trainer));

        trainerService.changeTrainersPassword("alice_smith", "password123", "newPassword");

        assertEquals("newPassword", trainer.getPassword());
        verify(trainerRepository, times(1)).save(trainer);
    }

//    @Test
//    void testChangeTrainerActiveStatus() {
//        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
//        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(trainer));
//
//        trainerService.changeTrainerActiveStatus("alice_smith", "password123", false);
//
//        assertFalse(trainer.getIsActive());
//        verify(trainerRepository, times(1)).save(trainer);
//    }
    @Test
    void testGetUnassignedTrainers() {
        when(userDetailsService.authenticate(anyString(), anyString())).thenReturn(true);
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));

        Optional<List<Trainer>> unassignedTrainers = trainerService.getUnassignedTrainers("trainee1", "admin", "adminpass");

        assertTrue(unassignedTrainers.isPresent());
        assertEquals(1, unassignedTrainers.get().size());
    }
}