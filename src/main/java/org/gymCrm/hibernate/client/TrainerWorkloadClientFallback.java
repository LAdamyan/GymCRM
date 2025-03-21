package org.gymCrm.hibernate.client;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.TrainerWorkloadRequest;
import org.gymCrm.hibernate.dto.TrainerWorkloadResponse;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.time.Year;
@Slf4j
@Component
public class TrainerWorkloadClientFallback implements TrainerWorkloadClient {

    @Override
    public void updateTrainerWorkload(TrainerWorkloadRequest request) {
        log.error("Fallback triggered: Unable to update trainer workload. Request: {}", request);
    }

    @Override
    public TrainerWorkloadResponse getTrainerWorkload(String trainerUsername, Year year, Month month) {
        log.error("Fallback triggered: Unable to fetch trainer workload for username: {}, year: {}, month: {}",
                trainerUsername, year, month);

        // Build a default fallback response with zero hours
        return TrainerWorkloadResponse.builder()
                .trainerUsername(trainerUsername)
                .year(year)
                .month(month)
                .totalHours(0)
                .build();
    }
}