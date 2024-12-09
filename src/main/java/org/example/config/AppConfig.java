package org.example.config;

import org.example.dao.TraineeDAO;
import org.example.dao.TrainerDAO;
import org.example.dao.TrainingDAO;
import org.example.dao.impl.InMemoryTraineeDAO;
import org.example.dao.impl.InMemoryTrainerDAO;
import org.example.dao.impl.InMemoryTrainingDAO;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.service.impl.TraineeServiceImpl;
import org.example.service.impl.TrainerServiceImpl;
import org.example.service.impl.TrainingServiceImpl;
import org.example.storage.InMemoryStorage;
import org.example.storage.StorageInitializer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

@Configuration
@ComponentScan(basePackages = "org.example.config")
@PropertySource("classpath:application.yml")
public class AppConfig {

    @Bean
    @Lazy
    public InMemoryStorage inMemoryStorage() {
        return new InMemoryStorage();
    }

    @Bean
    @Lazy
    public TraineeDAO traineeDAO(InMemoryStorage storage) {
        return new InMemoryTraineeDAO(storage.getTraineeMap());
    }

    @Bean
    @Lazy
    public TrainerDAO trainerDAO(InMemoryStorage storage) {
        return new InMemoryTrainerDAO(storage.getTrainerMap());
    }

    @Bean
    @Lazy
    public TrainingDAO trainingDAO(InMemoryStorage storage) {
        return new InMemoryTrainingDAO(storage.getTrainingMap());
    }

    public TrainerService trainerService() {
        return new TrainerServiceImpl(trainerDAO(inMemoryStorage()));
    }

    public TraineeService traineeService() {
        return new TraineeServiceImpl(traineeDAO(inMemoryStorage()));
    }

    public TrainingService trainingService() {
        return new TrainingServiceImpl(trainingDAO(inMemoryStorage()));
    }

    @Bean
    @Lazy
    public StorageInitializer storageInitializer() {
        return new StorageInitializer(traineeService(), trainerService(), trainingService());
    }

    @Bean
    @SuppressWarnings(value = "deprecation")
    public static PropertyPlaceholderConfigurer properties() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        propertyPlaceholderConfigurer.setProperties(Objects.requireNonNull(yaml.getObject()));
        return propertyPlaceholderConfigurer;
    }
}
