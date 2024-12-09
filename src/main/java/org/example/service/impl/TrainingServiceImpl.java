package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainingDAO;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.example.service.TrainingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;

    public TrainingServiceImpl(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public void createTraining(Training training) {
        trainingDAO.create(training);
        log.info("Created training session: [Training name: {}, Trainee: {}, Trainer: {}]",
                training.getTrainingName(),
                training.getTrainee().getUsername(),
                training.getTrainer().getUsername());
    }

    @Override
    public List<Training> getTrainingByType(TrainingType type) {
        List<Training> trainings = new ArrayList<>(trainingDAO.selectByType(type));
        log.info("Retrieved {} training sessions of type {}", trainings.size(), type);
        return trainings;
    }
}
