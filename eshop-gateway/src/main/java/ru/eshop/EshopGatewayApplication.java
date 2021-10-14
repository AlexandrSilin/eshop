package ru.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EshopGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopGatewayApplication.class, args);
    }

}
