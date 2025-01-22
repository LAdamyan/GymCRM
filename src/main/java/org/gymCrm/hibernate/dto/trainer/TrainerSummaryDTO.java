package org.gymCrm.hibernate.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.gymCrm.hibernate.model.TrainingType;

@Data
@AllArgsConstructor
public class TrainerSummaryDTO {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
}
