package org.gymCrm.dao;

import org.gymCrm.model.Training;
import org.gymCrm.model.TrainingType;

import java.util.List;

public interface TrainingDAO {

    void create(Training training);
    List<Training> selectByType(TrainingType type);
}
