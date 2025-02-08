package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.trainer.UpdateTrainerDTO;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.repo.TrainerRepository;
import org.gymCrm.hibernate.util.AuthenticationService;
import org.gymCrm.hibernate.service.TrainerService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    private final UserCredentialsUtil userCredentialsUtil;

    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;


    public TrainerServiceImpl(TrainerRepository trainerRepository, UserCredentialsUtil userCredentialsUtil, AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.userCredentialsUtil = userCredentialsUtil;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean doesTrainerExist(String username) {
        return trainerRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public Map<String,String> create(Trainer trainer) {
        log.info("Saving trainer: {}", trainer);
        try {
            String username = UserCredentialsUtil.generateUsername(trainer.getFirstName(), trainer.getLastName());
            Optional<Trainer> existingTrainer = trainerRepository.findByUsername(username);

             if (existingTrainer.isPresent()) {
                log.info("Trainer with username '{}' already exists.", username);
                return Map.of("message", "Trainer already exists", "username", username);
            } else {
                trainer.setUsername(username);
                // Generate a raw password
                String rawPassword = UserCredentialsUtil.generatePassword();

                // Encode the password for DB storage
                String encodedPassword = passwordEncoder.encode(rawPassword);
                trainer.setPassword(encodedPassword);

                trainerRepository.save(trainer);
                log.info("Trainee saved successfully with username: {}", username);

                // Return raw password to user
                return Map.of("username", username, "password", rawPassword);
            }
        } catch (Exception e) {
            log.error("Error while creating trainee", e);
            throw e;
        }
    }

    @Transactional
    public void update(Trainer trainer,String username,String password) {
        log.info("Updating trainer: {}", trainer);
        if (!authenticationService.authenticate(trainer.getUsername(), password)) {
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
        if (!authenticationService.authenticate(username, oldPassword)) {
            throw new SecurityException("Invalid username or password");
        }
        try {
            Optional<Trainer> trainerOptional = trainerRepository.findByUsername(username);
            trainerOptional.ifPresentOrElse(
                    trainer -> {
                        trainer.setPassword(passwordEncoder.encode(newPassword));
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
        if (!authenticationService.authenticate(username, password)) {
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
        if (!authenticationService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        log.info("Get unassigned trainers for trainee {} ", traineeUsername);
        return trainerRepository.getUnassignedTrainers(traineeUsername);
    }


}
