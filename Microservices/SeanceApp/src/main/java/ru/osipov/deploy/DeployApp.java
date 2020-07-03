package ru.osipov.deploy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;

@SpringBootApplication(scanBasePackages = "ru.osipov.deploy")
@EnableDiscoveryClient
public class DeployApp {
    public static void main(String[] args){
        SpringApplication.run(DeployApp.class,args);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider();
    }

}
