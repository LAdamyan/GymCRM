package org.gymCrm.hibernate.repo;

import org.gymCrm.hibernate.model.Address;
import org.gymCrm.hibernate.model.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(connection= EmbeddedDatabaseConnection.H2)
class TraineeRepositoryTest {

    @Autowired
    private TraineeRepository traineeRepository;

    @Test
    public void testSaveAndFindByUsername() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("Lil");
        trainee.setLastName("Adamyan");
        trainee.setUsername("Lil.Adamyan");
        trainee.setPassword("password123");
        trainee.setBirthDate(LocalDate.of(2000, 1, 1));
        trainee.setAddress(new Address("Test City", "Main St", "1", "A"));
        trainee.setActive(true);

        traineeRepository.save(trainee);

        Optional<Trainee> foundTrainee = traineeRepository.findByUsername("Lil.Adamyan");
        assertTrue(foundTrainee.isPresent());
        assertEquals("Lil", foundTrainee.get().getFirstName());
        assertEquals("Adamyan", foundTrainee.get().getLastName());
        assertEquals("Lil.Adamyan", foundTrainee.get().getUsername());
    }

    @Test
    void testDeleteByUsername() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("Jane");
        trainee.setLastName("Doe");
        trainee.setUsername("jane_doe");
        trainee.setPassword("xxxx");
        trainee.setBirthDate(LocalDate.of(2000, 1, 1));
        trainee.setAddress(new Address("Test City", "Main St", "1", "A"));
        traineeRepository.save(trainee);

        traineeRepository.deleteByUsername("jane_doe");
        Optional<Trainee> retrievedTrainee = traineeRepository.findByUsername("jane_doe");

        assertFalse(retrievedTrainee.isPresent());
    }

    @Test
    void testFindByUsernameNotFound() {

        Optional<Trainee> retrievedTrainee = traineeRepository.findByUsername("nonexistent_user");

        assertFalse(retrievedTrainee.isPresent());
    }


}