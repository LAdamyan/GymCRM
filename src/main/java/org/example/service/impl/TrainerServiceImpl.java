package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainerDAO;
import org.example.model.Trainer;
import org.example.service.TrainerService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDAO trainerDAO;

    public TrainerServiceImpl(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Override
    public void saveTrainer(Trainer trainer) {
        trainerDAO.create(trainer);
        log.info("Trainer saved: [ID: {}, Name: {} {}]",
                trainer.getId(), trainer.getFirstName(), trainer.getLastName());

    }

    @Override
    public void updateTrainer(Trainer trainer) {
        Trainer existing = trainerDAO.selectById(trainer.getId());
        if (existing != null) {
            trainerDAO.update(trainer);
            log.info("Updated trainer: [ID: {}, Name: {} {}]",
                    trainer.getId(), trainer.getFirstName(), trainer.getLastName());
        } else {
            log.warn("Failed to update trainer because it does not exist: ID {}", trainer.getId());
        }

    }

    @Override
    public Trainer getTrainerById(int id) {
        Trainer trainer = trainerDAO.selectById(id);
        if (trainer != null) {
            log.info("Retrieved trainer by ID: [ID: {}, Name: {} {}]",
                    id, trainer.getFirstName(), trainer.getLastName());
        } else {
            log.warn("Failed to retrieve trainer by ID {}", id);
        }
        return trainer;
    }
}
