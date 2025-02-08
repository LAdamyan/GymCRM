package org.gymCrm.hibernate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest
public class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    public void testOpenAPIConfig() {

        assertEquals("GymCRM API", openAPI.getInfo().getTitle());
        assertEquals("0.0.1", openAPI.getInfo().getVersion());
        assertEquals("Gym_CRM Application API", openAPI.getInfo().getDescription());

        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("BearerAuth"));
    }
}