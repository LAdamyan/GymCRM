package org.gymCrm.hibernate.config.jwt;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Service
public class BruteForceProtectionService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 5 * 60;
    private final Map<String, FailedLoginAttempt> attemptsCache = new ConcurrentHashMap<>();


    public void loginFailed(String identifier) {
        FailedLoginAttempt attempt = attemptsCache.getOrDefault(identifier, new FailedLoginAttempt());
        attempt.incrementAttempts();

        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            attempt.setLockTime(LocalDateTime.now().plusSeconds(LOCK_TIME_DURATION));
            log.warn("User/IP {} is now blocked for 5 minutes.", identifier);
        } else {
            log.info("Failed login attempt {} for user/IP {}.", attempt.getAttempts(), identifier);
        }
        attemptsCache.put(identifier, attempt);
    }
    public boolean isBlocked(String identifier) {
        FailedLoginAttempt attempt = attemptsCache.get(identifier);
        if (attempt == null) return false;

        if (attempt.getLockTime() != null && LocalDateTime.now().isBefore(attempt.getLockTime())) {
            log.info("User {} is still blocked.", identifier);
            return true;
        }

        if (attempt.getLockTime() != null && LocalDateTime.now().isAfter(attempt.getLockTime())) {
            log.info("User {} block expired, allowing login.", identifier);
            attemptsCache.remove(identifier);
        }

        return false;
    }

    public void loginSucceeded(String identifier) {
        attemptsCache.remove(identifier);
    }

    @Getter
    private static class FailedLoginAttempt {
        private int attempts;
        @Setter
        private LocalDateTime lockTime;

        public void incrementAttempts() {
            this.attempts++;
        }
    }
}