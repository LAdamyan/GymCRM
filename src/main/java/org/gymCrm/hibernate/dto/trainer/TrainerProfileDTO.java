package org.gymCrm.hibernate.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gymCrm.hibernate.dto.trainee.TraineeSummaryDTO;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.model.TrainingType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean isActive;
    private List<TraineeSummaryDTO> trainees;


    public TrainerProfileDTO(Trainer trainer) {
        this.username = trainer.getUsername();
        this.firstName= trainer.getFirstName();
        this.lastName =trainer.getLastName();
    }
}
