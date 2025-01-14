package org.gymCrm.hibernate.dao;

import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingDAO {

    void create(Training training);

    Optional<List<Training>> selectByType(TrainingType type);

    Optional<List<Training>> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, TrainingType trainingType);

    Optional<List<Training>> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName);

    List<String> getDistinctTrainingTypes();
}
