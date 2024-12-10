package org.gymCrm.service;

import org.gymCrm.model.Trainee;

import java.util.List;

public interface TraineeService {

    void saveTrainee(Trainee trainee);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Integer id);
    void updateTrainee(Trainee trainee);
    void deleteTrainee(Integer id);
}
