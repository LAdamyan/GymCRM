package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    boolean changePassword(String username, String oldPassword, String newPassword);
}
