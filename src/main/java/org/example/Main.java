package org.example;

import lombok.val;
import org.example.config.AppConfig;
import org.example.storage.InMemoryStorage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {
        val context = new AnnotationConfigApplicationContext(AppConfig.class);
        InMemoryStorage storage = context.getBean(InMemoryStorage.class);
        storage.getTraineeMap().forEach((k, v) -> System.out.println(k + ": " + v));
        storage.getTrainerMap().forEach((k, v) -> System.out.println(k + ": " + v));
        storage.getTrainingMap().forEach((k, v) -> System.out.println(k + ": " + v));
        context.close();
    }
}