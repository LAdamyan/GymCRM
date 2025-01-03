package org.gymCrm.hibernate.dao.impl;

import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TrainerDAOImpl trainerDAO;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void testCreateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Lil");
        trainer.setLastName("Adam");

        trainerDAO.create(trainer);

        verify(session).save(trainer);
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Lus");

        trainerDAO.update(trainer);

        verify(session).update(trainer);
    }

    @Test
    void testSelectByUsername() {
        String username = "johndoe";
        Trainer expectedTrainer = new Trainer();
        expectedTrainer.setUsername(username);

        Query<Trainer> query = mock(Query.class);
        when(session.createQuery("From Trainer t WHERE t.username=:username", Trainer.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(expectedTrainer);

        Optional<Trainer> result = trainerDAO.selectByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(expectedTrainer, result.get());
        verify(query).setParameter("username", username);
    }

    @Test
    void testChangeTrainersPassword() {
        String username = "johndoe";
        String newPassword = "newPassword";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);

        Query<Trainer> query = mock(Query.class);
        when(session.createQuery("From Trainer  t WHERE t.username = :username", Trainer.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainer);

        trainerDAO.changeTrainersPassword(username, newPassword);

        assertEquals(newPassword, trainer.getPassword());
        verify(session).update(trainer);
    }


//    @Test
//    void testGetUnassignedTrainers() {
//        String traineeUsername = "johndoe";
//        List<Trainer> expectedTrainers = new ArrayList<>();
//        expectedTrainers.add(new Trainer("Trainer1"));
//        expectedTrainers.add(new Trainer("Trainer2"));
//
//        String hql = "SELECT t FROM Trainer t WHERE t NOT IN " +
//                "(SELECT tn.trainers FROM Trainee tn WHERE tn.username = :username)";
//
//        Query<Trainer> query = mock(Query.class);
//        when(session.createQuery(hql, Trainer.class)).thenReturn(query);
//        when(query.setParameter("username", traineeUsername)).thenReturn(query);
//        when(query.getResultList()).thenReturn(expectedTrainers);
//
//        Optional<List<Trainer>> actualTrainers = trainerDAO.getUnassignedTrainers(traineeUsername);
//
//        assertTrue(actualTrainers.isPresent());
//        assertEquals(expectedTrainers, actualTrainers.get());
//        verify(query).setParameter("username", traineeUsername);
//    }

    @Test
    void testChangeTrainerActiveStatus_WhenTraineeFound_ShouldToggleStatus() {
        {
            Trainer trainer = new Trainer();
            trainer.setUsername("testTrainer");
            trainer.setActive(true);

            Query<Trainer> query = mock(Query.class);
            when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class))
                    .thenReturn(query);
            when(query.setParameter("username", "testTrainer")).thenReturn(query);
            when(query.uniqueResult()).thenReturn(trainer);

            trainerDAO.changeTrainerActiveStatus("testTrainer");

            assertFalse(trainer.isActive(), "Trainer should be activated");
            verify(session).update(trainer);
        }
    }
    @Test
    void changeTrainerActiveStatus_WhenTrainerNotFound_ShouldNotChangeStatus() {

        Query<Trainer> query = mock(Query.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)).thenReturn(query);
        when(query.setParameter("username", "unknownUser")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        trainerDAO.changeTrainerActiveStatus("unknownUser");

        verify(session, never()).update(any(Trainer.class));
    }

    @Test
    void testGetUnassignedTrainers_NoTrainersFound() {
        String traineeUsername = "johndoe";

        String hql = "SELECT t FROM Trainer t WHERE t NOT IN " +
                "(SELECT tn.trainers FROM Trainee tn WHERE tn.username = :username)";

        Query<Trainer> query = mock(Query.class);
        when(session.createQuery(hql, Trainer.class)).thenReturn(query);
        when(query.setParameter("username", traineeUsername)).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        Optional<List<Trainer>> actualTrainers = trainerDAO.getUnassignedTrainers(traineeUsername);

        assertTrue(actualTrainers.get().isEmpty());

    }
}