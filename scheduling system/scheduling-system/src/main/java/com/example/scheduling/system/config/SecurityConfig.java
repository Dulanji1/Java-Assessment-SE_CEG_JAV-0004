package com.example.scheduling.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoder bean
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for simplicity
                .authorizeRequests()
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll() // Allow unauthenticated access to specific endpoints
                .requestMatchers("/api/v1/employee/**").permitAll() // Allow access to all endpoints under /api/v1/employee
                .requestMatchers("/api/tasks/**").permitAll() // Allow access to task endpoints
                .requestMatchers("/api/v1/mail/**").permitAll()
                .anyRequest().authenticated() // Require authentication for all other endpoints
                .and()
                .formLogin().disable(); // Disable default form login

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/swagger-ui/**"); // Ignore Swagger UI if you're using it
    }
}
