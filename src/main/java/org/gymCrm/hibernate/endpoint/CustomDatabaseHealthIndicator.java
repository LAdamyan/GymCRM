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
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            return Health.up().
                    withDetail("database", "Database is up and running").build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "Database connection failed")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }


}
