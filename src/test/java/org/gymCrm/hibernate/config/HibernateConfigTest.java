package org.gymCrm.hibernate.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HibernateConfigTest {

    @Test
    public void testDataSourceBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        DriverManagerDataSource dataSource = context.getBean(DriverManagerDataSource.class);

        assertNotNull(dataSource, "DataSource should not be null");
        assertNotNull(dataSource.getUrl(), "DataSource URL should not be null");
        assertNotNull(dataSource.getUsername(), "DataSource username should not be null");
    }

    @Test
    public void testSessionFactoryBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        LocalSessionFactoryBean sessionFactory = context.getBean(LocalSessionFactoryBean.class);

        assertNotNull(sessionFactory, "SessionFactory should not be null");
    }

    @Test
    public void testTransactionManagerBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);

        assertNotNull(transactionManager, "Transaction Manager should not be null");
    }
}