package org.gymCrm.hibernate.controller;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;

import org.gymCrm.hibernate.endpoint.CustomMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MetricsController.class)
public class MetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomMetrics customMetrics;

    @InjectMocks
    private MetricsController metricsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTrainingTypesCount_ShouldReturnCorrectCount() throws Exception {
        // Given
        long expectedTrainingTypesCount = 5L;
        when(customMetrics.getTrainingTypesCount()).thenReturn(expectedTrainingTypesCount);

        // When & Then
        mockMvc.perform(get("/metrics/training-types"))
                .andExpect(status().isOk())  // Assert HTTP status 200
                .andExpect(content().contentType("application/json"))  // Assert content type is JSON
                .andExpect(jsonPath("$").value(expectedTrainingTypesCount));  // Assert response contains the expected count
    }

    @Test
    void getTotalTraineeCount_ShouldReturnCorrectCount() throws Exception {
        // Given
        long expectedTraineeCount = 100L;
        when(customMetrics.getTotalTraineeCount()).thenReturn(expectedTraineeCount);

        // When & Then
        mockMvc.perform(get("/metrics/total-trainees"))
                .andExpect(status().isOk())  // Assert HTTP status 200
                .andExpect(content().contentType("application/json"))  // Assert content type is JSON
                .andExpect(jsonPath("$").value(expectedTraineeCount));  // Assert response contains the expected count
    }
}
