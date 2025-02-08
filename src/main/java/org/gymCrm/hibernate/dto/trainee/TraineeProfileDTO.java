package org.gymCrm.hibernate.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gymCrm.hibernate.dto.address.AddressDTO;
import org.gymCrm.hibernate.dto.trainer.TrainerSummaryDTO;
import org.gymCrm.hibernate.model.Trainee;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private AddressDTO address;
    private boolean isActive;
    private List<TrainerSummaryDTO> trainers;

    public TraineeProfileDTO(Trainee trainee) {
        this.username = trainee.getUsername();
        this.firstName = trainee.getFirstName();
        this.lastName = trainee.getLastName();
    }
}
