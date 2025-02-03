package org.gymCrm.hibernate.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(connection= EmbeddedDatabaseConnection.H2)
class TrainerRepositoryTest {

    @Autowired
    private TrainerRepository trainerRepository;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setId(1);
        trainer.setUsername("michael_j");
        trainer.setPassword("securePass123");
        trainer.setFirstName("Michael");
        trainer.setLastName("Johnson");
        trainer.setActive(true);
        trainer.setSpecialization(new TrainingType("Cardio"));

        trainerRepository.save(trainer);

    }
    @Test
    void testFindByUsername() {
        Optional<Trainer> foundTrainer = trainerRepository.findByUsername("michael_j");
        assertTrue(foundTrainer.isPresent());
        assertEquals("Michael", foundTrainer.get().getFirstName());
    }


    @Test
    void testDeleteByUsername() {
        trainerRepository.deleteByUsername("michael_j");
        Optional<Trainer> deletedTrainer = trainerRepository.findByUsername("michael_j");
        assertFalse(deletedTrainer.isPresent());
    }
    @Test
    void testSaveTrainer() {
        Trainer newTrainer = new Trainer();
        newTrainer.setId(2);
        newTrainer.setUsername("sarah_b");
        newTrainer.setPassword("strongPass456");
        newTrainer.setFirstName("Sarah");
        newTrainer.setLastName("Brown");
        newTrainer.setActive(true);
        newTrainer.setSpecialization(new TrainingType("Strength Training"));

        Trainer savedTrainer = trainerRepository.save(newTrainer);
        assertNotNull(savedTrainer);
        assertEquals("sarah_b", savedTrainer.getUsername());
    }
}