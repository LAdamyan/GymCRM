package org.gymCrm.hibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDTO {
    private String trainingName;
    private Date trainingDate;
    private String trainingType;
    private int duration;

}
