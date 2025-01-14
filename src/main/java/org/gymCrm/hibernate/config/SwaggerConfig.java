package org.gymCrm.hibernate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.gymCrm.hibernate.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointInfo())
                .pathMapping("/");
    }

    private ApiInfo apiEndPointInfo() {
        return new ApiInfoBuilder()
                .title("Application Spring Rest API")
                .description("Gym_CRM Application API")
                .version("0.0.1")
                .build();
    }
}
