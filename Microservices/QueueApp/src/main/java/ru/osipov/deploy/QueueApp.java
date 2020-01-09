package ru.osipov.deploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QueueApp {
    public static void main(String[] args){
        SpringApplication.run(QueueApp.class,args);
    }
}