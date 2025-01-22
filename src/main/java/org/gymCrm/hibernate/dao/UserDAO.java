package org.gymCrm.hibernate.dao;

import org.gymCrm.hibernate.model.User;

import java.util.Optional;

public interface UserDAO <T extends User> {

    Optional<T> findByUsername(String username);

    Optional<User> changePassword(String username, String newPassword);

}