package org.example.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainerDAO;
import org.example.model.Trainer;
import org.example.util.UsernamePasswordUtil;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
public class InMemoryTrainerDAO implements TrainerDAO {

    private final Map<String, Trainer> trainerMap;

    public InMemoryTrainerDAO(Map<String, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
        log.info("InMemoryTrainerDAO initialized with {} trainers", trainerMap.size());
    }

    @Override
    public void create(Trainer trainer) {
        String username = UsernamePasswordUtil.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = UsernamePasswordUtil.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainerMap.put(String.valueOf(trainer.getId()), trainer);
        log.info("Created trainer: ID {}, Username {}", trainer.getId(), username);
    }

    @Override
    public void update(Trainer trainer) {
        if (trainerMap.containsKey(String.valueOf(trainer.getId()))) {
            trainerMap.put(String.valueOf(trainer.getId()), trainer);
            log.info("Updated trainer with ID: {}", trainer.getId());
        } else {
            log.warn("Attempted to update non-existent trainer with ID: {}", trainer.getId());
        }
    }

    @Override
    public Trainer selectById(int id) {
        Trainer trainer = trainerMap.get(String.valueOf(id));
        if (trainer != null) {
            log.debug("Selected Trainer: ID {}, Name: {} {}", trainer.getId(), trainer.getFirstName(), trainer.getLastName());
        } else {
            log.warn("No trainer found with ID: {}", id);
        }
        return trainer;
    }
}
