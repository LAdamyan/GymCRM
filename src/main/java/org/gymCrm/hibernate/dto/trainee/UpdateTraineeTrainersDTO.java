package org.gymCrm.hibernate.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTraineeTrainersDTO{
    @NotBlank(message = "Trainee username is required")
    private String traineeUsername;

    @NotEmpty(message = "Trainer list cannot be empty")
    private List<String> trainerUsernames;

    public UpdateTraineeTrainersDTO() {}

    public UpdateTraineeTrainersDTO(String traineeUsername, List<String> trainerUsernames) {
        this.traineeUsername = traineeUsername;
        this.trainerUsernames = trainerUsernames;
    }

    public UpdateTraineeTrainersDTO(List<String> trainerUsernames) {
        this.trainerUsernames = trainerUsernames;
    }
}
