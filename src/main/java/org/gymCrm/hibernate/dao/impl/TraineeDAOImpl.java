package org.gymCrm.hibernate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TraineeDAO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
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
            String password = UserCredentialsUtil.generatePassword();
            trainee.setUsername(username);
            trainee.setPassword(password);
            session.save(trainee);
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
            Trainee trainee = session.get(Trainee.class, username);
            if (trainee != null) {
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
    public void activateTrainee(String username) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainee trainee = session.createQuery("FROM Trainee WHERE username = :username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainee != null) {
                trainee.setActive(true);
                session.update(trainee);
            }
        } catch (Exception e) {
            log.error("Error while updating active status of trainee", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void deactivateTrainee(String username) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainee trainee = session.createQuery("FROM Trainee WHERE username = :username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainee != null) {
                trainee.setActive(false);
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
