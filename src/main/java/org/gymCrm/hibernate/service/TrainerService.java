package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.dto.trainer.UpdateTrainerDTO;
import org.gymCrm.hibernate.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    void create(Trainer trainer);
    void update(Trainer trainer,String username,String password);
    Optional<Trainer> updateTrainer(String username, UpdateTrainerDTO updateDTO);
    Optional<Trainer> selectByUsername(String username);
    void changeTrainersPassword(String username, String oldPassword,String newPassword);
    void changeTrainerActiveStatus(String username,String password,boolean isActive);
    Optional<List<Trainer>> getUnassignedTrainers(String username);
    Optional<List<Trainer>> getUnassignedTrainers(String traineeUsername, String username, String password);
}
