package org.gymCrm.dao.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.gymCrm.dao.TrainingDAO;
import org.gymCrm.model.Training;
import org.gymCrm.model.TrainingType;
import org.gymCrm.util.UserCredentialsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class InMemoryTrainingDAO implements TrainingDAO {

    private static long counter = 0;
    private final Map<Long, Training> trainingMap;

    public InMemoryTrainingDAO(Map<Long, Training> trainingMap) {
        this.trainingMap = trainingMap;
        log.info("InMemoryTrainingDAO initialized with {} trainings", trainingMap.size());
    }

    @Override
    public void create(Training training) {
        val traineeName = training.getTrainee().getFirstName();
        val traineeLastName = training.getTrainee().getLastName();
        val trainerName = training.getTrainer().getFirstName();
        val trainerLastName = training.getTrainer().getLastName();

        training.getTrainee().setUsername(UserCredentialsUtil.generateUsername(traineeName, traineeLastName));
        training.getTrainer().setUsername(UserCredentialsUtil.generateUsername(trainerName, trainerLastName));
        training.getTrainee().setPassword(UserCredentialsUtil.generatePassword());
        training.getTrainer().setPassword(UserCredentialsUtil.generatePassword());

        Long uniqueId = generateUniqueId();
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

    private static synchronized Long generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        String uniqueIdStr = String.valueOf(timestamp) + counter;
        counter++;
        return Long.valueOf(uniqueIdStr);
    }

}