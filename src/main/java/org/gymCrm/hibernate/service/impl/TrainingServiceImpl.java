package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TrainingDAO;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.service.TrainingService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;
    private final UserDetailsService<User> userDetailsService;

    public TrainingServiceImpl(TrainingDAO trainingDAO, UserDetailsService<User> userDetailsService) {
        this.trainingDAO = trainingDAO;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public void createTraining(Training training, String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("Authentication failed for user: " + username + " permission denied");
        }
        trainingDAO.create(training);
        log.info("Created training {}", training);
    }

    @Override
    public Optional<List<Training>> getTrainingByType(TrainingType type, String username, String password) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("Authentication failed for user: " + username + " permission denied");
        }
        return trainingDAO.selectByType(type);
    }

    @Override
    public Optional<List<Training>> getTraineeTrainings(String username, String password, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("Authentication failed for user: " + username + " permission denied");
        }
        return trainingDAO.getTraineeTrainings(username,fromDate,toDate,trainerName,trainingType);
    }
    @Override
    public Optional<List<Training>> getTrainerTrainings(String username, String password, Date fromDate, Date toDate, String traineeName) {
        if (!userDetailsService.authenticate(username, password)) {
            throw new SecurityException("Authentication failed for user: " + username + " permission denied");
        }
        return  trainingDAO.getTrainerTrainings(username,fromDate,toDate,traineeName);
    }
}
