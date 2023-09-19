package com.example.demoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.example.demoserver", "com.example.democore"})
@SpringBootApplication
public class DemoServerApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application,application-core");
        SpringApplication.run(DemoServerApplication.class, args);
    }

}
