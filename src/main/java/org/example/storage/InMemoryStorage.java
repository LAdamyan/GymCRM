package org.example.storage;

import lombok.Data;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;

import java.util.*;

@Data
public class InMemoryStorage {

    private final Map<String, Trainee> traineeMap = new HashMap<>();
    private final Map<String, Trainer> trainerMap = new HashMap<>();
    private final Map<String, Training> trainingMap = new HashMap<>();

}