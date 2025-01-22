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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
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

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setId(1);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("john.doe");
        trainer.setPassword("password123");
        trainer.setActive(true);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }
    @Test
    void create_TrainerAlreadyExists() {
        String existingUsername = "john.doe";

        List<Trainer> existingTrainers = List.of(trainer);
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class))
                .thenReturn(mock(Query.class));
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)
                .setParameter("username", existingUsername))
                .thenReturn(mock(Query.class));
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)
                .setParameter("username", existingUsername)
                .list())
                .thenReturn(existingTrainers);

        trainerDAO.create(trainer);

        verify(session, never()).save(trainer);
    }
    @Test
    void create_NewTrainer() {
        String newUsername = "john.doe";

        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class))
                .thenReturn(mock(Query.class));
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)
                .setParameter("username", newUsername))
                .thenReturn(mock(Query.class));
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)
                .setParameter("username", newUsername)
                .list())
                .thenReturn(Collections.emptyList());

        trainerDAO.create(trainer);

        verify(session).save(trainer);
    }
    @Test
    void updateTrainer_Success() {
        trainerDAO.update(trainer);

        verify(session).update(trainer);
    }
    @Test
    void selectByUsername_Found() {
        String username = "john.doe";
        when(session.createQuery("From Trainer t WHERE t.username=:username", Trainer.class))
                .thenReturn(mock(Query.class));
        when(session.createQuery("From Trainer t WHERE t.username=:username", Trainer.class)
                .setParameter("username", username))
                .thenReturn(mock(Query.class));
        when(session.createQuery("From Trainer t WHERE t.username=:username", Trainer.class)
                .setParameter("username", username)
                .uniqueResult())
                .thenReturn(trainer);

        Optional<Trainer> result = trainerDAO.selectByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }
    @Test
    void selectByUsername_NotFound() {
        String username = "non.existing";

        when(session.createQuery("From Trainer t WHERE t.username=:username", Trainer.class))
                .thenReturn(mock(Query.class));
        when(session.createQuery("From Trainer t WHERE t.username=:username", Trainer.class)
                .setParameter("username", username))
                .thenReturn(mock(Query.class));
        when(session.createQuery("From Trainer t WHERE t.username=:username", Trainer.class)
                .setParameter("username", username)
                .uniqueResult())
                .thenReturn(null);

        Optional<Trainer> result = trainerDAO.selectByUsername(username);

        assertTrue(result.isEmpty());
    }
    @Test
    void changeTrainersPassword_Success() {
        String newPassword = "newPassword";

        when(session.createQuery("From Trainer  t WHERE t.username = :username", Trainer.class))
                .thenReturn(mock(Query.class));
        when(session.createQuery("From Trainer  t WHERE t.username = :username", Trainer.class)
                .setParameter("username", trainer.getUsername()))
                .thenReturn(mock(Query.class));
        when(session.createQuery("From Trainer  t WHERE t.username = :username", Trainer.class)
                .setParameter("username", trainer.getUsername())
                .uniqueResult())
                .thenReturn(trainer);

        trainerDAO.changeTrainersPassword(trainer.getUsername(), newPassword);

        verify(session).update(trainer);
        assertEquals(newPassword, trainer.getPassword());
    }

    @Test
    void changeTrainerActiveStatus_Success() {
        boolean originalStatus = trainer.isActive();

        // Mock the trainer query
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class))
                .thenReturn(mock(Query.class));
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)
                .setParameter("username", trainer.getUsername()))
                .thenReturn(mock(Query.class));
        when(session.createQuery("FROM Trainer WHERE username = :username", Trainer.class)
                .setParameter("username", trainer.getUsername())
                .uniqueResult())
                .thenReturn(trainer);

        trainerDAO.changeTrainerActiveStatus(trainer.getUsername());

        verify(session).update(trainer);
        assertEquals(!originalStatus, trainer.isActive());
    }
    @Test
    void getUnassignedTrainers_Success() {
        String traineeUsername = "trainee1";
        List<Trainer> unassignedTrainers = List.of(trainer);

        when(session.createQuery("SELECT t FROM Trainer t WHERE t.id NOT IN (SELECT tr.id FROM Trainee tn JOIN tn.trainers tr WHERE tn.username = :username)", Trainer.class))
                .thenReturn(mock(Query.class));
        when(session.createQuery("SELECT t FROM Trainer t WHERE t.id NOT IN (SELECT tr.id FROM Trainee tn JOIN tn.trainers tr WHERE tn.username = :username)", Trainer.class)
                .setParameter("username", traineeUsername))
                .thenReturn(mock(Query.class));
        when(session.createQuery("SELECT t FROM Trainer t WHERE t.id NOT IN (SELECT tr.id FROM Trainee tn JOIN tn.trainers tr WHERE tn.username = :username)", Trainer.class)
                .setParameter("username", traineeUsername)
                .getResultList())
                .thenReturn(unassignedTrainers);

        Optional<List<Trainer>> result = trainerDAO.getUnassignedTrainers(traineeUsername);

        assertTrue(result.isPresent());
        assertEquals(unassignedTrainers, result.get());
    }

    }