package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.trainer.UpdateTrainerDTO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.repo.TrainerRepository;
import org.gymCrm.hibernate.service.TrainerService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    private final UserCredentialsUtil userCredentialsUtil;

    private final UserDetailsService<Trainer> userDetailsService;

    public TrainerServiceImpl(TrainerRepository trainerRepository, UserCredentialsUtil userCredentialsUtil, UserDetailsService<Trainer> userDetailsService) {
        this.trainerRepository = trainerRepository;
        this.userCredentialsUtil = userCredentialsUtil;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public void create(Trainer trainer) {
        log.info("Saving trainer: {}", trainer);
        try {
            String username = userCredentialsUtil.generateUsername(trainer.getFirstName(), trainer.getLastName());
            Optional<Trainer> existingTrainer = trainerRepository.findByUsername(username);

            if (existingTrainer.isPresent()) {
                Trainer existing = existingTrainer.get();
                trainer.setUsername(existing.getUsername());
                trainer.setPassword(existing.getPassword());
                log.info("Trainer with username '{}' already exists. Using existing credentials.", username);
            } else {
                trainer.setUsername(username);
                trainer.setPassword(userCredentialsUtil.generatePassword());
                log.info("Generated new username '{}' and password for the trainer.", username);
            }
            trainerRepository.save(trainer);
            log.info("Trainer saved successfully with username: {}", username);
        } catch (Exception e) {
            log.error("Error while creating trainer", e);
            throw e;
        }
    }

    @Transactional
    public void update(Trainer trainer,String username,String password) {
        log.info("Updating trainer: {}", trainer);
        if (!userDetailsService.authenticate(trainer.getUsername(), password)) {
            throw new SecurityException("User " + trainer.getUsername() + " not authenticated, permission denied!");
        }
        try {
            if (trainerRepository.existsById(trainer.getId())) {
                trainerRepository.save(trainer);
                log.info("Trainer with username {} updated successfully.",username);
            } else {
                log.warn("Trainer with ID {} not found.", trainer.getId());
            }
        } catch (Exception e) {
            log.error("Error while updating trainer", e);
            throw e;
        }
    }

    @Override
    public Optional<Trainer> updateTrainer(String username, UpdateTrainerDTO updateDTO) {
        Optional<Trainer> trainerOpt = trainerRepository.findByUsername(username);
        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();
            trainer.setFirstName(updateDTO.getFirstName());
            trainer.setLastName(updateDTO.getLastName());
            String newUsername = userCredentialsUtil.generateUsername(updateDTO.getFirstName(),updateDTO.getLastName());;
            trainer.setUsername(newUsername);
            trainerRepository.save(trainer);
            return Optional.of(trainer);
        }
        return Optional.empty();
    }

    public Optional<Trainer> selectByUsername(String username) {
        log.info("Fetching trainer by username: {}", username);
        try {
            return trainerRepository.findByUsername(username);
        } catch (Exception e) {
            log.error("Error occurred while fetching trainer with username: {}", username, e);
            throw e;
        }
    }

    @Transactional
    public void changeTrainersPassword(String username, String oldPassword,String newPassword) {
        log.info("Changing password for trainer with username: {}", username);
        if (!userDetailsService.authenticate(username, oldPassword)) {
            throw new SecurityException("Invalid username or password");
        }
        try {
            Optional<Trainer> trainerOptional = trainerRepository.findByUsername(username);
            trainerOptional.ifPresentOrElse(
                    trainer -> {
                        trainer.setPassword(newPassword);
                        trainerRepository.save(trainer);
                        log.info("Password updated successfully for trainer: {}", username);
                    },
                    () -> log.warn("Trainer with username '{}' not found.", username)
            );
        } catch (Exception e) {
            log.error("Error while changing password for trainer with username: {}", username, e);
            throw e;
        }
    }

    @Transactional
    public void changeTrainerActiveStatus(String username,String password,boolean isActive) {
        log.info("Changing active status of trainer with username {}", username);
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            trainer.setActive(isActive);
            trainerRepository.save(trainer);

            String action = isActive ? "activated" : "deactivated";
            log.info("Trainer {} successfully {}", username, action);
        } else {
            log.warn("Trainer not found for username: {}", username);
        }
    }

    @Override
    public Optional<List<Trainer>> getUnassignedTrainers(String username) {
        log.info("Get unassigned trainers for trainee {} ",username);
        return trainerRepository.getUnassignedTrainers(username);
    }

    @Override
    public Optional<List<Trainer>> getUnassignedTrainers(String traineeUsername,  String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        log.info("Get unassigned trainers for trainee {} ", traineeUsername);
        return trainerRepository.getUnassignedTrainers(traineeUsername);
    }


}
