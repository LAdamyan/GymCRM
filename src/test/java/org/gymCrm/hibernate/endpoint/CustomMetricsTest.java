package org.gymCrm.hibernate.endpoint;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.gymCrm.hibernate.repo.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CustomMetricsTest {
    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private Counter traineeRegistrationCounter;

    @Mock
    private Counter traineeCountCounter;

    @InjectMocks
    private CustomMetrics customMetrics;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(meterRegistry.counter("trainee_registration_count")).thenReturn(traineeRegistrationCounter);
        when(meterRegistry.counter("trainee_count")).thenReturn(traineeCountCounter);


    }





}