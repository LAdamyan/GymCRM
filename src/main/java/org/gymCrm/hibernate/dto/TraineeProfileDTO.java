package org.gymCrm.hibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.gymCrm.hibernate.model.Trainee;

import java.time.LocalDate;
import java.util.List;

@Data
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
