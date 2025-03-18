package org.gymCrm.hibernate.client;

import org.gymCrm.hibernate.dto.TrainerWorkloadRequest;
import org.gymCrm.hibernate.dto.TrainerWorkloadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Month;
import java.time.Year;

@FeignClient(name = "trainer-workload-service", fallback = TrainerWorkloadClientFallback.class)
public interface TrainerWorkloadClient {

    @PostMapping("/api/v1/workload")
    void updateTrainerWorkload(
            @RequestBody TrainerWorkloadRequest request
    );

    @GetMapping("/api/v1/workload/{trainerUsername}/{year}/{month}")
    TrainerWorkloadResponse getTrainerWorkload(
            @PathVariable String trainerUsername,
            @PathVariable Year year,
            @PathVariable Month month
    );
}