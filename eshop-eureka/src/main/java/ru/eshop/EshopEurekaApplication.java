package ru.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EshopEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopEurekaApplication.class, args);
    }

}
