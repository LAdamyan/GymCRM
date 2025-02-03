package org.gymCrm.hibernate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.gymCrm.hibernate")
public class AppConfig implements WebMvcConfigurer {

    private final CustomTransactionInterceptor customTransactionInterceptor;

    public AppConfig(CustomTransactionInterceptor customTransactionInterceptor) {
        this.customTransactionInterceptor = customTransactionInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering CustomTransactionInterceptor...");
        registry.addInterceptor(customTransactionInterceptor);
    }


}
