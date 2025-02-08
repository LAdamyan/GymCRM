package org.gymCrm.hibernate.endpoint;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.io.File;

public class DiskSpaceHealthIndicator implements HealthIndicator {

    private static final long THRESHOLD = 100 * 1024 * 1024;
    private final File diskPartition;

    public DiskSpaceHealthIndicator() {
        this.diskPartition = new File("/");
    }
    public DiskSpaceHealthIndicator(File diskPartition) {
        this.diskPartition = diskPartition;
    }

    @Override
    public Health health() {
        long freeSpace = diskPartition.getFreeSpace();

        if (freeSpace < THRESHOLD) {
            return Health.down()
                    .withDetail("error", "Low disk space")
                    .withDetail("free_space", freeSpace / (1024 * 1024) + " MB")
                    .build();
        }

        return Health.up()
                .withDetail("free_space", freeSpace / (1024 * 1024) + " MB")
                .build();
    }

}