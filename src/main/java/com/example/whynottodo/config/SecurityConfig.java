package com.example.whynottodo.config;

import com.example.whynottodo.Auth.JwtAuthenticationFilter;
import com.example.whynottodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        try {
            AuthenticationManagerBuilder authenticationManagerBuilder =
                    httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

            authenticationManagerBuilder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);

            AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

            httpSecurity.csrf(httpSecurityCsrfConfigurer ->
                            httpSecurityCsrfConfigurer.ignoringRequestMatchers("/api/user/**")
                                    .ignoringRequestMatchers("/api/tasks/**")
                                    .ignoringRequestMatchers("/api/admin/**"))
                    .authenticationManager(authenticationManager)
                    .sessionManagement(sessionManagementConfigurer ->
                            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("/api/admin").hasRole("ADMIN")
                                    .requestMatchers("api/moderator/**").hasRole("MODERATOR")
                                    .requestMatchers("/api/user/**").permitAll()
                                    .requestMatchers("/api/tasks/**").authenticated()
                                    .anyRequest().authenticated())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return httpSecurity.build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error in filterChain: " + e.getMessage());
        }
    }
}
