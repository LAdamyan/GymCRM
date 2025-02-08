//package org.gymCrm.hibernate.config;
//
//import org.apache.catalina.security.SecurityConfig;
//import org.gymCrm.hibernate.config.jwt.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@EnableWebSecurity
//public class SecurityConfigTest {
//
//    @InjectMocks
//    private SecurityConfig securityConfig;
//
//    @Mock
//    private JwtAuthorizationFilter jwtAuthorizationFilter;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @Mock
//    private LogoutHandler customLogoutHandler;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private CustomAccessDeniedHandler accessDeniedHandler;
//
//    @Mock
//    private CustomAuthenticationEntryPoint authenticationEntryPoint;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
////    @Test
////    public void testSecurityFilterChain() throws Exception {
////        // Given
////        HttpSecurity http = mock(HttpSecurity.class);
////
////        // Simulate behavior of the security filter chain configuration
////        when(http.csrf(any())).thenReturn(http);
////        when(http.authorizeHttpRequests(any())).thenReturn(http);
////        when(http.sessionManagement(any())).thenReturn(http);
////        when(http.addFilterBefore(any(), any())).thenReturn(http);
////        when(http.exceptionHandling(any())).thenReturn(http);
////        when(http.logout(any())).thenReturn(http);
////        when(http.build()).thenReturn((DefaultSecurityFilterChain) mock(SecurityFilterChain.class));
////
////        // When
////        securityConfig.securityFilterChain(http, authenticationManager, accessDeniedHandler, authenticationEntryPoint);
////
////        // Then
////        verify(http).csrf(AbstractHttpConfigurer::disable);
////        verify(http).authorizeHttpRequests(any());
////        verify(http).sessionManagement(any());
////        verify(http).addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
////        verify(http).exceptionHandling(any());
////        verify(http).logout(any());
////    }
//
//    @Test
//    public void testPasswordEncoder() {
//
//        PasswordEncoder encoder = securityConfig.passwordEncoder();
//
//        assertTrue(encoder instanceof BCryptPasswordEncoder);
//    }
//
//    @Test
//    public void testAuthenticationManager() throws Exception {
//
//        AuthenticationConfiguration authenticationConfiguration = mock(AuthenticationConfiguration.class);
//        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);
//
//        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);
//
//        assertEquals(authenticationManager, result);
//    }
//
//    @Test
//    public void testBruteForceProtectionService() {
//        // When
//        BruteForceProtectionService service = securityConfig.bruteForceProtectionService();
//
//        // Then
//        assertNotNull(service);
//    }
//}