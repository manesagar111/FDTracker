package com.example.fdtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FdTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FdTrackerApplication.class, args);
    }
}