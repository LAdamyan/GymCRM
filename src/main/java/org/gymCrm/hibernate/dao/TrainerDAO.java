package org.gymCrm.hibernate.dao;

import org.gymCrm.hibernate.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO {
    void create(Trainer trainer);

    void update(Trainer trainer);

    Optional<Trainer> selectByUsername(String username);

    void changeTrainersPassword(String username, String password);

    void changeTrainerActiveStatus(String username);

    Optional<List<Trainer>> getUnassignedTrainers(String traineeUsername);
}
