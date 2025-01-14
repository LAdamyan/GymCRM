package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService {

    String saveTrainee(Trainee trainee);
    Optional<List<Trainee>> getAllTrainees(String username, String password);
    Optional<List<Trainee>> getAllTrainees();
    Optional<Trainee> getTraineeByUsername(String username,String password);
    Optional<Trainee> getTraineeByUsername(String username);
    void updateTrainee(Trainee trainee,String username, String password);
    void deleteTrainee(String username, String password);
    void deleteTrainee(String username);
    void changeTraineesPassword(String username,String oldPassword,String newPassword);
    void changeTraineeActiveStatus(String username, String password);
    void changeTraineeActiveStatus(String username, String password,boolean isActive);

}
