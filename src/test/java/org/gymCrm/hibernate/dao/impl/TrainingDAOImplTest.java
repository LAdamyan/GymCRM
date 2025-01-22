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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingDAOImplTest {

    @InjectMocks
    private TrainingDAOImpl trainingDAO;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }
    @Test
    public void testCreateTraining() {
        Training training = new Training();

        trainingDAO.create(training);

        verify(session, times(1)).save(training);
    }
    @Test
    public void testSelectByType() {
        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName("Yoga");

        Training training = new Training();
        training.setTrainingType(trainingType);

        List<Training> trainingList = new ArrayList<>();
        trainingList.add(training);

        Query<Training> mockQuery = mock(Query.class);
        when(session.createQuery("From Training t WHERE t.trainingType = :type", Training.class)).thenReturn(mockQuery);

        when(mockQuery.setParameter("type", trainingType)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(trainingList);

        Optional<List<Training>> result = trainingDAO.selectByType(trainingType);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals(trainingType, result.get().get(0).getTrainingType());
    }
    @Test
    public void testGetTraineeTrainings() {
        String username = "trainee1";
        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName("Yoga");

        List<Training> trainingList = new ArrayList<>();

        Training training = new Training();
        training.setTrainingName("Morning Yoga");
        training.setTrainingType(trainingType);
        trainingList.add(training);

        Query<Training> mockQuery = mock(Query.class);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(trainingList);

        Optional<List<Training>> result = trainingDAO.getTraineeTrainings(username, null, null, null, trainingType);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("Morning Yoga", result.get().get(0).getTrainingName());
    }

    @Test
    public void testGetTrainerTrainings() {
        String username = "trainer1";
        String traineeName = "trainee1";

        List<Training> trainingList = new ArrayList<>();

        Training training = new Training();
        training.setTrainingName("Evening Pilates");
        trainingList.add(training);

        Query<Training> mockQuery = mock(Query.class);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(trainingList);

        Optional<List<Training>> result = trainingDAO.getTrainerTrainings(username, null, null, traineeName);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("Evening Pilates", result.get().get(0).getTrainingName());
    }
    }