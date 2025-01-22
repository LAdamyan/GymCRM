package org.gymCrm.hibernate.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gymCrm.hibernate.model.TrainingType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveTrainerDTO {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
}
