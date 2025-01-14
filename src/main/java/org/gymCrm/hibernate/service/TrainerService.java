package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.dto.UpdateTrainerDTO;
import org.gymCrm.hibernate.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    void saveTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer, String username, String password);
    void updateTrainer(Trainer trainer, String username);
    Optional<Trainer> updateTrainer(String username, UpdateTrainerDTO updateDTO);
    Optional<Trainer> getTrainerByUsername(String username, String password);
    Optional<Trainer> getTrainerByUsername(String username);
    void changeTrainersPassword(String username,String oldPassword, String newPassword);
    void changeTrainerActiveStatus(String username, String password);
    void changeTrainerActiveStatus(String username, String password,boolean isActive);
    Optional <List<Trainer>>getUnassignedTrainers(String username, String password,String TraineeUsername);
}
