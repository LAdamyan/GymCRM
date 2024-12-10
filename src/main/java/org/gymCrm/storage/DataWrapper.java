package org.gymCrm.storage;

import lombok.Data;
import org.gymCrm.model.Trainee;
import org.gymCrm.model.Trainer;
import org.gymCrm.model.Training;

import java.util.List;

@Data
public class DataWrapper {

    private List<Trainee> trainees;
    private List<Trainer> trainers;
    private List<Training> trainings;

}
