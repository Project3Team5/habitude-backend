package com.habitude;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
        // Exclude Spring Security’s auto-configuration
        // → This disables all of Spring Boot’s default security setup
        //    so no login page, no authentication/authorization checks.
        // → Useful for rapid prototyping or when we want completely open
        //    endpoints while we build out our controllers.
        exclude = { SecurityAutoConfiguration.class }
)
public class HabitudeApplication {

    public static void main(String[] args) {
        // Bootstraps the entire Spring context and starts the embedded server
        SpringApplication.run(HabitudeApplication.class, args);
    }
}
