package org.gymCrm.hibernate.config.jwt.backendJwt;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.config.FeignClientInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class StartupTokenGenerator {
    private final BackendJwtUtil backendJwtUtil;
    private final FeignClientInterceptor feignClientInterceptor;

    public StartupTokenGenerator(BackendJwtUtil backendJwtUtil, FeignClientInterceptor feignClientInterceptor) {
        this.backendJwtUtil = backendJwtUtil;
        this.feignClientInterceptor = feignClientInterceptor;
    }

    @PostConstruct
    public void init() {
        log.info("Generating backend JWT token for Feign client...");
        String backendToken = "Bearer " + backendJwtUtil.generateBackendToken();
        feignClientInterceptor.setBackendToken(backendToken);
        log.info("Backend JWT Token set successfully for Feign client.");
    }
}
