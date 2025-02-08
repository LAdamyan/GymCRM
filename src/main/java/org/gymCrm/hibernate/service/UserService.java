package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.model.User;

import java.util.Optional;

public interface UserService <T extends User>{

    Optional<T> findByUsername(String username);

    Optional<T> changePassword(String username, String newPassword);
}
