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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeDAOImplTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TraineeDAOImpl traineeDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void testCreateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        when(session.createQuery("FROM Trainee WHERE username = :username", Trainee.class))
                .thenReturn(mock(org.hibernate.query.Query.class));
        when(session.createQuery("FROM Trainee WHERE username = :username", Trainee.class)
                .setParameter("username", "John.Doe"))
                .thenReturn(mock(org.hibernate.query.Query.class));

        traineeDAO.create(trainee);

        verify(session, times(1)).save(trainee);
        assertNotNull(trainee.getUsername());
        assertNotNull(trainee.getPassword());
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUsername("johnDoe");

        traineeDAO.update(trainee);

        verify(session).update(trainee);
    }


    @Test
    void testDeleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john.doe");

        when(session.createQuery("From Trainee t WHERE t.username=:username", Trainee.class))
                .thenReturn(mock(org.hibernate.query.Query.class));
        when(session.createQuery("From Trainee t WHERE t.username=:username", Trainee.class)
                .setParameter("username", "john.doe"))
                .thenReturn(mock(org.hibernate.query.Query.class));
        when(session.createQuery("From Trainee t WHERE t.username=:username", Trainee.class)
                .setParameter("username", "john.doe")
                .list())
                .thenReturn(List.of(trainee));

        traineeDAO.delete("john.doe");

        verify(session, times(1)).delete(trainee);
    }

    @Test
    void testSelectByUsername() {
        String username = "johndoe";
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setUsername(username);

        Query<Trainee> query = mock(Query.class);
        when(session.createQuery("From Trainee t WHERE t.username=:username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(expectedTrainee);

        Optional<Trainee> result = traineeDAO.selectByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(expectedTrainee, result.get());
        verify(query).setParameter("username", username);
    }

    @Test
    void testListAllTrainees() {
        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
        Query<Trainee> query = mock(Query.class);
        when(session.createQuery("FROM Trainee", Trainee.class)).thenReturn(query);
        when(query.list()).thenReturn(expectedTrainees);

        Optional<List<Trainee>> result = traineeDAO.listAll();

        assertTrue(result.isPresent());
        assertEquals(expectedTrainees.size(), result.get().size());
    }

    @Test
    void testChangeTraineesPassword() {
        String username = "johndoe";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);

        Query<Trainee> query = mock(Query.class);
        when(session.createQuery("FROM Trainee t WHERE t.username =: username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);

        traineeDAO.changeTraineesPassword(username, newPassword);

        assertEquals(newPassword, trainee.getPassword());
        verify(session).update(trainee);
    }

    @Test
    void changeTraineeActiveStatus_WhenTraineeFound_ShouldToggleStatus() {
        Trainee trainee = new Trainee();
        trainee.setUsername("testTrainee");
        trainee.setActive(true);

        Query<Trainee> query = mock(Query.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("FROM Trainee WHERE username = :username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", "testTrainee")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);

        traineeDAO.changeTraineeActiveStatus("testTrainee");

        assertFalse(trainee.isActive(), "Trainee should be deactivated.");
        verify(session).update(trainee);
    }
    @Test
    void changeTraineeActiveStatus_WhenTraineeNotFound_ShouldNotChangeStatus() {

        Query<Trainee> query = mock(Query.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("FROM Trainee WHERE username = :username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", "unknownUser")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        traineeDAO.changeTraineeActiveStatus("unknownUser");

        verify(session, never()).update(any(Trainee.class));
    }


    @Test
    void testUpdateTraineeTrainers_NonExistingTrainee() {
        String username = "johndoe";
        List<Trainer> newTrainers = new ArrayList<>();

        Query<Trainee> query = mock(Query.class);
        when(session.createQuery("FROM Trainee WHERE username =: username", Trainee.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        traineeDAO.updateTraineeTrainers(username, newTrainers);

        verify(session, never()).update(any(Trainee.class));
    }

    @Test
    void testUpdateTraineeTrainers_ThrowsException() {
        String username = "johndoe";
        List<Trainer> newTrainers = new ArrayList<>();

        Query<Trainee> query = mock(Query.class);
        when(session.createQuery("FROM Trainee WHERE username =:username", Trainee.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            traineeDAO.updateTraineeTrainers(username, newTrainers);
        });
    }
}