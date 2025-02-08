package org.gymCrm.hibernate.dto.training;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TraineeTrainingResponse {
    private String trainingName;
    private Date trainingDate;
    private String trainingType;
    private int trainingDuration;
    private String trainerName;
}
