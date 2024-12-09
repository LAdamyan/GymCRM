package org.example.dao;

import org.example.model.Trainer;

public interface TrainerDAO {
    void create(Trainer trainer);
    void update(Trainer trainer);
    Trainer selectById(int id);
}
