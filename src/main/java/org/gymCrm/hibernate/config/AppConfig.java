package org.gymCrm.hibernate.config;

import org.springframework.context.annotation.*;


@Configuration
@ComponentScan(basePackages = "org.gymCrm.hibernate")
@Import(HibernateConfig.class)
public class AppConfig {



}
