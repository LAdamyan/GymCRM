package org.gymCrm.hibernate.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

    void createTraining(Training training, String username, String password);


    Optional<List<Training>> getTrainingByType(TrainingType type, String username, String password);

    Optional<List<Training>> getTraineeTrainings(String username, String password, Date fromDate, Date toDate, String trainerName, TrainingType trainingType);
    Optional<List<Training>> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, TrainingType trainingType);

    Optional<List<Training>> getTrainerTrainings(String username, String password, Date fromDate, Date toDate, String traineeName);
    Optional<List<Training>> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName);

    List<String> getAllTrainingTypes();



}
