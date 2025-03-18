package org.gymCrm.hibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.time.Year;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkloadResponse {
    private String trainerUsername;
    private Year year;
    private Month month;
    private int totalHours;
}
