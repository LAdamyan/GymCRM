package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeDAO;
import org.example.model.Trainee;
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDAO traineeDAO;

    public TraineeServiceImpl(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Override
    public void saveTrainee(Trainee trainee) {
        traineeDAO.create(trainee);
        log.info("Trainee saved: [ID: {}, Name: {} {}]",
                trainee.getId(), trainee.getFirstName(), trainee.getLastName());
    }

    @Override
    public List<Trainee> getAllTrainees() {
        List<Trainee> allTrainees = new ArrayList<>(traineeDAO.listAll());
        log.info("Retrieved all trainees. Total number: {}", allTrainees.size());
        return allTrainees;
    }

    @Override
    public Trainee getTraineeById(Integer id) {
        Trainee trainee = traineeDAO.selectById(id);
        if (trainee != null) {
            log.info("Retrieved trainee by ID: [ID: {}, Name: {} {}]",
                    id, trainee.getFirstName(), trainee.getLastName());
        } else {
            log.warn("Failed to retrieve trainee by ID {}", id);
        }
        return trainee;
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        Trainee existing = traineeDAO.selectById(trainee.getId());
        if (existing != null) {
            traineeDAO.update(trainee);
            log.info("Updated trainee: [ID: {}, Name: {} {}]",
                    trainee.getId(), trainee.getFirstName(), trainee.getLastName());
        } else {
            log.warn("Failed to update trainee because it does not exist: ID {}", trainee.getId());
        }
    }

    @Override
    public void deleteTrainee(Integer id) {
        Trainee trainee = traineeDAO.selectById(id);
        if (trainee != null) {
            traineeDAO.delete(id);
            log.info("Deleted trainee by ID {}", id);
        } else {
            log.warn("Failed to delete trainee because it does not exist: ID {}", id);
        }
    }
}
