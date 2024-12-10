package org.gymCrm.storage;

import lombok.Data;
import org.gymCrm.model.Trainee;
import org.gymCrm.model.Trainer;
import org.gymCrm.model.Training;

import java.util.*;

@Data
public class InMemoryStorage {

    private final Map<Integer, Trainee> traineeMap = new HashMap<>();
    private final Map<Integer, Trainer> trainerMap = new HashMap<>();
    private final Map<Long, Training> trainingMap = new HashMap<>();

}