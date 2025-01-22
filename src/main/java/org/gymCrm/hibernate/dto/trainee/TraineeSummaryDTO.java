package org.gymCrm.hibernate.dto.trainee;

import lombok.Data;
import org.gymCrm.hibernate.dto.trainer.TrainerProfileDTO;

@Data
public class TraineeSummaryDTO {
    private String username;
    private String firstName;
    private String lastName;

    public TraineeSummaryDTO(TrainerProfileDTO profileDTO) {
    }
}
