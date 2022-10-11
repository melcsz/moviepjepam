package com.example.moviepj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MoviepjApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviepjApplication.class, args);
    }

}
