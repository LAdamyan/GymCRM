package org.gymCrm.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.endpoint.CustomMetrics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.geo.CustomMetric;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication(scanBasePackages = "org.gymCrm.hibernate")
public class GymCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymCrmApplication.class, args);


    }

}