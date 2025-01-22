package org.gymCrm.hibernate.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import javax.xml.transform.Source;
import java.util.List;

@Data
public class UpdateTraineeTrainersDTO{
    @NotBlank(message = "Trainee username is required")
    private String traineeUsername;

    @NotEmpty(message = "Trainer list cannot be empty")
    private List<String> trainerUsernames;
}
