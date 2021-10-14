package ru.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class EshopConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopConfigServerApplication.class, args);
    }

}
