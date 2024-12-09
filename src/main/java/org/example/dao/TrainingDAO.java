package org.example.dao;

import org.example.model.Training;
import org.example.model.TrainingType;

import java.util.List;

public interface TrainingDAO {

    void create(Training training);
    List<Training> selectByType(TrainingType type);
}
