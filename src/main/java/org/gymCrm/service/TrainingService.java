package org.gymCrm.service;

import org.gymCrm.model.Training;
import org.gymCrm.model.TrainingType;

import java.util.List;

public interface TrainingService {

    void createTraining(Training training);
    List<Training> getTrainingByType(TrainingType type);

}
