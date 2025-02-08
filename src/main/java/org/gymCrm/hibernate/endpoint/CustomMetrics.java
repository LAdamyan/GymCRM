package org.gymCrm.hibernate.endpoint;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.gymCrm.hibernate.repo.TraineeRepository;
import org.gymCrm.hibernate.repo.TrainingTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter traineeRegistrationCounter;
    private final Counter trainingTypesCounter;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public CustomMetrics(MeterRegistry meterRegistry,
                         TraineeRepository traineeRepository,
                         TrainingTypeRepository trainingTypeRepository) {
        this.traineeRepository = traineeRepository;
        this.trainingTypeRepository = trainingTypeRepository;

        traineeRegistrationCounter = meterRegistry.counter("trainee_registration_count");
        trainingTypesCounter = meterRegistry.counter("training_types_count");

        Gauge.builder("trainee_total_count", this, CustomMetrics::getTotalTraineeCount)
                .description("Total number of trainees in the system")
                .register(meterRegistry);

        initializeTrainingTypesCount();
    }

    public void incrementTraineeRegistration() {
        traineeRegistrationCounter.increment();
    }

    private void initializeTrainingTypesCount() {
        long trainingTypeCount = trainingTypeRepository.count();
        for (int i = 0; i < trainingTypeCount; i++) {
            trainingTypesCounter.increment();
        }
    }
    public long getTraineeRegistrationCount() {
        return (long) traineeRegistrationCounter.count();
    }

    public long getTrainingTypesCount() {
        return (long) trainingTypesCounter.count();
    }

    public long getTotalTraineeCount() {
        return traineeRepository.count();
    }
}
