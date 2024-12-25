package org.gymCrm.hibernate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TraineeDAO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TraineeDAOImpl implements TraineeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public void create(Trainee trainee) {
        log.info("Saving trainee: {}", trainee);
        Session session = sessionFactory.getCurrentSession();
        try {
            String username = UserCredentialsUtil.generateUsername(trainee.getFirstName(), trainee.getLastName());
            List<Trainee> existingTrainees  = session.createQuery("FROM Trainee WHERE username = :username", Trainee.class)
                    .setParameter("username", username)
                    .list();
            if (!existingTrainees.isEmpty()) {
                Trainee existingTrainee = existingTrainees.get(0);
                trainee.setUsername(existingTrainee.getUsername());
                trainee.setPassword(existingTrainee.getPassword());
                log.info("Trainee with username '{}' already exists. Using existing username and password.", username);
            } else {
                String newPassword = UserCredentialsUtil.generatePassword();
                trainee.setPassword(newPassword);
                trainee.setUsername(username);
                log.info("Generated new username '{}' and password for the trainee.",username);
            }
            session.save(trainee);
            log.info("Trainee saved successfully with username: {}",username);
        } catch (Exception e) {
            log.error("Error while creating trainee", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void update(Trainee trainee) {
        log.info("Updating trainee: {}", trainee);
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(trainee);
        } catch (Exception e) {
            log.error("Error while updating trainee", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void delete(String username) {
        log.info("Deleting trainee: {}", username);
        Session session = sessionFactory.getCurrentSession();
        try {
            List<Trainee> trainees = session.createQuery("From Trainee t WHERE t.username=:username", Trainee.class)
                    .setParameter("username", username)
                    .list();
            if (!trainees.isEmpty()) {
                Trainee trainee = trainees.get(0);
                session.delete(trainee);
            }
        } catch (Exception e) {
            log.error("Error while deleting trainee", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainee> selectByUsername(String username) {
        log.info("Fetching trainee by username: {}", username);
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainee trainee = session.createQuery("From Trainee t WHERE t.username=:username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.ofNullable(trainee);
        } catch (NonUniqueResultException e) {
            log.warn("Multiple trainees found with username: {}", username);
            return Optional.empty();
        }
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<List<Trainee>> listAll() {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.createQuery("FROM Trainee", Trainee.class).list());
    }

    @Transactional
    @Override
    public void changeTraineesPassword(String username, String newPassword) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainee trainee = session.createQuery("FROM Trainee t WHERE t.username =: username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainee != null) {
                trainee.setPassword(newPassword);
                session.update(trainee);
            }
        } catch (Exception e) {
            log.error("Error while changing password", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void changeTraineeActivation(String username,boolean activate) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainee trainee = session.createQuery("FROM Trainee WHERE username = :username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainee != null && trainee.isActive() != activate) {
                    trainee.setActive(activate);
                    session.update(trainee);
            }
        } catch (Exception e) {
            log.error("Error while updating active status of trainee", e);
            throw e;
        }
    }


    @Transactional
    @Override
    public void updateTraineeTrainers(String traineeUsername, List<Trainer> trainers) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainee trainee = session.createQuery("FROM Trainee WHERE username =: username", Trainee.class)
                    .setParameter("username", traineeUsername)
                    .uniqueResult();
            if (trainee != null) {
                trainee.setTrainers(new HashSet<>(trainers));
                session.update(trainee);
            }
        } catch (Exception e) {
            log.error("Error while updating trainee " + traineeUsername + " trainers", e);
            throw e;
        }
    }
}
