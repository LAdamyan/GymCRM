package org.gymCrm.hibernate.endpoint;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;



@Component
public class CustomDatabaseHealthIndicator implements HealthIndicator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Health health() {
        try {
            if (isDatabaseHealthy()) {
                return Health.up().withDetail("database", "Database is up and running").build();
            } else {
                return Health.down().withDetail("database", "Database is down").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail("database", "Database check failed").build();
        }
    }

    private boolean isDatabaseHealthy() {
        try {
            return entityManager.createNativeQuery("SELECT 1").getSingleResult() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
