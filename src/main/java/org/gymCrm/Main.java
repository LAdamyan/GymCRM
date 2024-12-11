package org.gymCrm;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.config.AppConfig;
import org.gymCrm.model.Trainee;
import org.gymCrm.model.Trainer;
import org.gymCrm.service.TraineeService;
import org.gymCrm.service.TrainerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);

        Trainee trainee1 = new Trainee();
        trainee1.setFirstName("Lilit");
        trainee1.setLastName("Adamyan");
        traineeService.saveTrainee(trainee1);
        System.out.println("Trainee1 created :"+ trainee1);

        Trainee trainee2 = new Trainee();
        trainee2.setFirstName("Lilit");
        trainee2.setLastName("Adamyan");
        traineeService.saveTrainee(trainee2);
        System.out.println("Trainee2 created :"+ trainee2);

        Trainer trainer = new Trainer();
        trainer.setFirstName("Lilit");
        trainer.setLastName("Adamyan");
        trainerService.saveTrainer(trainer);
        System.out.println("Trainer created :"+ trainer);

        traineeService.deleteTrainee(2);
        System.out.println("Trainee2 is deleted");

        Trainee trainee3 = new Trainee();
        trainee3.setFirstName("Lilit");
        trainee3.setLastName("Adamyan");
        traineeService.saveTrainee(trainee3);
        System.out.println("Trainee3 created :"+ trainee3);
    }

}