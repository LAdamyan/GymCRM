package org.gymCrm.hibernate.dao.impl;

import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Training> query;

    @InjectMocks
    private TrainingDAOImpl trainingDAO;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void testCreateTraining() {
        Training training = new Training();
        assertDoesNotThrow(() -> trainingDAO.create(training));
        verify(session).save(training);
    }

    @Test
    void testSelectByType() {
        TrainingType type = new TrainingType();
        List<Training> trainingList = Arrays.asList(new Training(), new Training());

        when(session.createQuery("From Training t WHERE t.trainingType = :type", Training.class))
                .thenReturn(query);
        when(query.setParameter("type", type)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainingList);

        Optional<List<Training>> result = trainingDAO.selectByType(type);

        verify(session).createQuery("From Training t WHERE t.trainingType = :type", Training.class);
        verify(query).setParameter("type", type);
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());


    }

    @Test
    void testGetTraineeTrainings() {
        String username = "john.doe";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "Jane Smith";
        TrainingType trainingType = new TrainingType();
        List<Training> trainingList = Arrays.asList(new Training(), new Training());

        when(session.createQuery(
                "FROM Training t WHERE t.trainee.username = :username AND t.startDate >= :fromDate AND" +
                        " t.endDate <= :toDate AND t.trainer.name = :trainerName AND t.trainingType = :trainingType",
                Training.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.setParameter("fromDate", fromDate)).thenReturn(query);
        when(query.setParameter("toDate", toDate)).thenReturn(query);
        when(query.setParameter("trainerName", trainerName)).thenReturn(query);
        when(query.setParameter("trainingType", trainingType)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainingList);

        Optional<List<Training>> result = trainingDAO.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        verify(session).createQuery(anyString(), eq(Training.class));
        verify(query).setParameter("username", username);
        verify(query).setParameter("fromDate", fromDate);
        verify(query).setParameter("toDate", toDate);
        verify(query).setParameter("trainerName", trainerName);
        verify(query).setParameter("trainingType", trainingType);
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    void getTrainerTrainings() {
        String username = "jane.smith";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "John Doe";
        List<Training> trainingList = Collections.singletonList(new Training());

        when(session.createQuery(
                "From Training t WHERE t.trainer.username = :username AND t.startDate >= :fromDate AND t.endDate <= :toDate AND t.trainee.name = :traineeName",
                Training.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.setParameter("fromDate", fromDate)).thenReturn(query);
        when(query.setParameter("toDate", toDate)).thenReturn(query);
        when(query.setParameter("traineeName", traineeName)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainingList);

        Optional<List<Training>> result = trainingDAO.getTrainerTrainings(username, fromDate, toDate, traineeName);

        verify(session).createQuery(anyString(), eq(Training.class));
        verify(query).setParameter("username", username);
        verify(query).setParameter("fromDate", fromDate);
        verify(query).setParameter("toDate", toDate);
        verify(query).setParameter("traineeName", traineeName);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }
}