package org.gymCrm.hibernate.controller;

import org.gymCrm.hibernate.endpoint.CustomMetrics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final CustomMetrics customMetrics;

    public MetricsController(CustomMetrics customMetrics) {
        this.customMetrics = customMetrics;
    }

    @GetMapping("/trainee-registrations")
    public long getTraineeRegistrationCount() {
        return customMetrics.getTraineeRegistrationCount();
    }

    @GetMapping("/training-types")
    public long getTrainingTypesCount(){
        return customMetrics.getTrainingTypesCount();
    }

    @GetMapping("/total-trainees")
    public long getTotalTraineeCount() {
        return customMetrics.getTotalTraineeCount();
    }
}
