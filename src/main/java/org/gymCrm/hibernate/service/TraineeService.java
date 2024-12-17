package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService {

    void saveTrainee(Trainee trainee);
    Optional<List<Trainee>> getAllTrainees(String username, String password);
    Optional<Trainee> getTraineeByUsername(String username,String password);
    void updateTrainee(Trainee trainee,String username, String password);
    void deleteTrainee(String username, String password);
    void changeTraineesPassword(String username,String oldPassword,String newPassword);
    void activateTrainee(String username, String password);
    void deactivateTrainee(String username, String password);
}
