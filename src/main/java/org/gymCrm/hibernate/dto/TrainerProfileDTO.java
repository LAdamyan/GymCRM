package org.gymCrm.hibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.gymCrm.hibernate.model.Trainer;

import java.util.List;

@Data
@AllArgsConstructor
public class TrainerProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeSummaryDTO> trainees;


    public TrainerProfileDTO(Trainer trainer) {
        this.username = trainer.getUsername();
        this.firstName= trainer.getFirstName();
        this.lastName =trainer.getLastName();
    }
}
