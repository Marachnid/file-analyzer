package com.analyzer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * main class for spring-boot application - initializes spring-boot web environment 
 */
@SpringBootApplication
public class SpringBootStartup {

    /**
     * initializes springboot startup
     * @param args cla (none currently used)
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootStartup.class, args);
    }
}
