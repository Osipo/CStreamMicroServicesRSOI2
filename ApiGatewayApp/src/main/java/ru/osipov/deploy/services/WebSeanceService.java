package ru.osipov.deploy.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.osipov.deploy.errors.ApiException;
import ru.osipov.deploy.models.CreateSeance;
import ru.osipov.deploy.models.SeanceInfo;
import ru.osipov.deploy.utils.LocalDateAdapter;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;

@Service
public class WebSeanceService {
    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Value("${service.seance.url}")
    protected String serviceUrl;
    private static final Logger logger = LoggerFactory.getLogger(WebSeanceService.class);

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class,new LocalDateAdapter()).create();

    public WebSeanceService(){}

    public SeanceInfo[] getAll(){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<SeanceInfo[]> response = restTemplate.exchange(serviceUrl+"/v1/seances",
                HttpMethod.GET, entity, SeanceInfo[].class);
        return response.getBody();
    }

    public SeanceInfo getByCidFid(Long cid, Long fid){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SeanceInfo> response;
        try{
            response = restTemplate.exchange(serviceUrl+"/v1/seances/"+cid+"/"+fid,HttpMethod.GET,entity,SeanceInfo.class);
        }
        catch(HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/seances/"+cid+"/"+fid, null);
        }
        return response.getBody();
    }

    public SeanceInfo[] getByCid(Long cid){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SeanceInfo[]> response;
        try{
            response = restTemplate.exchange(serviceUrl+"/v1/seances/"+cid,HttpMethod.GET,entity,SeanceInfo[].class);
        }
        catch(HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/seances/"+cid, null);
        }
        return response.getBody();
    }

    public URI createSeance(CreateSeance data){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(data),headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<URI> response;
        try{
            response = restTemplate.exchange(serviceUrl+"/v1/seances/create",HttpMethod.POST,entity,URI.class);
        }
        catch(HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/seances/create", null);
        }
        return response.getBody();
    }
}
