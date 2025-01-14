package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TrainerDAO;
import org.gymCrm.hibernate.dto.UpdateTrainerDTO;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.service.TrainerService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDAO trainerDAO;
    private final UserDetailsService<Trainer> userDetailsService;

    public TrainerServiceImpl(TrainerDAO trainerDAO, UserDetailsService userDetailsService) {

        this.trainerDAO = trainerDAO;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    @Override
    public void saveTrainer(Trainer trainer) {
        log.info("Attempting to save trainer: {}", trainer);
        try {
            trainerDAO.create(trainer);
            log.info("Trainer saved successfully with ID: {}", trainer.getId());
        } catch (Exception e) {
            log.error("Error while saving trainer", e);
            throw e;
        }
    }

    @Override
    public void updateTrainer(Trainer trainer, String username, String password) {
        log.info("Attempting to update trainer: {}", trainer);
        if (!userDetailsService.authenticate(trainer.getUsername(), password)) {
            throw new SecurityException("User " + trainer.getUsername() + " not authenticated, permission denied!");
        }
        trainerDAO.update(trainer);
        log.info("Updated trainer with username: {}", username);
    }

    @Override
    public Optional<Trainer> updateTrainer(String username, UpdateTrainerDTO updateDTO) {
        Optional<Trainer> trainerOpt = trainerDAO.selectByUsername(username);
        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();
            trainer.setFirstName(updateDTO.getFirstName());
            trainer.setLastName(updateDTO.getLastName());
            trainerDAO.create(trainer);
            return Optional.of(trainer);
        }
        return Optional.empty();
    }

    @Override
    public void updateTrainer(Trainer trainer, String username) {
        trainerDAO.update(trainer);
        log.info("Updated trainer with username: {}", username);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> getTrainerByUsername(String username, String password) {
        log.info("Fetching trainer by ID: {}", username);
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("Invalid username or password");
        }
        return trainerDAO.selectByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> getTrainerByUsername(String username) {
        log.info("Fetching trainer by ID: {}", username);
        return trainerDAO.selectByUsername(username);
    }

    @Override
    public void changeTrainersPassword(String username, String oldPassword, String newPassword) {
        if (!userDetailsService.authenticate(username, oldPassword)) {
            throw new SecurityException("Invalid username or password");
        }
        trainerDAO.changeTrainersPassword(username, newPassword);
        log.info("Trainer's {} password changed", username);
    }

    @Override
    public void changeTrainerActiveStatus(String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        Optional<Trainer> optionalTrainer = trainerDAO.selectByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            trainerDAO.changeTrainerActiveStatus(username);

            String action = trainer.isActive() ? "activated" : "deactivated";
            log.info("Trainer {} successfully {} ", username, action);
        } else {
            log.warn("Trainer not found for username: {}", username);
        }
    }
    @Override
    public void changeTrainerActiveStatus(String username, String password,boolean isActive) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        Optional<Trainer> optionalTrainer = trainerDAO.selectByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            trainer.setActive(isActive);
            trainerDAO.update(trainer);

            String action = trainer.isActive() ? "activated" : "deactivated";
            log.info("Trainer {} successfully {} ", username, action);
        } else {
            log.warn("Trainer not found for username: {}", username);
        }
    }

    @Override
    public Optional<List<Trainer>> getUnassignedTrainers(String username, String password, String traineeUsername) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        log.info("Get unassigned trainers for trainee {} ", traineeUsername);
        return trainerDAO.getUnassignedTrainers(traineeUsername);
    }

}
