package org.example.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeDAO;
import org.example.model.Trainee;
import org.example.util.UsernamePasswordUtil;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryTraineeDAO implements TraineeDAO {

    private final Map<String, Trainee> traineeMap;

    public InMemoryTraineeDAO(Map<String, Trainee> traineeMap) {
        this.traineeMap = traineeMap;
        log.info("Initialized with {} initial trainees", traineeMap.size());
    }

    @Override
    public void create(Trainee trainee) {
        String username = UsernamePasswordUtil.generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = UsernamePasswordUtil.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        traineeMap.put(String.valueOf(trainee.getId()), trainee);
        log.info("Created new trainee: {} {} , ID: {}",
                trainee.getFirstName(), trainee.getLastName(),
                trainee.getId());
    }

    @Override
    public void update(Trainee trainee) {
        if (traineeMap.containsKey(String.valueOf(trainee.getId()))) {
            traineeMap.put(String.valueOf(trainee.getId()), trainee);
            log.info("Updated trainee with ID: {}", trainee.getId());
        } else {
            log.warn("Attempted to update non-existing trainee with ID: {}", trainee.getId());
        }
    }

    @Override
    public void delete(int id) {
        if (traineeMap.containsKey(String.valueOf(id))) {
            traineeMap.remove(String.valueOf(id));
            log.info("Deleted trainee with ID: {}", id);
        } else {
            log.warn("Attempted to delete non-existing trainee with ID: {}", id);
        }
    }

    @Override
    public Trainee selectById(int id) {
        Trainee trainee = traineeMap.get(String.valueOf(id));
        if (trainee != null) {
            log.debug("Selected trainee: {} {}", trainee.getFirstName(), trainee.getLastName());
        } else {
            log.warn("No trainee found with ID: {}", id);
        }
        return trainee;
    }

    @Override
    public List<Trainee> listAll() {
        List<Trainee> allTrainees = new ArrayList<>(traineeMap.values());
        log.info("Listing all trainees: {} trainees found", allTrainees.size());
        return allTrainees;
    }
}


