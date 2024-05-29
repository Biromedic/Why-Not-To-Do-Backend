package com.example.whynottodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class WhyNotToDoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhyNotToDoApplication.class, args);
    }

}
