package org.gymCrm.service;

import org.gymCrm.model.Trainer;

public interface TrainerService {
    void saveTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Trainer getTrainerById(int id);
}
