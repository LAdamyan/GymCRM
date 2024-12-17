package org.gymCrm.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.config.AppConfig;
import org.gymCrm.hibernate.config.HibernateConfig;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.service.TraineeService;
import org.gymCrm.hibernate.service.TrainerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

@Slf4j
public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, HibernateConfig.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);

        Trainee trainee1 = new Trainee();
        trainee1.setFirstName("Lil");
        trainee1.setLastName("Adamyan");
        trainee1.setPassword(trainee1.getPassword());
        trainee1.setUsername(trainee1.getUsername());
        trainee1.setBirthDate(LocalDate.parse("2020-05-06"));
        traineeService.saveTrainee(trainee1);
        log.info("Saved trainee1: {}", trainee1);

        Trainee trainee2 = new Trainee();
        trainee2.setFirstName("Lus");
        trainee2.setLastName("Adamyan");
        trainee2.setPassword(trainee2.getPassword());
        trainee2.setUsername(trainee2.getUsername());
        trainee2.setBirthDate(LocalDate.parse("2024-01-01"));
        traineeService.saveTrainee(trainee2);
        log.info("Saved trainee2: {}", trainee2);

        Trainee trainee3 = new Trainee();
        trainee3.setFirstName("Lil");
        trainee3.setLastName("Adamyan");
        trainee3.setPassword(trainee3.getPassword());
        trainee3.setUsername(trainee3.getUsername());
        trainee3.setBirthDate(LocalDate.parse("2020-05-06"));
        traineeService.saveTrainee(trainee3);
        log.info("Saved trainee3: {}", trainee3);

        Trainer trainer1 = new Trainer();
        trainer1.setFirstName("John");
        trainer1.setLastName("Doe");
        trainer1.setUsername(trainer1.getUsername());
        trainer1.setPassword(trainer1.getPassword());
        trainer1.setSpecialization("Yoga");
        trainerService.saveTrainer(trainer1);
        log.info("Saved trainer: {}", trainer1);

        Trainer trainer2 = new Trainer();
        trainer2.setFirstName("John");
        trainer2.setLastName("Doe");
        trainer2.setUsername(trainer1.getUsername());
        trainer2.setPassword(trainer1.getPassword());
        trainer2.setSpecialization("BodyBuilding");
        trainerService.saveTrainer(trainer1);
        log.info("Saved trainer: {}", trainer2);

        Trainer trainer3 = new Trainer();
        trainer3.setFirstName("John");
        trainer3.setLastName("Doe");
        trainer3.setUsername(trainer3.getUsername());
        trainer3.setPassword(trainer3.getPassword());
        trainer3.setSpecialization("BodyBuilding");
        trainerService.saveTrainer(trainer3);
        log.info("Saved trainer: {}", trainer3);
    }
}