package org.gymCrm.storage;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.model.Trainee;
import org.gymCrm.model.Trainer;
import org.gymCrm.model.Training;
import org.gymCrm.service.TraineeService;
import org.gymCrm.service.TrainerService;
import org.gymCrm.service.TrainingService;
import org.gymCrm.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class StorageInitializer implements BeanPostProcessor {

    @Value("${data.file.path}")
    private String dataFilePath;

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    private final TrainingService trainingService;

    public StorageInitializer(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @PostConstruct
    public void init() {
        try {
            DataWrapper dataWrapper = FileUtil.readData(dataFilePath, DataWrapper.class);
            log.info("Data file loaded successfully from {}", dataFilePath);

            List<Trainee> trainees = dataWrapper.getTrainees();
            for (Trainee trainee : trainees) {
                traineeService.saveTrainee(trainee);
                log.debug("Saved trainee: {}", trainee.getUsername());
            }

            List<Trainer> trainers = dataWrapper.getTrainers();
            for (Trainer trainer : trainers) {
                trainerService.saveTrainer(trainer);
                log.debug("Saved trainer: {}", trainer.getUsername());
            }

            List<Training> trainings = dataWrapper.getTrainings();
            for (Training training : trainings) {
                trainingService.createTraining(training);
                log.debug("Created training session for: {}, {}",
                        training.getTrainee().getUsername(), training.getTrainer().getUsername());
            }

        } catch (IOException e) {
            log.error("Failed to initialize storage from file at {}: {}", dataFilePath, e.getMessage());
            throw new RuntimeException("Failed to initialize storage from file", e);
        }
    }
}