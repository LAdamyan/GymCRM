package org.gymCrm.hibernate.config;
import lombok.extern.slf4j.Slf4j;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.gymCrm.hibernate.config.jwt.backendJwt.BackendJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class FeignClientInterceptor implements RequestInterceptor {
    private final BackendJwtUtil backendJwtUtil;

    public FeignClientInterceptor(BackendJwtUtil backendJwtUtil) {
        this.backendJwtUtil = backendJwtUtil;
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = "Bearer " + backendJwtUtil.generateBackendToken();
        template.header("Authorization", token);
        log.info("Feign Client Token: {}", token); // Log the token for debugging
    }
}