package org.gymCrm.hibernate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.TrainingDAO;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainingDAOImpl implements TrainingDAO {


    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public void create(Training training) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(training);
        } catch (Exception e) {
            log.error("Error while creating training ", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Training>> selectByType(TrainingType type) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "From Training t WHERE t.trainingType = :type";
        try {
            List<Training> trainings = session.createQuery(hql, Training.class)
                    .setParameter("type", type)
                    .getResultList();
            return Optional.ofNullable(trainings);
        } catch (Exception e) {
            log.error("Error while fetching trainings by type", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public Optional<List<Training>> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT t FROM Training t " +
                "JOIN t.trainees tr " +
                "JOIN t.trainingType tt " +
                "JOIN t.trainers tn " +
                "WHERE tr.username = :username " +
                "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
                "AND tt.typeName = :typeName " +
                "AND tn.firstName = :trainerName";
        try {
            List<Training> trainings = session.createQuery(hql, Training.class)
                    .setParameter("username", username)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("trainerName", trainerName)
                    .setParameter("typeName", trainingType.getTypeName())
                    .getResultList();
            return Optional.ofNullable(trainings);
        } catch (Exception e) {
            log.error("Error while fetching trainee trainings", e);
            throw e;
        }
    }

    @Override
    public Optional<List<Training>> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT t FROM Training t " +
                "JOIN t.trainers tr " +
                "JOIN t.trainees tn " +
                "WHERE tr.username = :username " +
                "AND t.trainingDate BETWEEN :fromDate AND:toDate" +
                " AND tn.firstName =:traineeName";
        try {
            List<Training> trainings = session.createQuery(hql, Training.class)
                    .setParameter("username", username)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("traineeName", traineeName)
                    .getResultList();
            return Optional.ofNullable(trainings);
        } catch (Exception e) {
            log.error("Error while fetching trainer trainings", e);
            throw e;
        }
    }

    @Override
    public List<String> getDistinctTrainingTypes() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT DISTINCT t.trainingType.typeName FROM org.gymCrm.hibernate.model.Training t", String.class)
                .getResultList();
    }

}