package org.gymCrm.hibernate.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppInitTest {

    private final AppInit appInit = new AppInit();

    @Test
    void testGetRootConfigClasses() {
        Class<?>[] rootConfigClasses = appInit.getRootConfigClasses();
        assertNull(rootConfigClasses, "Root configuration classes should be null");
    }

    @Test
    void testGetServletConfigClasses() {
        Class<?>[] servletConfigClasses = appInit.getServletConfigClasses();
        assertNotNull(servletConfigClasses, "Servlet configuration classes should not be null");
        assertEquals(1, servletConfigClasses.length, "There should be exactly one servlet configuration class");
        assertEquals(AppConfig.class, servletConfigClasses[0], "The servlet configuration class should be AppConfig");
    }

    @Test
    void testGetServletMappings() {
        String[] servletMappings = appInit.getServletMappings();
        assertNotNull(servletMappings, "Servlet mappings should not be null");
        assertEquals(1, servletMappings.length, "There should be exactly one servlet mapping");
        assertEquals("/", servletMappings[0], "The servlet mapping should be '/'");
    }
}