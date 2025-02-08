package org.gymCrm.hibernate.config;


import org.gymCrm.hibernate.config.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtUtil jwtUtil;
    private final LogoutHandler customLogoutHandler;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter, JwtUtil jwtUtil, LogoutHandler customLogoutHandler) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtUtil = jwtUtil;
        this.customLogoutHandler = customLogoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager,
                                                   CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint)
            throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtUtil);
        http
                . csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                antPathRequestMatcher("/swagger-ui/**"),
                                antPathRequestMatcher("/swagger-ui.html"),
                                antPathRequestMatcher("/v3/api-docs/**"),
                                antPathRequestMatcher("/swagger-resources/**"),
                                antPathRequestMatcher("/webjars/**"),
                                antPathRequestMatcher("/actuator/health"))
                        .permitAll()
                        .requestMatchers(
                                antPathRequestMatcher("/login", HttpMethod.POST.name()),
                                antPathRequestMatcher("/logout"))
                        .permitAll()
                        .requestMatchers(
                                antPathRequestMatcher("/trainees",HttpMethod.POST.name()),
                                antPathRequestMatcher("/trainers", HttpMethod.POST.name()))
                        .permitAll()
                        .anyRequest().authenticated()
                )
                . sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.setStatus(200);
                            response.getWriter().write("{\"message\": \"Logout successful\"}");
                        })
                        ));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AntPathRequestMatcher antPathRequestMatcher(String uri) {
        return new AntPathRequestMatcher(uri);
    }

    private AntPathRequestMatcher antPathRequestMatcher(String uri, String method) {
        return new AntPathRequestMatcher(uri, method);
    }

    @Bean
    public BruteForceProtectionService bruteForceProtectionService() {
        return new BruteForceProtectionService();
    }

}