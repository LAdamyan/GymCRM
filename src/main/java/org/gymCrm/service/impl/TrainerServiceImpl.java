package org.gymCrm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.dao.TrainerDAO;
import org.gymCrm.model.Trainer;
import org.gymCrm.service.TrainerService;
import org.gymCrm.util.UserCredentialsUtil;

@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDAO trainerDAO;

    public TrainerServiceImpl(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Override
    public void saveTrainer(Trainer trainer) {
        String username = UserCredentialsUtil.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = UserCredentialsUtil.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);

        trainerDAO.create(trainer);

        log.info("Trainer saved: [ID: {}, Name: {} {}, Username: {}]",
                trainer.getId(), trainer.getFirstName(), trainer.getLastName(), username);

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
