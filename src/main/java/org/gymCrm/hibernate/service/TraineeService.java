package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    void create(Trainee trainee);
    void update(Trainee trainee,String username, String password);
    void delete(String username,String password);
    void delete(String username);
    Optional<Trainee> selectByUsername(String username);
    List<Trainee> listAll();
    void changeTraineePassword(String username,String oldPassword, String newPassword);
    void changeTraineeActiveStatus(String username, String password,boolean isActive);
    void updateTraineeTrainers(String traineeUsername,List<Trainer>trainers);
}
