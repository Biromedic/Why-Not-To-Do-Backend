package com.example.whynottodo.config;

import com.example.whynottodo.model.User;
import com.example.whynottodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class PasswordEncoderConfig {
    private final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return usernameOrEmail -> {
            System.out.println("Attempting to find user with username or email: " + usernameOrEmail);
            Optional<User> userOptional = repository.findByUsername(usernameOrEmail);
            if (userOptional.isEmpty()) {
                userOptional = repository.findByEmail(usernameOrEmail);
            }
            if (userOptional.isEmpty()) {
                System.out.println("User not found with username or email: " + usernameOrEmail);
                throw new UsernameNotFoundException("User not Found");
            }
            return userOptional.get();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }
}
