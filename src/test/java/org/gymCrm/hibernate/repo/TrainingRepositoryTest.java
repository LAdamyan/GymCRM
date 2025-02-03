package org.gymCrm.hibernate.repo;

import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TrainingRepositoryTest {

    @Autowired
    private TrainingRepository trainingRepository;
    private TrainingType trainingType;
    private Training training;

    @BeforeEach
    void setUp() {

        trainingType = new TrainingType();
        trainingType.setTypeName("CARDIO");

        training = new Training();
        training.setTrainingName("Morning Cardio");
        training.setTrainingType(trainingType);
        training.setTrainingDate(new Date());
        training.setDuration(60);

        trainingRepository.save(training);
    }

    @Test
    public void testFindByTrainingType() {

        List<Training> foundTrainings = trainingRepository.findByTrainingType(trainingType);

        assertFalse(foundTrainings.isEmpty());
        assertEquals(training.getTrainingName(), foundTrainings.get(0).getTrainingName());
    }

    @Test
    public void testFindTraineeTrainings() {
        List<Training> traineeTrainings = trainingRepository.findTraineeTrainings(
                "someTraumaUsername",
                new Date(System.currentTimeMillis() - 100000),
                new Date(),
                null,
                trainingType.getTypeName());

        assertNotNull(traineeTrainings);

    }
}