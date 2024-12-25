package org.gymCrm.hibernate.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    public void testAppConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        assertNotNull(context, "ApplicationContext should not be null");

        assertNotNull(context.getBean(DriverManagerDataSource.class), "DataSource bean should be available");
        assertNotNull(context.getBean(LocalSessionFactoryBean.class), "SessionFactory bean should be available");
        assertNotNull(context.getBean(PlatformTransactionManager.class), "TransactionManager bean should be available");
    }
}