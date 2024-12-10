package org.gymCrm.dao;

import org.gymCrm.model.Trainer;

public interface TrainerDAO {
    void create(Trainer trainer);
    void update(Trainer trainer);
    Trainer selectById(int id);
}
