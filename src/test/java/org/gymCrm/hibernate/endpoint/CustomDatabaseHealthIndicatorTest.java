package org.gymCrm.hibernate.endpoint;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomDatabaseHealthIndicatorTest {

    @Mock
    private EntityManager entityManager;

    private CustomDatabaseHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        healthIndicator = new CustomDatabaseHealthIndicator();
        ReflectionTestUtils.setField(healthIndicator, "entityManager", entityManager);

    }

    @Test
    void testHealth_whenDatabaseIsHealthy() {
        when(entityManager.createNativeQuery("SELECT 1")).thenReturn(mock(jakarta.persistence.Query.class));
        when(entityManager.createNativeQuery("SELECT 1").getSingleResult()).thenReturn(1);

        Health health = healthIndicator.health();

        assertEquals(Health.up().withDetail("database", "Database is up and running").build(), health);

    }

    @Test
    void testHealth_whenDatabaseIsUnhealthy() {
        when(entityManager.createNativeQuery("SELECT 1")).thenThrow(new RuntimeException("Database error"));

        Health health = healthIndicator.health();

        assertEquals(Health.down().withDetail("database", "Database is down").build(), health);

    }

    @Test
    void testHealth_whenDatabaseCheckFails() {
        when(entityManager.createNativeQuery("SELECT 1")).thenThrow(new RuntimeException("Database check failed"));

        Health health = healthIndicator.health();

        assertEquals(Health.down().withDetail("database", "Database is down").build(), health);

    }
}