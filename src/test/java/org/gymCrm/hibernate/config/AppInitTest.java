package org.gymCrm.hibernate.config;

import org.gymCrm.hibernate.GymCrmApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppInitTest {


    private AppInit appInit;

    @BeforeEach
    void setUp() {
        appInit = new AppInit();
    }

    @Test
    void testConfigure() {
        SpringApplicationBuilder builder = mock(SpringApplicationBuilder.class);
        when(builder.sources(GymCrmApplication.class)).thenReturn(builder);

        SpringApplicationBuilder result = appInit.configure(builder);

        assertNotNull(result);
        verify(builder, times(1)).sources(GymCrmApplication.class);
    }
}