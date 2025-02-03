package org.gymCrm.hibernate.endpoint;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetricsTask {

    private final CustomMetrics customMetrics;

    public MetricsTask(CustomMetrics customMetrics) {
        this.customMetrics = customMetrics;
    }

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTraineeCount() {
        long count = customMetrics.getTraineeCount();
        System.out.println("Current trainee count: " + count);
    }
}
