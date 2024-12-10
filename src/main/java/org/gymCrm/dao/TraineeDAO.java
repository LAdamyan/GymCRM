package org.gymCrm.dao;

import org.gymCrm.model.Trainee;

import java.util.List;

public interface TraineeDAO {

    void create(Trainee trainee);
    void update(Trainee trainee);
    void delete(int id);
    Trainee selectById(int id);
    List<Trainee> listAll();

}
