package org.gymCrm.hibernate.dto.training;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gymCrm.hibernate.model.TrainingType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDTO {
    private String trainingName;
    private Date trainingDate;
    private String trainingType;
    private int duration;
    private String traineeName;

    public TrainingDTO(String trainingName, Date trainingDate, TrainingType trainingType, int duration, String traineeName) {
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingType = trainingType != null ? trainingType.getTypeName() : null;
        this.duration = duration;
        this.traineeName = traineeName;
    }
}


