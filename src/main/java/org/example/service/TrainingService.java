package org.example.service;

import org.example.model.Training;
import org.example.model.TrainingType;

import java.util.List;

public interface TrainingService {

    void createTraining(Training training);
    List<Training> getTrainingByType(TrainingType type);

}
