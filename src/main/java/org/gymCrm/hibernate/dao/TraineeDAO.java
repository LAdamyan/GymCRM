package org.gymCrm.hibernate.dao;

import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {

    void create(Trainee trainee);

    void update(Trainee trainee);

    void delete(String username);

    Optional<Trainee> selectByUsername(String username);

    Optional<List<Trainee>> listAll();

    void changeTraineesPassword(String username, String password);

    void activateTrainee(String username);

    void deactivateTrainee(String username);

    void updateTraineeTrainers(String traineeUsername, List<Trainer> trainers);

}
