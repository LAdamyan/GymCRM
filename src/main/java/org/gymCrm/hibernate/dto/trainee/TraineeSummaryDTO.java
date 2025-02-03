package org.gymCrm.hibernate.dto.trainee;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.gymCrm.hibernate.dto.trainer.TrainerProfileDTO;

@Data
@NoArgsConstructor
public class TraineeSummaryDTO {
    private String username;
    private String firstName;
    private String lastName;

    public TraineeSummaryDTO(TrainerProfileDTO profileDTO) {
        this.username = profileDTO.getUsername();
        this.firstName = profileDTO.getFirstName();
        this.lastName = profileDTO.getLastName();
    }
}
