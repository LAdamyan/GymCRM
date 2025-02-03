package org.gymCrm.hibernate.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;
@Slf4j
@Component
public class CustomTransactionInterceptor implements HandlerInterceptor {

    private static final String TRANSACTION_ID = "transactionId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String transactionId = UUID.randomUUID().toString();
        MDC.put(TRANSACTION_ID, transactionId);
        response.addHeader(TRANSACTION_ID, transactionId);
        log.info("CustomTransactionInterceptor: Generated transactionId [{}]", transactionId);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("CustomTransactionInterceptor: Removing transactionId [{}]", MDC.get(TRANSACTION_ID));
        MDC.remove(TRANSACTION_ID);
    }
}