package org.gymCrm.storage;

import org.gymCrm.model.Trainee;
import org.gymCrm.model.Trainer;
import org.gymCrm.model.Training;
import org.gymCrm.service.TraineeService;
import org.gymCrm.service.TrainerService;
import org.gymCrm.service.TrainingService;
import org.gymCrm.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageInitializerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private StorageInitializer storageInitializer;

    private DataWrapper dataWrapper;

    @BeforeEach
    void setUp(){
        Trainee trainee1 = new Trainee();
        trainee1.setUsername("Trainee1");
        Trainee trainee2 = new Trainee();
        trainee2.setUsername("Trainee2");
        List<Trainee> trainees = Arrays.asList(trainee1, trainee2);

        Trainer trainer1 = new Trainer();
        trainer1.setUsername("Trainer1");
        Trainer trainer2 = new Trainer();
        trainer2.setUsername("Trainer2");
        List<Trainer> trainers = Arrays.asList(trainer1, trainer2);

        Training training1 = new Training();
        training1.setTrainee(trainee1);
        training1.setTrainer(trainer1);

        Training training2 = new Training();
        training2.setTrainee(trainee2);
        training2.setTrainer(trainer2);

        List<Training> trainings = Arrays.asList(training1, training2);

        dataWrapper = new DataWrapper();
        dataWrapper.setTrainees(trainees);
        dataWrapper.setTrainers(trainers);
        dataWrapper.setTrainings(trainings);

        ReflectionTestUtils.setField(storageInitializer, "dataFilePath", "test-path");
    }

    @Test
    void testInit(){
        try (MockedStatic<FileUtil> fileUtilMockedStatic = mockStatic(FileUtil.class)) {
            fileUtilMockedStatic.when(() -> FileUtil.readData(anyString(), eq(DataWrapper.class))).thenReturn(dataWrapper);

            storageInitializer.init();

            verify(traineeService, times(2)).saveTrainee(any(Trainee.class));
            verify(trainerService, times(2)).saveTrainer(any(Trainer.class));
            verify(trainingService, times(2)).createTraining(any(Training.class));
        }
    }
}