package org.gymCrm.hibernate.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gymCrm.hibernate.dto.training.TrainingTypeDTO;
import org.gymCrm.hibernate.model.TrainingType;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainerDTO {

    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Specialization is required")
    private TrainingType specialization;


}
