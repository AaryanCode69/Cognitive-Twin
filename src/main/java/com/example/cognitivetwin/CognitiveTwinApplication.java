package com.example.cognitivetwin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CognitiveTwinApplication {

    public static void main(String[] args) {
        SpringApplication.run(CognitiveTwinApplication.class, args);
    }

}
