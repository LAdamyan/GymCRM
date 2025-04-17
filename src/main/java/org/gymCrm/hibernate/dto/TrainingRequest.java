package org.gymCrm.hibernate.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class TrainingRequest {
        private String trainerUsername;        // Unique Trainer identifier
        private String trainerFirstName;       // Trainer's first name
        private String trainerLastName;        // Trainer's last name
        private boolean isActive;              // Whether the trainer is currently active

        @JsonFormat(pattern = "yyyy-MM-dd")    // Standard date format
        private LocalDate trainingDate;        // Date of training session

        private int trainingDuration;
    }
