package org.example.service;

import org.example.model.Trainer;

public interface TrainerService {
    void saveTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Trainer getTrainerById(int id);
}
