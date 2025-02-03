package org.gymCrm.hibernate.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomTransactionInterceptorTest {

    @InjectMocks
    private CustomTransactionInterceptor interceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @BeforeEach
    void setUp() {
        MDC.clear();
    }

    @Test
    void testPreHandle_ShouldGenerateTransactionId() {
        assertTrue(interceptor.preHandle(request, response, handler));

        String transactionId = MDC.get("transactionId");
        assertNotNull(transactionId);
        assertFalse(transactionId.isEmpty());

        verify(response, times(1)).addHeader(eq("transactionId"), eq(transactionId));
    }

    @Test
    void testAfterCompletion_ShouldRemoveTransactionId() {
        MDC.put("transactionId", "test-transaction-id");

        interceptor.afterCompletion(request, response, handler, null);

        assertNull(MDC.get("transactionId"));
    }
}