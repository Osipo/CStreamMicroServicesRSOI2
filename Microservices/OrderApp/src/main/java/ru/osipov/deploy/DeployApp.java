package ru.osipov.deploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;

@SpringBootApplication(scanBasePackages = "ru.osipov.deploy")
@EnableDiscoveryClient
public class DeployApp {
    public static void main(String[] args){
        SpringApplication.run(DeployApp.class,args);
    }

}
