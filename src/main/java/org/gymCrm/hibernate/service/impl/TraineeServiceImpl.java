package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TraineeDAO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.service.TraineeService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDAO traineeDAO;
    private final UserDetailsService<Trainee> userDetailsService;


    public TraineeServiceImpl(TraineeDAO traineeDAO, UserCredentialsUtil userCredentialsUtil, UserDetailsService<Trainee> userDetailsService) {
        this.traineeDAO = traineeDAO;
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

    @Transactional
    @Override
    public Optional<Trainee> getTraineeByUsername(String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        log.info("Fetching trainee by username: {}", username);
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
    public void deleteTrainee(String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
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
    public void changeTraineeActivation(String username, String password,boolean activate) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("User " + username + " not authenticated, permission denied!");
        }
        traineeDAO.changeTraineeActivation(username,activate);
        String action = activate ? "activated" : "deactivated";
        log.info("Trainee {} successfully {}", username, action);
    }


}
