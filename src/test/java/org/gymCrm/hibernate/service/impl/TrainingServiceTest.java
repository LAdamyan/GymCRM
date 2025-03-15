package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.dto.TrainerWorkloadRequest;
import org.gymCrm.hibernate.dto.TrainingRequest;
import org.gymCrm.hibernate.service.TrainerWorkloadClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.Mockito.verify;

class TrainingServiceTest {

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainerWorkloadClient trainerWorkloadClient;

    private TrainingRequest trainingRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingRequest = new TrainingRequest();
        trainingRequest.setTrainerUsername("trainer1");
        trainingRequest.setTrainerFirstName("John");
        trainingRequest.setTrainerLastName("Doe");
        trainingRequest.setActive(true);
        trainingRequest.setTrainingDuration(60);
    }

    @Test
    void testAddTraining() {
        trainingService.addTraining(trainingRequest);

        verify(trainerWorkloadClient).updateTrainerWorkload(new TrainerWorkloadRequest(
                trainingRequest.getTrainerUsername(),
                trainingRequest.getTrainerFirstName(),
                trainingRequest.getTrainerLastName(),
                trainingRequest.isActive(),
                trainingRequest.getTrainingDate(),
                trainingRequest.getTrainingDuration()
        ));
    }

    @Test
    void testDeleteTraining() {
        trainingService.deleteTraining(trainingRequest);

        verify(trainerWorkloadClient).updateTrainerWorkload(new TrainerWorkloadRequest(
                trainingRequest.getTrainerUsername(),
                trainingRequest.getTrainerFirstName(),
                trainingRequest.getTrainerLastName(),
                trainingRequest.isActive(),
                trainingRequest.getTrainingDate(),
                trainingRequest.getTrainingDuration()
        ));
    }
}