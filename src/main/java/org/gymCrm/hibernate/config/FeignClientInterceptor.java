package org.gymCrm.hibernate.config;
import lombok.extern.slf4j.Slf4j;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private String backendToken;

    public void setBackendToken(String backendToken) {
        this.backendToken = backendToken;
        log.info("[FeignClientInterceptor] Token initialized: {}", backendToken);
    }

    @Override
    public void apply(RequestTemplate template) {
        if (backendToken != null && !backendToken.isEmpty()) {
            log.info("[FeignClientInterceptor] Adding Authorization header with token...");
            template.header("Authorization", backendToken);
            log.info("[FeignClientInterceptor] Headers sent: {}", template.headers());
        } else {
            log.warn("[FeignClientInterceptor] No token found. Requests may fail.");
        }
    }
}