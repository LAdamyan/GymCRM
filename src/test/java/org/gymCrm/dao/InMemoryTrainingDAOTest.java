package org.gymCrm.dao;

import org.gymCrm.dao.impl.InMemoryTrainingDAO;
import org.gymCrm.model.*;
import org.gymCrm.util.UserCredentialsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTrainingDAOTest {

    @Test
    public void testCreateAndSelectByType() {
        Map<Long, Training> trainingMap = new HashMap<>();

        TrainingDAO trainingDAO = new InMemoryTrainingDAO(trainingMap);

        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setFirstName("Trainee First");
        trainee.setLastName("Trainee Last");
        trainee.setUsername(UserCredentialsUtil.generateUsername("Trainee First", "Trainee Last"));
        trainee.setPassword(UserCredentialsUtil.generatePassword());
        trainee.setActive(true);
        trainee.setBirthDate(LocalDate.from(LocalDateTime.of(1990, 5, 15, 0, 0)));
        trainee.setAddress(new Address("City", "Street", "Building", "123"));

        Trainer trainer = new Trainer();
        trainer.setId(2);
        trainer.setFirstName("Trainer First");
        trainer.setLastName("Trainer Last");
        trainer.setUsername(UserCredentialsUtil.generateUsername("Trainer First", "Trainer Last"));
        trainer.setPassword(UserCredentialsUtil.generatePassword());
        trainer.setActive(true);
        trainer.setSpecialization("Fitness");

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName("Training Name");
        training.setType(TrainingType.AEROBICS);
        training.setTrainingDate(LocalDateTime.now());
        training.setDuration(1.5);

        trainingDAO.create(training);

        List<Training> trainingsByType = trainingDAO.selectByType(TrainingType.AEROBICS);

        Assertions.assertEquals(1, trainingsByType.size());
        Assertions.assertEquals(training, trainingsByType.get(0));
    }
}
