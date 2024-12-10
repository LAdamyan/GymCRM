package org.gymCrm.config;

import org.gymCrm.dao.TraineeDAO;
import org.gymCrm.dao.TrainerDAO;
import org.gymCrm.dao.TrainingDAO;
import org.gymCrm.dao.impl.InMemoryTraineeDAO;
import org.gymCrm.dao.impl.InMemoryTrainerDAO;
import org.gymCrm.dao.impl.InMemoryTrainingDAO;
import org.gymCrm.service.TraineeService;
import org.gymCrm.service.TrainerService;
import org.gymCrm.service.TrainingService;
import org.gymCrm.service.impl.TraineeServiceImpl;
import org.gymCrm.service.impl.TrainerServiceImpl;
import org.gymCrm.service.impl.TrainingServiceImpl;
import org.gymCrm.storage.InMemoryStorage;
import org.gymCrm.storage.StorageInitializer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

@Configuration
@ComponentScan(basePackages = "org.gymCrm.config")
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
    @Bean
    public TrainerService trainerService() {
        return new TrainerServiceImpl(trainerDAO(inMemoryStorage()));
    }

    @Bean
    public TraineeService traineeService() {
        return new TraineeServiceImpl(traineeDAO(inMemoryStorage()));
    }

    @Bean
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
