package ru.osipov.deploy.utils.tasks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ru.osipov.deploy.models.CreateCinema;
import ru.osipov.deploy.models.UpdateCinema;
import ru.osipov.deploy.web.ApiController;

import java.util.Arrays;

public class CheckQueueTask implements Runnable {

    @Value("${service.queue.url}")
    protected String qUrl;
    private final ApiController c;

    public CheckQueueTask(ApiController c){
        this.c = c;
    }

    @Override
    public void run() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Integer> response;
        response = restTemplate.exchange(qUrl+"/v1/queue/size", HttpMethod.GET,entity,Integer.class);
        Integer s = response.getBody();
        if(s != null && s > 0){
            ResponseEntity<UpdateCinema> r2;
            r2 = restTemplate.exchange(qUrl+"/v1/queue/item",HttpMethod.GET,entity, UpdateCinema.class);
            UpdateCinema d = r2.getBody();
            Long id = d.getCid();
            CreateCinema req = new CreateCinema(d.getName(),d.getCountry(),d.getCity(),d.getRegion(),d.getStreet(),d.getSeances());
            c.updateSeance(id,req);
            c.changeInterval();
        }
    }
}
