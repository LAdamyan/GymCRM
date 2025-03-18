package org.gymCrm.hibernate.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class TrainerWorkloadRequest {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private int trainingDuration;
    private String actionType; // ADD or DELETE

    public TrainerWorkloadRequest(String trainerUsername, String trainerFirstName, String trainerLastName,
                                  boolean isActive, LocalDate trainingDate, int trainingDuration, String actionType) {
        this.trainerUsername = trainerUsername;
        this.trainerFirstName = trainerFirstName;
        this.trainerLastName = trainerLastName;
        this.isActive = isActive;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.actionType = actionType;
    }
}


