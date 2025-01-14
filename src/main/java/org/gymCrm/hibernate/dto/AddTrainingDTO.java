package org.gymCrm.hibernate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.gymCrm.hibernate.model.TrainingType;

import java.util.Date;

@Data
public class AddTrainingDTO {

    @NotBlank(message = "Trainee username is required")
    private String traineeUsername;

    @NotBlank(message = "Trainer username is required")
    private String trainerUsername;

    @NotBlank(message = "Training name is required")
    private String trainingName;

    @NotNull(message = "Training date is required")
    private Date trainingDate;

    @NotNull(message = "Training duration is required")
    private Integer duration;
}
