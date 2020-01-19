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
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.CreateCinema;
import ru.osipov.deploy.models.UpdateCinema;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.Map;

@Service
public class WebCinemaService {
    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Value("${service.cinema.url}")
    protected String serviceUrl;

    @Value("${service.queue.url}")
    protected String queueUrl;

    private String cinemaToken;

    private static final Logger logger = LoggerFactory.getLogger(WebCinemaService.class);
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public WebCinemaService(){
        if(serviceUrl == null || queueUrl == null){
            this.queueUrl = "http://localhost:8888";
            this.serviceUrl = "http://localhost:2222";
            this.cinemaToken = getToken();
        }
    }

    public String getToken(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization","Basic "+String.format("base64(%s:%s)", WebConfig.getAppKey(), WebConfig.getAppSecret()));//set basic auth between services.
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, String>> response;
        try{
            response = restTemplate.exchange(serviceUrl + "/v1/cinemas/token", HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, String>>() {});
        }
        catch (HttpClientErrorException e){
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/cinemas/token/", null);
        }
        logger.info("Get seance token: '{}'",response.getBody().get("access_token"));
        return response.getBody().get("access_token");
    }


    @HystrixCommand(fallbackMethod = "getAll_fallback",   commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"), @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")})
    public CinemaInfo[] getAll(){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("my_other_key", "my_other_value");
        logger.info("cinema_token: "+cinemaToken);
        headers.set("Authorization","Basic "+cinemaToken);
        // HttpEntity<String>: To get result as String.
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<CinemaInfo[]> response = restTemplate.exchange(serviceUrl+"/v1/cinemas",
                HttpMethod.GET, entity, CinemaInfo[].class);
        return response.getBody();
    }

    @SuppressWarnings("unused")
    public CinemaInfo[] getAll_fallback(){
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON_UTF8);
        throw new ApiException("Service unavailable.",new ConnectException(),500,h,"Service unavailable. Connection refused.",serviceUrl+"/v1/cinemas",null);
    }


    public CinemaInfo getById(Long id){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        logger.info("cinema_token: "+cinemaToken);
        headers.set("Authorization","Basic "+cinemaToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CinemaInfo> response;
        try{
            response = restTemplate.exchange(serviceUrl+"/v1/cinemas/"+id,HttpMethod.GET,entity,CinemaInfo.class);
        }
        catch(HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/cinemas/"+id, null);
        }
        return response.getBody();
    }

    @HystrixCommand(fallbackMethod = "update_fallback")
    public CinemaInfo updateCinema(Long id, CreateCinema data){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("X-HTTP-Method-Override", "PATCH");
        logger.info("cinema_token: "+cinemaToken);
        headers.set("Authorization","Basic "+cinemaToken);
        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(data),headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<CinemaInfo> response;
        try{
            response = restTemplate.exchange(serviceUrl+"/v1/cinemas/"+id,HttpMethod.PATCH,entity,CinemaInfo.class);
        }
        catch (HttpClientErrorException e){
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/cinemas/"+id, null);
        }
        return response.getBody();
    }


    @SuppressWarnings("unused")
    public CinemaInfo update_fallback(Long id, CreateCinema data){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
       // headers.set("X-HTTP-Method-Override", "PATCH");

        UpdateCinema item = new UpdateCinema(id,data.getName(),data.getCountry(),data.getCity(),data.getRegion(),data.getStreet(),data.getSeances());

        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(item),headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Void> response;
        response = restTemplate.exchange(queueUrl+"/v1/queue/insert",HttpMethod.POST,entity,Void.class);
        return new CinemaInfo(-2l,data.getName(),data.getCountry(),data.getCity(),data.getRegion(),data.getStreet());
    }
}
