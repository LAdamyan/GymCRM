package org.gymCrm.hibernate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TrainerDAO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.util.UserCredentialsUtil;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainerDAOImpl implements TrainerDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public void create(Trainer trainer) {
        log.info("Saving trainer: {}", trainer);
        Session session = sessionFactory.getCurrentSession();
        try {
            String username = UserCredentialsUtil.generateUsername(trainer.getFirstName(), trainer.getLastName());

            List<Trainer> existingTrainers = session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)
                    .setParameter("username", username)
                    .list();
            if (!existingTrainers.isEmpty()) {
                Trainer existingTrainer = existingTrainers.get(0);
                trainer.setUsername(existingTrainer.getUsername());
                trainer.setPassword(existingTrainer.getPassword());
                log.info("Trainer with username '{}' already exists. Using existing username and password.", username);
            } else {
                String newPassword = UserCredentialsUtil.generatePassword();
                trainer.setPassword(newPassword);
                trainer.setUsername(username);
                log.info("Generated new username '{}' and password for the trainer.", username);
            }
            session.save(trainer);
            log.info("Trainer saved successfully with username: {}", username);
        } catch (Exception e) {
            log.error("Error while creating trainer", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void update(Trainer trainer) {
        log.info("Updating trainer: {}", trainer);
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(trainer);
        } catch (Exception e) {
            log.error("Error while updating trainee", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainer> selectByUsername(String username) {
        log.info("Fetching trainer by username: {}", username);
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainer trainer = session.createQuery("From Trainer t WHERE t.username=:username", Trainer.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.ofNullable(trainer);
        } catch (NonUniqueResultException e) {
            log.warn("Multiple trainers found with username: {}", username);
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void changeTrainersPassword(String username, String password) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainer trainer = session.createQuery("From Trainer  t WHERE t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainer != null) {
                trainer.setPassword(password);
                session.update(trainer);
            }
        } catch (Exception e) {
            log.error("Error while changing password", e);
            throw e;
        }
    }

    @Override
    public void changeTrainerActivation(String username,boolean activate) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Trainer trainer = session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainer != null && trainer.isActive() != activate) {
                trainer.setActive(activate);
                session.update(trainer);
            }
        } catch (Exception e) {
            log.error("Error while updating active status of trainer", e);
            throw e;
        }
    }

    @Override
    public Optional<List<Trainer>> getUnassignedTrainers(String traineeUsername) {
        Session session = sessionFactory.getCurrentSession();
        try {
            String hql = "SELECT t FROM Trainer t WHERE t NOT IN " +
                    "(SELECT tn.trainers FROM Trainee tn WHERE tn.username = :username)";
            List<Trainer> trainers = session.createQuery(hql, Trainer.class)
                    .setParameter("username", traineeUsername)
                    .getResultList();
            return Optional.ofNullable(trainers);
        } catch (Exception e) {
            log.error("Error while fetching unassigned trainers for trainee username: {}", traineeUsername, e);
            throw e;
        }
    }
}
