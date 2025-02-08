package org.gymCrm.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@EnableScheduling
@EnableJpaRepositories(basePackages = "org.gymCrm.hibernate.repo")
@EntityScan(basePackages = "org.gymCrm.hibernate.model")
@SpringBootApplication
public class GymCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymCrmApplication.class, args);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode("I4McD66bwD");
        System.out.println("Hashed password: " + hashedPassword);
        boolean matches = passwordEncoder.matches("0ZLecKL2Pw", "$2a$10$MIz.YfoWNaYBnp9Fv1ManOzI1h21/NKE2EAlHr6arwSGdOF.XjXeW");
        System.out.println(matches);

    }

}