package ru.osipov.deploy.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.osipov.deploy.WebConfig;
import ru.osipov.deploy.errors.ApiException;
import ru.osipov.deploy.models.CreateCinema;
import ru.osipov.deploy.models.CreateSeance;
import ru.osipov.deploy.models.SeanceInfo;
import ru.osipov.deploy.models.UpdateCinema;
import ru.osipov.deploy.utils.LocalDateAdapter;

import java.net.ConnectException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@Service
public class WebSeanceService {
    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Value("${service.seance.url}")
    protected String serviceUrl;
    
    @Value("${service.queue.url}")
    protected String queueUrl;

    private String seanceToken;

    private static final Logger logger = LoggerFactory.getLogger(WebSeanceService.class);

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class,new LocalDateAdapter()).create();

    public WebSeanceService(){
        if(serviceUrl == null || queueUrl == null){
            this.queueUrl = "http://localhost:8888";
            this.serviceUrl = "http://localhost:1111";
            this.seanceToken = getToken();
        }
    }

    public String getToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization","Basic "+String.format("base64(%s:%s)", WebConfig.getAppKey(), WebConfig.getAppSecret()));//set basic auth between services.
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, String>> response;
        try{
            response = this.restTemplate.exchange(serviceUrl + "/v1/seances/token", HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, String>>() {});
        }
        catch (HttpClientErrorException e){
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/seances/token/", null);
        }
        return response.getBody().get("access_token");
    }

    @HystrixCommand(fallbackMethod = "getAll_fallback",   commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"), @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")})
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

    @SuppressWarnings("unused")
    public SeanceInfo[] getAll_fallback(){
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON_UTF8);
        throw new ApiException("Service unavailable.",new ConnectException(),500,h,"Service unavailable. Connection refused.",serviceUrl+"/v1/seances",null);
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

    @HystrixCommand(fallbackMethod = "createFallback",   commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"), @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")})
    public URI createSeance(CreateSeance data, CreateCinema d, Long id){//SECOND FOR FALLBACK.
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization","Basic "+seanceToken);//service token.
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
    
    @SuppressWarnings("unused")
    public URI createFallback(CreateSeance data, CreateCinema d,Long id){
         // HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        UpdateCinema item = new UpdateCinema(id,d.getName(),d.getCountry(),d.getCity(),d.getRegion(),d.getStreet(),d.getSeances());

        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(item),headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Void> response;
        response = restTemplate.exchange(queueUrl+"/v1/queue/insert",HttpMethod.POST,entity,Void.class);
        return URI.create("http://localhost:8080");
    }
}
