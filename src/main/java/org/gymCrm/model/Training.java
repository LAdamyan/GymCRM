package org.gymCrm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Training {

    private Trainee trainee;
    private Trainer trainer;
    @JsonProperty(value = "trainingName")
    private String trainingName;
    private TrainingType type;
    @JsonProperty(value = "trainingDate")
    private LocalDateTime trainingDate;
    @JsonProperty(value = "duration")
    private double duration;

}