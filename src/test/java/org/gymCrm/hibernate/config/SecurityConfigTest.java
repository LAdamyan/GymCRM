//package org.gymCrm.hibernate.config;
//
//import org.gymCrm.hibernate.config.jwt.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//class SecurityConfigTest {
//
//    @Mock
//    private JwtAuthorizationFilter jwtAuthorizationFilter;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @Mock
//    private CustomLogoutHandler customLogoutHandler;
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
//    @Mock
//    private CustomAuthenticationProvider customAuthProvider;
//
//    @InjectMocks
//    private SecurityConfig securityConfig;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//    @Test
//    void securityFilterChain() throws Exception {
//        HttpSecurity http = mock(HttpSecurity.class);
//        AuthenticationManagerBuilder authBuilder = mock(AuthenticationManagerBuilder.class);
//        when(http.getSharedObject(AuthenticationManagerBuilder.class)).thenReturn(authBuilder);
//        when(http.csrf(any())).thenReturn(http);
//        when(http.authorizeHttpRequests(any())).thenReturn(http);
//        when(http.sessionManagement(any())).thenReturn(http);
//        when(http.addFilterBefore(any(JwtAuthenticationFilter.class), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(http);
//        when(http.addFilterBefore(eq(jwtAuthorizationFilter), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(http);
//        when(http.exceptionHandling(any())).thenReturn(http);
//        when(http.logout(any())).thenReturn(http);
//
//        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(http, authenticationManager, accessDeniedHandler, authenticationEntryPoint);
//
//        assertNotNull(securityFilterChain);
//        verify(http, times(1)).csrf(any());
//        verify(http, times(1)).authorizeHttpRequests(any());
//        verify(http, times(1)).sessionManagement(any());
//        verify(http, times(1)).addFilterBefore(any(JwtAuthenticationFilter.class), eq(UsernamePasswordAuthenticationFilter.class));
//        verify(http, times(1)).addFilterBefore(eq(jwtAuthorizationFilter), eq(UsernamePasswordAuthenticationFilter.class));
//        verify(http, times(1)).exceptionHandling(any());
//        verify(http, times(1)).logout(any());
//    }
//    @Test
//    void authenticationManager() throws Exception {
//        HttpSecurity http = mock(HttpSecurity.class);
//        AuthenticationManagerBuilder authBuilder = mock(AuthenticationManagerBuilder.class);
//        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
//
//        when(http.getSharedObject(AuthenticationManagerBuilder.class)).thenReturn(authBuilder);
//        when(authBuilder.authenticationProvider(customAuthProvider)).thenReturn(authBuilder);
//        when(authBuilder.build()).thenReturn(mockAuthManager);
//
//        AuthenticationManager authenticationManager = securityConfig.authenticationManager(http, customAuthProvider);
//
//        assertNotNull(authenticationManager);
//        verify(authBuilder, times(1)).authenticationProvider(customAuthProvider);
//    }
//
//    @Test
//    void passwordEncoder() {
//        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
//        assertNotNull(passwordEncoder);
//    }
//    @Test
//    void bruteForceProtectionService() {
//        BruteForceProtectionService bruteForceProtectionService = securityConfig.bruteForceProtectionService();
//        assertNotNull(bruteForceProtectionService);
//    }
//}