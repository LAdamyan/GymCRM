package org.gymCrm.hibernate.endpoint;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.gymCrm.hibernate.repo.TraineeRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter traineeRegistrationCounter;
    private final Counter traineeCountCounter;
    private final TraineeRepository traineeRepository;

    public CustomMetrics(MeterRegistry meterRegistry, TraineeRepository traineeRepository) {
        traineeRegistrationCounter = meterRegistry.counter("trainee_registration_count");
        traineeCountCounter = meterRegistry.counter("trainee_count");
        this.traineeRepository = traineeRepository;
        initializeTraineeCount();
    }

    private void initializeTraineeCount() {
        long traineeCount = traineeRepository.count();
        for (int i = 0; i < traineeCount; i++) {
            traineeCountCounter.increment();
        }
        System.out.println("Initialized trainee count to: " + traineeCount);
    }

    public void incrementTraineeRegistration() {
        traineeRegistrationCounter.increment();
        traineeCountCounter.increment();
        System.out.println("Trainee registration count incremented.");
    }

    public long getTraineeCount() {
        return (long) traineeCountCounter.count();
    }
}
