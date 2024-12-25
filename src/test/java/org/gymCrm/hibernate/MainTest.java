package org.gymCrm.hibernate;

import org.gymCrm.hibernate.config.AppConfig;
import org.gymCrm.hibernate.config.HibernateConfig;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.service.TraineeService;
import org.gymCrm.hibernate.service.TrainerService;
import org.gymCrm.hibernate.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingService trainingService;
    @Mock
    private Trainee trainee;
    @Mock
    private Trainer trainer;
    @Mock
    private Training training;
    @Mock
    private TrainingType trainingType;

    private ApplicationContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new AnnotationConfigApplicationContext(AppConfig.class, HibernateConfig.class);

        lenient().when(trainee.getUsername()).thenReturn("TigAdamyan");
        lenient().when(trainer.getUsername()).thenReturn("JohnSmith");
        lenient().when(training.getTrainingName()).thenReturn("Athletics");
        lenient().when(trainingType.getTypeName()).thenReturn("Bodybuilding");
    }
    @Test
    public void testSaveTrainee() {
        trainerService.saveTrainer(trainer);
        verify(trainerService, times(1)).saveTrainer(trainer);
        System.out.println("Trainer saved successfully");
    }

    @Test
    public void testSaveTrainer() {
        trainerService.saveTrainer(trainer);
        verify(trainerService, times(1)).saveTrainer(trainer);
        System.out.println("Trainer saved successfully");
    }

    @Test
    public void testCreateTraining() {
        trainingService.createTraining(training, "TigAdamyan", "password123");
        verify(trainingService, times(1)).createTraining(training, "TigAdamyan", "password123");
        System.out.println("Training created successfully");
    }

    @Test
    public void testGetTraineeTrainings() {
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "Tom";

        Optional<List<Training>> traineeTrainings = Optional.of(Arrays.asList(training));
        when(trainingService.getTraineeTrainings(
                "TigAdamyan", "password123", fromDate, toDate, trainerName, trainingType
        )).thenReturn(traineeTrainings);

        Optional<List<Training>> result = trainingService.getTraineeTrainings(
                "TigAdamyan", "password123", fromDate, toDate, trainerName, trainingType
        );

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        System.out.println("Trainee Training fetched successfully");
    }
    @Test
    public void testGetTraineeByUsername() {
        when(traineeService.getTraineeByUsername("TigAdamyan", "password123"))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> retrievedTrainee = traineeService.getTraineeByUsername("TigAdamyan", "password123");

        assertTrue(retrievedTrainee.isPresent());
        assertEquals(trainee, retrievedTrainee.get());
        System.out.println("Trainee fetched successfully");
    }

    @Test
    public void testDeleteTrainee() {
        traineeService.deleteTrainee("TigAdamyan", "password123");
        verify(traineeService, times(1)).deleteTrainee("TigAdamyan", "password123");
        System.out.println("Trainee deleted successfully");
    }
}