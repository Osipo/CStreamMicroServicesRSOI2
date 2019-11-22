package ru.osipov.deploy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import ru.osipov.deploy.services.WebFilmService;
import ru.osipov.deploy.services.WebGenreService;
import ru.osipov.deploy.web.ApiController;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = true)
public class GatewayApp {
    public static void main(String[] args){
        SpringApplication.run(GatewayApp.class,args);
    }

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
