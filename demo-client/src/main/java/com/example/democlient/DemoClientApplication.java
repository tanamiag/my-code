package com.example.democlient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.example.democlient", "com.example.democore"})
@SpringBootApplication
public class DemoClientApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application,application-core");
        SpringApplication.run(DemoClientApplication.class, args);
    }

}
