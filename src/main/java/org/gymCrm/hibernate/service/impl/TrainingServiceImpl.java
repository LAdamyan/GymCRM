package org.gymCrm.hibernate.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.client.TrainerWorkloadClient;
import org.gymCrm.hibernate.config.FeignClientInterceptor;
import org.gymCrm.hibernate.config.jwt.backendJwt.BackendJwtUtil;
import org.gymCrm.hibernate.dto.TrainerWorkloadRequest;
import org.gymCrm.hibernate.dto.TrainingRequest;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.repo.TrainingRepository;
import org.gymCrm.hibernate.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;

    @Autowired
    private TrainerWorkloadClient trainerWorkloadClient;


    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public void create(Training training) {
        try {
            trainingRepository.save(training);
            log.info("Training created successfully: {}", training);
        } catch (Exception e) {
            log.error("Error while creating training", e);
            throw e;
        }
    }
    
    public void addTraining(TrainingRequest request) {
        TrainerWorkloadRequest workloadRequest = new TrainerWorkloadRequest(
                request.getTrainerUsername(),
                request.getTrainerFirstName(),
                request.getTrainerLastName(),
                request.isActive(),
                request.getTrainingDate(),
                request.getTrainingDuration(),
                "ADD"
        );
        trainerWorkloadClient.updateTrainerWorkload(workloadRequest);
    }

    public void deleteTraining(TrainingRequest request) {
        TrainerWorkloadRequest workloadRequest = new TrainerWorkloadRequest(
                request.getTrainerUsername(),
                request.getTrainerFirstName(),
                request.getTrainerLastName(),
                request.isActive(),
                request.getTrainingDate(),
                request.getTrainingDuration(),
                "DELETE"
        );
        trainerWorkloadClient.updateTrainerWorkload(workloadRequest);
    }

      public Optional<List<Training>> selectByType(TrainingType type) {
        try {
            List<Training> trainings = trainingRepository.findByTrainingType(type);
            return Optional.ofNullable(trainings);
        } catch (Exception e) {
            log.error("Error while fetching trainings by type", e);
            throw e;
        }
    }

    public Optional<List<Training>> getTraineeTrainings(
            String username,
            Date fromDate,
            Date toDate,
            String trainerName,
            TrainingType trainingType) {
        log.info("Fetching trainings for trainee: {}", username);
        try {
            return Optional.of(trainingRepository.findTraineeTrainings(username, fromDate, toDate, trainerName, String.valueOf(trainingType)));
        } catch (Exception e) {
            log.error("Error while fetching trainee trainings", e);
            throw e;
        }
    }

    public Optional<List<Training>> getTrainerTrainings(
            String username,
            Date fromDate,
            Date toDate,
            String traineeName
    ) {
        log.info("Fetching trainings for trainer: {}", username);
        try {
            return Optional.of(trainingRepository.findTrainerTrainings(username, fromDate, toDate, traineeName));
        } catch (Exception e) {
            log.error("Error while fetching trainer trainings", e);
            throw e;
        }
    }

    public List<String> getDistinctTrainingTypes() {
        try {
            return trainingRepository.findDistinctTrainingTypes();
        } catch (Exception e) {
            log.error("Error while fetching distinct training types", e);
            throw e;
        }
    }
}