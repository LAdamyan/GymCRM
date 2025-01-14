package org.gymCrm.hibernate.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@WebFilter("/*")
public class LoggingFilter implements javax.servlet.Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, javax.servlet.ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String transactionId = MDC.get("transactionId");
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();

        log.info(("[{}] Incoming request: {} {}"), transactionId, method, uri);

        chain.doFilter(request, response);

        int status = httpResponse.getStatus();
        log.info("[{}] Response status: {}", transactionId, status);
    }
}
