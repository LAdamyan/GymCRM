package org.gymCrm.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.dao.TraineeDAO;
import org.gymCrm.model.Trainee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class InMemoryTraineeDAO implements TraineeDAO {

    private final Map<Integer, Trainee> traineeMap;

    public InMemoryTraineeDAO(Map<Integer, Trainee> traineeMap) {
        this.traineeMap = traineeMap;
        log.info("Initialized with {} initial trainees", traineeMap.size());
    }

    @Override
    public void create(Trainee trainee) {
        traineeMap.put(trainee.getId(), trainee);
        log.info("Trainee created in memory: {}", trainee);
    }

    @Override
    public void update(Trainee trainee) {
        if (traineeMap.containsKey((trainee.getId()))) {
            traineeMap.put(trainee.getId(), trainee);
            log.info("Updated trainee with ID: {}", trainee.getId());
        } else {
            log.warn("Attempted to update non-existing trainee with ID: {}", trainee.getId());
        }
    }

    @Override
    public void delete(int id) {
        if (traineeMap.containsKey(id)) {
            traineeMap.remove(id);
            log.info("Deleted trainee with ID: {}", id);
        } else {
            log.warn("Attempted to delete non-existing trainee with ID: {}", id);
        }
    }

    @Override
    public Trainee selectById(int id) {
        Trainee trainee = traineeMap.get(id);
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


