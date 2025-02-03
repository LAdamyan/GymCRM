package org.gymCrm.hibernate.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppConfigTest {

    @Mock
    private CustomTransactionInterceptor customTransactionInterceptor;

    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig(customTransactionInterceptor);
    }
    @Test
    void testInterceptorRegistration() {
        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        when(registry.addInterceptor(any(HandlerInterceptor.class))).thenReturn(mock(InterceptorRegistration.class));

        appConfig.addInterceptors(registry);

        verify(registry, times(1)).addInterceptor(customTransactionInterceptor);
    }

}