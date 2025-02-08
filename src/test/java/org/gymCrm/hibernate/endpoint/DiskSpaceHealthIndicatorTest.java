package org.gymCrm.hibernate.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DiskSpaceHealthIndicatorTest {

    private File mockFile;
    private DiskSpaceHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        mockFile = Mockito.mock(File.class);
        healthIndicator = new DiskSpaceHealthIndicator(mockFile);
    }

    @Test
    void testHealth_WhenDiskSpaceIsLow_ShouldReturnDown() {
        Mockito.when(mockFile.getFreeSpace()).thenReturn(50 * 1024 * 1024L); // 50MB

        Health health = healthIndicator.health();
        assertEquals("DOWN", health.getStatus().getCode());
        assertTrue(health.getDetails().containsKey("error"));
        assertEquals("Low disk space", health.getDetails().get("error"));
    }

    @Test
    void testHealth_WhenDiskSpaceIsSufficient_ShouldReturnUp() {
        Mockito.when(mockFile.getFreeSpace()).thenReturn(200 * 1024 * 1024L); // 200MB

        Health health = healthIndicator.health();
        assertEquals("UP", health.getStatus().getCode());
        assertTrue(health.getDetails().containsKey("free_space"));
    }

}