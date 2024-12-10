package org.gymCrm;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.gymCrm.config.AppConfig;
import org.gymCrm.model.*;
import org.gymCrm.service.TraineeService;
import org.gymCrm.service.TrainingService;
import org.gymCrm.storage.InMemoryStorage;
import org.gymCrm.util.UserCredentialsUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class Main {
    public static void main(String[] args) {
        val context = new AnnotationConfigApplicationContext(AppConfig.class);
        InMemoryStorage storage = context.getBean(InMemoryStorage.class);
        storage.getTraineeMap().forEach((k, v) -> System.out.println(k + ": " + v));
        storage.getTrainerMap().forEach((k, v) -> System.out.println(k + ": " + v));
        storage.getTrainingMap().forEach((k, v) -> System.out.println(k + ": " + v));


        TraineeService traineeService = context.getBean(TraineeService.class);

        Trainee trainee = new Trainee(LocalDate.of(1990, 1, 1), new Address("City A"));
        trainee.setId(1);
        trainee.setFirstName("Lil");
        trainee.setLastName("Adam");
        traineeService.saveTrainee(trainee);
        System.out.println("Trainee created:" + trainee);

        trainee.setId(3);
        trainee.setFirstName("UpdatedName ");
        trainee.setPassword(UserCredentialsUtil.generatePassword());
        trainee.setBirthDate(LocalDate.of(2024, 5, 6));
        traineeService.updateTrainee(trainee);
        System.out.println("Updated trainee:" + trainee);

        Trainee retrievedTrainee = traineeService.getTraineeById(1);
        retrievedTrainee.setId(2);
        retrievedTrainee.setFirstName("Tom");
        ;
        retrievedTrainee.setPassword("588888");
        traineeService.updateTrainee(retrievedTrainee);
        if (retrievedTrainee != null) {
            log.info("Retrieved Trainee: {}", retrievedTrainee);
        } else {
            log.warn("Trainee with ID {} not found.", trainee);
        }

        log.info("Updating Trainee with ID {}...", trainee.getId());
        if (retrievedTrainee != null) {
            retrievedTrainee.setLastName("UpdatedLastName");
            traineeService.updateTrainee(retrievedTrainee);
            log.info("Trainee updated: {}", retrievedTrainee);
        }

        int traineeId = 1;
        traineeService.deleteTrainee(traineeId);
        log.info("Trainee with ID {} deleted.", traineeId);

        traineeService.getAllTrainees();
        System.out.println("All trainees: " + traineeService.getAllTrainees());

        Address address2 = new Address("123 Komitas");
        Trainee trainee3 = new Trainee(
                4,
                "Alice",
                "Wonderland",
                UserCredentialsUtil.generateUsername("Alice", "Wonderland"),
                UserCredentialsUtil.generatePassword(),
                true,
                LocalDate.of(1995, 3, 15),  // birthDate
                address2
        );
        System.out.println("Trainee3  created:" + trainee3);
        traineeService.saveTrainee(trainee3);

        System.out.println("All trainees: " + traineeService.getAllTrainees());

        TrainingService trainingService = context.getBean(TrainingService.class);

        Trainee trainee4 = new Trainee(6,
                "Aren",
                "Black",
                UserCredentialsUtil.generateUsername("Aren", "Black"),
                UserCredentialsUtil.generatePassword(),
                true,
                LocalDate.of(1995, 3, 15),  // birthDate
                address2);

        Trainer trainer = new Trainer(3,
                "Mara",
                "White",
                UserCredentialsUtil.generateUsername("Mara", "White"),
                UserCredentialsUtil.generatePassword(),
                true, "Aerobics");

        Training training = new Training(trainee4, trainer,
                "bodybuilding",
                TrainingType.BODYBUILDING,
                LocalDateTime.of(2023, 12, 15, 9, 30),
                1.5);

        trainingService.createTraining(training);
        System.out.println("Training created: " + training);

        trainingService.getTrainingByType(TrainingType.BODYBUILDING).forEach(System.out::println);

        context.close();


    }
}