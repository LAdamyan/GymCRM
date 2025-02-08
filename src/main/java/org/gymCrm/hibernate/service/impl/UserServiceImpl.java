package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.repo.UserRepository;
import org.gymCrm.hibernate.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public  class UserServiceImpl<T extends User> implements UserService<T> {

    private final UserRepository<T> userRepository;

    public UserServiceImpl(UserRepository<T> userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Optional<T> findByUsername(String username) {
        try {
            Optional<T> user = userRepository.findByUsername(username);
            log.debug("Query result for username '{}': {}", username, user.orElse(null));
            return user;
        } catch (Exception e) {
            log.error("Error fetching user by username: {}", username, e);
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<T> changePassword(String username, String newPassword) {
        try {
            Optional<T> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                T user = userOptional.get();
                user.setPassword(newPassword);
                userRepository.save(user);
                log.info("Password updated successfully for username: {}", username);
                return Optional.of(user);
            } else {
                log.warn("User with username '{}' not found", username);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error while changing password for username: {}", username, e);
            throw e;
        }
    }
}
