package org.gymCrm.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.dao.TrainerDAO;
import org.gymCrm.model.Trainer;

import java.util.Map;

@Slf4j
public class InMemoryTrainerDAO implements TrainerDAO {

    private final Map<Integer, Trainer> trainerMap;

    public InMemoryTrainerDAO(Map<Integer, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
        log.info("InMemoryTrainerDAO initialized with {} trainers", trainerMap.size());
    }

    @Override
    public void create(Trainer trainer) {
        trainerMap.put(trainer.getId(), trainer);
        log.info("Trainer created in memory: {}", trainer);
    }

    @Override
    public void update(Trainer trainer) {
        if (trainerMap.containsKey(trainer.getId())) {
            trainerMap.put(trainer.getId(), trainer);
            log.info("Updated trainer with ID: {}", trainer.getId());
        } else {
            log.warn("Attempted to update non-existent trainer with ID: {}", trainer.getId());
        }
    }

    @Override
    public Trainer selectById(int id) {
        Trainer trainer = trainerMap.get(id);
        if (trainer != null) {
            log.debug("Selected Trainer: ID {}, Name: {} {}", trainer.getId(), trainer.getFirstName(), trainer.getLastName());
        } else {
            log.warn("No trainer found with ID: {}", id);
        }
        return trainer;
    }
}
