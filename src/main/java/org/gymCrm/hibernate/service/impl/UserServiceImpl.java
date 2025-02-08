package org.gymCrm.hibernate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.exception.AuthenticationException;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.repo.UserRepository;
import org.gymCrm.hibernate.util.AuthenticationService;
import org.gymCrm.hibernate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) throws Exception {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Optional<User> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.debug("User found with username: {}", username);
        } else {
            log.error("User not found with username: {}", username);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                userRepository.changePassword(username, encodedNewPassword);
                return true;
            } else {
                log.error("Authentication failed for user: {}", username);
                throw new AuthenticationException("Authentication failed for user: " + username);
            }
        } else {
            log.error("User not found: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }


}
