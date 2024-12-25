package org.gymCrm.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.config.AppConfig;
import org.gymCrm.hibernate.config.HibernateConfig;
import org.gymCrm.hibernate.model.*;
import org.gymCrm.hibernate.service.TraineeService;
import org.gymCrm.hibernate.service.TrainerService;
import org.gymCrm.hibernate.service.TrainingService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, HibernateConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        Trainee trainee = new Trainee();
        trainee.setFirstName("Tig");
        trainee.setLastName("Adamyan");
        trainee.setBirthDate(LocalDate.of(1980, 7, 24));
        trainee.setAddress(new Address("New York", "Broadway", "25 ave", "55"));
        trainee.setActive(true);
        traineeService.saveTrainee(trainee);
        System.out.println("Trainee saved successfully");

        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Smith");
        trainer.setUsername(trainer.getUsername());
        trainer.setPassword(trainer.getPassword());
        trainer.setActive(true);
        trainer.setSpecialization("Fitness Coach");
        trainerService.saveTrainer(trainer);
        System.out.println("Trainer saved successfully");

        Trainee trainee2 = new Trainee();
        trainee2.setFirstName("Lil");
        trainee2.setLastName("Adamyan");
        trainee2.setBirthDate(LocalDate.of(1985, 7, 15));
        trainee2.setAddress(new Address("Yerevan", "Abovyan", "50 ave", "7"));
        trainee2.setActive(true);
        traineeService.saveTrainee(trainee2);
        System.out.println("Trainee2 saved successfully");

        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName("Bodybuilding");

        Training training = new Training();
        training.setTrainingName("Athletics");
        training.setTrainingDate(new Date());
        training.setDuration(90);
        trainingService.createTraining(training, trainee.getUsername(), trainee.getPassword());

        training.setTrainees(Set.of(trainee));
        training.setTrainers(Set.of(trainer));

        // Fetch trainee's trainings
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "Tom";

        Optional<List<Training>> traineeTrainings = trainingService.getTraineeTrainings(
                trainee.getUsername(), trainee.getPassword(), fromDate, toDate, trainerName, trainingType
        );
        traineeTrainings.ifPresentOrElse(
                trainings -> trainings.forEach(t -> System.out.println("Trainee Training: " + t.getTrainingName())),
                () -> System.out.println("No trainings found for the trainee.")
        );

        // Fetch trainee by username
        Optional<Trainee> retrievedTrainee = traineeService.getTraineeByUsername(trainee.getUsername(), trainee.getPassword());
        retrievedTrainee.ifPresent(t -> System.out.println("Retrieved trainee: " + t));

        // Delete the trainee and observe cascade deletion

        traineeService.deleteTrainee(trainee.getUsername(), trainee.getPassword());
        System.out.println("Trainee and associated Training deleted!");

    }
    }
