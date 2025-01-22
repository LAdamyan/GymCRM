package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TraineeDAO;
import org.gymCrm.hibernate.dao.TrainerDAO;
import org.gymCrm.hibernate.dao.TrainingDAO;
import org.gymCrm.hibernate.model.*;
import org.gymCrm.hibernate.service.TrainingService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final UserDetailsService<User> userDetailsService;

    public TrainingServiceImpl(TrainingDAO trainingDAO, TraineeDAO traineeDAO, TrainerDAO trainerDAO, UserDetailsService<User> userDetailsService) {
        this.trainingDAO = trainingDAO;
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        this.userDetailsService = userDetailsService;
    }
    @Transactional
    @Override
    public void createTraining(Training training, String username, String password) {
        trainingDAO.create(training);
        log.info("Created training {}", training);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Training>> getTrainingByType(TrainingType type, String username, String password) {
        return trainingDAO.selectByType(type);
    }
    @Transactional
    @Override
    public Optional<List<Training>> getTraineeTrainings(String username, String password, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) {
        return trainingDAO.getTraineeTrainings(username,fromDate,toDate,trainerName,trainingType);
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<List<Training>> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) {
        return trainingDAO.getTraineeTrainings(username,fromDate,toDate,trainerName,trainingType);

    }
    @Transactional
    @Override
    public Optional<List<Training>> getTrainerTrainings(String username, String password, Date fromDate, Date toDate, String traineeName) {
        return  trainingDAO.getTrainerTrainings(username,fromDate,toDate,traineeName);
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<List<Training>> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName) {
        return trainingDAO.getTrainerTrainings(username,fromDate,toDate,traineeName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getAllTrainingTypes() {
        return trainingDAO.getDistinctTrainingTypes();
    }
}
