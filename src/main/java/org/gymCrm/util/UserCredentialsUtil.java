package org.gymCrm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserCredentialsUtil {

    private static final Map<String, Integer> usernameCounts = new HashMap<>();

    public static String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        Integer count = usernameCounts.getOrDefault(baseUsername, 0);

        String username = baseUsername;
        if (count > 0) {
            username += count;
            log.debug("Username collision detected. Generated unique username: {}", username);
        } else {
            log.debug("Generated username: {}", username);
        }
        usernameCounts.put(baseUsername, count + 1);
        return username;
    }

    public static String generatePassword() {
        String password = RandomStringUtils.randomAlphanumeric(10);
        log.debug("Generated random password.");
        return password;
    }

    public static void clearUsernameCounts() {
        usernameCounts.clear();

    }
}