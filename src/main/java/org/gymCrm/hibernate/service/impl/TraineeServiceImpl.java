package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TraineeDAO;
import org.gymCrm.hibernate.dao.TrainerDAO;
import org.gymCrm.hibernate.dto.trainee.UpdateTraineeDTO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.service.TraineeService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final UserDetailsService<Trainee> userDetailsService;


    public TraineeServiceImpl(TraineeDAO traineeDAO, UserCredentialsUtil userCredentialsUtil, TrainerDAO trainerDAO, UserDetailsService<Trainee> userDetailsService) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    @Override
    public String saveTrainee(Trainee trainee) {
        traineeDAO.create(trainee);
        log.info("Created new trainee with username: {}", trainee.getUsername());
        return trainee.getPassword();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Trainee>> getAllTrainees(String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            log.warn("Authentication failed for user: {}", username);
            return Optional.empty();
        } else {
            log.info("Access granted. Retrieving all trainees.");
            return traineeDAO.listAll();
        }
    }

    @Override
    public Optional<List<Trainee>> getAllTrainees() {
        return traineeDAO.listAll();
    }

    @Transactional
    @Override
    public Optional<Trainee> getTraineeByUsername(String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        log.info("Fetching trainee by username: {}", username);
        return traineeDAO.selectByUsername(username);

    }
    @Transactional(readOnly = true)
    @Override
    public Optional<Trainee> getTraineeByUsername(String username) {
        log.info("Fetching trainee by username without authentication: {}", username);
        return traineeDAO.selectByUsername(username);
    }

    @Transactional
    @Override
    public void updateTrainee(Trainee trainee, String username, String password) {
        if (!userDetailsService.authenticate(trainee.getUsername(), password)) {
            throw new SecurityException("User " + trainee.getUsername() + " not authenticated, permission denied!");
        }
        traineeDAO.update(trainee);
        log.info("Updated trainee with username: {}", username);
    }


    @Transactional
    @Override
    public void updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        log.info("Updating trainers for trainee: {}", traineeUsername);

        Trainee trainee = traineeDAO.selectByUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee with username " + traineeUsername + " not found"));

        List<Trainer> trainers = trainerUsernames.stream()
                .map(username -> {
                    Trainer trainer = trainerDAO.selectByUsername(username)
                            .orElseThrow(() -> new EntityNotFoundException("Trainer with username " + username + " not found"));
                    Hibernate.initialize(trainer.getTrainees());
                    return trainer;
                })
                .collect(Collectors.toList());

        Hibernate.initialize(trainee.getTrainers());

        trainee.setTrainers(new HashSet<>(trainers));
        traineeDAO.update(trainee);

        log.info("Successfully updated trainers for trainee: {}", traineeUsername);
    }

    @Transactional
    @Override
    public void deleteTrainee(String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        traineeDAO.delete(username);
        log.info("Deleted trainee with username: {}", username);
    }
    @Transactional
    @Override
    public void deleteTrainee(String username) {
        traineeDAO.delete(username);
        log.info("Deleted trainee with username: {}", username);
    }


    @Override
    public void changeTraineesPassword(String username, String oldPassword, String newPassword) {
       if(!userDetailsService.authenticate(username,oldPassword)){
           throw  new SecurityException("User " + username + " not authenticated, permission denied!");
       }
       traineeDAO.changeTraineesPassword(username,newPassword);
       log.info("Trainee's {} password changed",username);
    }

    @Override
    public void changeTraineeActiveStatus(String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        Optional<Trainee> optionalTrainee = traineeDAO.selectByUsername(username);

       if(optionalTrainee.isPresent()){
           Trainee trainee = optionalTrainee.get();
           traineeDAO.changeTraineeActiveStatus(username);

           String action = trainee.isActive() ? "deactivated" : "activated";
           log.info("Trainee {} successfully {}", username, action);
       } else {
           log.warn("Trainee not found for username: {}", username);
       }
       }

    @Override
    public void changeTraineeActiveStatus(String username, String password, boolean isActive) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        Optional<Trainee> optionalTrainee = traineeDAO.selectByUsername(username);
        if(optionalTrainee.isPresent()){
            Trainee trainee = optionalTrainee.get();
            trainee.setActive(isActive);
            traineeDAO.update(trainee);  // Make sure DAO supports updating a Trainee

            String action = isActive ? "activated" : "deactivated";
            log.info("Trainee {} successfully {}", username, action);
        } else {
            log.warn("Trainee not found for username: {}", username);
        }
    }

    @Override
    public Optional<Trainee> updateTrainee(String username, UpdateTraineeDTO updateTraineeDTO) {
        Optional<Trainee>traineeOpt = traineeDAO.selectByUsername(username);
        if(traineeOpt.isPresent()){
            Trainee trainee = traineeOpt.get();
            trainee.setFirstName(updateTraineeDTO.getFirstName());
            trainee.setLastName(updateTraineeDTO.getLastName());
            traineeDAO.create(trainee);
            return Optional.of(trainee);
        }
        return Optional.empty();
    }


}



