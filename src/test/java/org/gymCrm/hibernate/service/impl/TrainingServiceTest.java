package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.dto.TrainingRequest;
import org.gymCrm.hibernate.client.TrainerWorkloadClient;
import org.junit.jupiter.api.BeforeEach;
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


}