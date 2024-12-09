package org.example.dao.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.dao.TrainingDAO;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.example.util.UsernamePasswordUtil;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryTrainingDAO implements TrainingDAO {

    private static long counter = 0;
    private final Map<String, Training> trainingMap;

    public InMemoryTrainingDAO(Map<String, Training> trainingMap) {
        this.trainingMap = trainingMap;
        log.info("InMemoryTrainingDAO initialized with {} trainings", trainingMap.size());
    }

    @Override
    public void create(Training training) {
        val traineeName = training.getTrainee().getFirstName();
        val traineeLastName = training.getTrainee().getLastName();
        val trainerName = training.getTrainer().getFirstName();
        val trainerLastName = training.getTrainer().getLastName();

        training.getTrainee().setUsername(UsernamePasswordUtil.generateUsername(traineeName, traineeLastName));
        training.getTrainer().setUsername(UsernamePasswordUtil.generateUsername(trainerName, trainerLastName));
        training.getTrainee().setPassword(UsernamePasswordUtil.generatePassword());
        training.getTrainer().setPassword(UsernamePasswordUtil.generatePassword());

        String uniqueId = generateUniqueId();
        trainingMap.put(uniqueId, training);
        log.info("Created training with unique ID {}. Trainee: {} {}, Trainer: {} {}",
                uniqueId, traineeName, traineeLastName, trainerName, trainerLastName);
    }

    @Override
    public List<Training> selectByType(TrainingType type) {
        List<Training> trainingsByType = new ArrayList<>();
        for (Training training : trainingMap.values()) {
            if (training.getType() == type) {
                trainingsByType.add(training);
            }
        }
        log.info("Selected {} trainings of type {}", trainingsByType.size(), type);
        return trainingsByType;
    }

    private static synchronized String generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        String uniqueId = String.valueOf(timestamp) + counter;
        counter++;
        return uniqueId;
    }
}

