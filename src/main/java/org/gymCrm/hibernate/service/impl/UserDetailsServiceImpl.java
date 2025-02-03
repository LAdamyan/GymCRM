package org.gymCrm.hibernate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.repo.UserRepository;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Primary
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl <T extends User> implements UserDetailsService<T> {

    private final UserRepository<T> userRepository;


    @Override
    public boolean authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user ->user.getPassword().equals(password))
                .orElse(false);
    }
}
