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

    public void loginFailed(String username) {
        FailedLoginAttempt attempt = attemptsCache.getOrDefault(username, new FailedLoginAttempt());
        attempt.incrementAttempts();

        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            attempt.setLockTime(LocalDateTime.now().plusSeconds(LOCK_TIME_DURATION));
            log.warn("User {} is now blocked for 5 minutes.", username);
        } else {
            log.info("Failed login attempt {} for user {}.", attempt.getAttempts(), username);
        }
        attemptsCache.put(username, attempt);
    }
    public boolean isBlocked(String username) {
        FailedLoginAttempt attempt = attemptsCache.get(username);
        if (attempt == null) return false;

        if (attempt.getLockTime() != null && LocalDateTime.now().isBefore(attempt.getLockTime())) {
            log.info("User {} is still blocked.", username);
            return true;
        }

        if (attempt.getLockTime() != null && LocalDateTime.now().isAfter(attempt.getLockTime())) {
            log.info("User {} block expired, allowing login.", username);
            attemptsCache.remove(username);
        }

        return false;
    }

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
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