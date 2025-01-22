package org.gymCrm.hibernate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.UserDAO;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl <T extends User> implements UserDetailsService<T> {

    private final UserDAO <T>userDAO;


    @Override
    public boolean authenticate(String username, String password) {
        return userDAO.findByUsername(username)
                .map(user ->user.getPassword().equals(password))
                .orElse(false);
    }



}