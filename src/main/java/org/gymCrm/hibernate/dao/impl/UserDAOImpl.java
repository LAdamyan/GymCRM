package org.gymCrm.hibernate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.UserDAO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserDAOImpl<T extends User> implements UserDAO<T> {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    @Override
    public Optional<T> findByUsername(String username) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String hql = "FROM User WHERE username = :username";
            List<User> users = session.createQuery(hql, User.class)
                    .setParameter("username", username)
                    .setMaxResults(1)
                    .list();
            log.debug("Query result for username '{}': {}", username, users);
            return users.isEmpty() ? Optional.empty() : Optional.of((T) users.get(0));
        } catch (Exception e) {
            log.error("Error fetching user by username: {}", username, e);
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<User> changePassword(String username, String newPassword) {
        Session session = sessionFactory.getCurrentSession();
        try {
            User user = session.createQuery("FROM User u WHERE u.username=:username", User.class)
                    .setParameter("username", username)
                    .setMaxResults(1)
                    .uniqueResult();
            if (user != null) {
                user.setPassword(newPassword);
                session.update(user);
                return Optional.of(user);
            }else{
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error while changing password", e);
            throw e;
        }
    }


}