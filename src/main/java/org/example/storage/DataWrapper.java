package org.example.storage;

import lombok.Data;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;

import java.util.List;

@Data
public class DataWrapper {

    private List<Trainee> trainees;
    private List<Trainer> trainers;
    private List<Training> trainings;

}
