package org.gymCrm.hibernate.service.impl;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.endpoint.CustomMetrics;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.repo.TraineeRepository;
import org.gymCrm.hibernate.util.AuthenticationService;
import org.gymCrm.hibernate.service.TraineeService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;

    private final UserCredentialsUtil userCredentialsUtil;

    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;


    public TraineeServiceImpl(TraineeRepository traineeRepository, UserCredentialsUtil userCredentialsUtil, AuthenticationService authenticationService, CustomMetrics customMetrics, PasswordEncoder passwordEncoder) {
        this.traineeRepository = traineeRepository;
        this.userCredentialsUtil = userCredentialsUtil;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Map<String, String> create(Trainee trainee) {
        log.info("Saving trainee: {}", trainee);
        try {
            String username = UserCredentialsUtil.generateUsername(trainee.getFirstName(), trainee.getLastName());
            Optional<Trainee> existingTrainee = traineeRepository.findByUsername(username);

            if (existingTrainee.isPresent()) {
                log.debug("Trainee with username '{}' already exists.", username);
                return Map.of("message", "Trainee already exists", "username", username);
            } else {
                trainee.setUsername(username);
                // Generate a raw password
                String rawPassword = UserCredentialsUtil.generatePassword();

                // Encode the password for DB storage
                String encodedPassword = passwordEncoder.encode(rawPassword);
                trainee.setPassword(encodedPassword);

                traineeRepository.save(trainee);
                log.debug("Trainee saved successfully with username: {}", username);

                // Return raw password to user
                return Map.of("username", username, "password", rawPassword);
            }
        } catch (Exception e) {
            log.error("Error while creating trainee", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void update(Trainee trainee,String username, String password) {
        log.info("Updating trainee: {}", trainee);
        if (!authenticationService.authenticate(trainee.getUsername(), password)) {
            throw new SecurityException("User " + trainee.getUsername() + " not authenticated, permission denied!");
        }
        try {
            if (traineeRepository.existsById(trainee.getId())) {
                traineeRepository.save(trainee);
                log.info("Trainee with username {} updated successfully.",username);
            } else {
                log.warn("Trainee with ID {} not found.", trainee.getId());
            }
        } catch (Exception e) {
            log.error("Error while updating trainee", e);
        }
    }

    @Transactional
    @Override
    public void delete(String username, String password) {
        log.info("Deleting trainee with username {}", username);
        if (!authenticationService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        try {
            traineeRepository.deleteByUsername(username);
            log.info("Deleted trainee with username: {}", username);
        } catch (Exception e) {
            log.error("Error during deletion of trainee with username: {}", username, e);
            throw e;
        }
    }


    @Transactional
    @Override
    public void delete(String username) {
        traineeRepository.deleteByUsername(username);
        log.info("Deleted trainee with username: {}", username);
    }


    @Transactional
    @Override
    public Optional<Trainee> selectByUsername(String username) {
        log.info("Fetching trainee by username: {}", username);
        try {
            return traineeRepository.findByUsername(username);
        } catch (NoResultException e) {
            log.warn("No trainee found for username: {}", username);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error occurred while fetching trainee with username: {}", username, e);
            throw e;
        }
    }

    @Transactional
    @Override
    public List<Trainee> listAll() {
        log.info("Fetching all trainees.");
        return traineeRepository.findAll();
    }


    @Transactional
    @Override
    public void changeTraineePassword(String username,String oldPassword, String newPassword) {
        log.info("Changing password for trainee with username {}", username);
        if (!authenticationService.authenticate(username, oldPassword)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        try {
            Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(username);
            optionalTrainee.ifPresentOrElse(
                    trainee -> {
                        trainee.setPassword(passwordEncoder.encode(newPassword));
                        traineeRepository.save(trainee);
                        log.info("Password updated successfully for trainee: {}", username);
                    },
                    () -> log.warn("Trainee with username '{}' not found.", username));
        } catch (Exception e) {
            log.error("Error while changing password for trainee with username: {}", username, e);
            throw e;

        }
    }

    @Transactional
    @Override
    public void changeTraineeActiveStatus(String username, String password,boolean isActive) {
        log.info("Changing active status of trainee with username {}", username);
        if (!authenticationService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
            Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(username);
            if (optionalTrainee.isPresent()) {
                Trainee trainee = optionalTrainee.get();
                trainee.setActive(isActive);
                traineeRepository.save(trainee);

                String action = isActive ? "activated" : "deactivated";
                log.info("Trainee {} successfully {}", username, action);
            } else {
                log.warn("Trainee not found for username: {}", username);
            }
        }

   @Transactional
   @Override
    public void updateTraineeTrainers(String traineeUsername,List<Trainer>trainers){
       log.info("Updating trainers for trainee with username: {}", traineeUsername);
       try{
           Optional<Trainee>optionalTrainee = traineeRepository.findByUsername(traineeUsername);
           optionalTrainee.ifPresentOrElse(
                   trainee -> {
                       trainee.setTrainers(new HashSet<>(trainers));
                       traineeRepository.save(trainee);
                       log.info("Trainers updated for trainee: {}", traineeUsername);
                   },
           ()->log.warn("Trainee with username '{}' not found.", traineeUsername)
           );
       }catch (Exception e){
           log.error("Error while updating trainers for trainee with username: {}", traineeUsername, e);
           throw e;
       }
   }


}




