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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.osipov.deploy.errors.ApiException;
import ru.osipov.deploy.models.CreateGenreR;
import ru.osipov.deploy.models.GenreInfo;

import java.net.ConnectException;
import java.net.URI;
import java.util.Arrays;
@Service
public class WebGenreService {
    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Value("${service.genre.url}")
    protected String serviceUrl;
    private static final Logger logger = LoggerFactory.getLogger(WebFilmService.class);
    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    public WebGenreService(){}

    public WebGenreService(String url){
        if(url.contains("http"))
            this.serviceUrl = url;
        else
            this.serviceUrl = "http://"+serviceUrl;
    }

    @HystrixCommand(fallbackMethod = "getByName_fallback",  commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"), @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")})
    public GenreInfo[] getByName(String name){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("my_other_key", "my_other_value");

        // HttpEntity<String>: To get result as String.
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        // RestTemplate
        //RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<GenreInfo[]> response;
        try {
            response = this.restTemplate.exchange(serviceUrl + "/v1/genres?name=" + name,
                    HttpMethod.GET, entity, GenreInfo[].class);
        }
        catch(HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/genres?name="+name, null);
        }
        return response.getBody();
    }

    @SuppressWarnings("unused")
    public GenreInfo[] getByName_fallback(String name){
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON_UTF8);
        throw new ApiException("Service unavailable.",new ConnectException(),500,h,"Service unavailable. Connection refused.",serviceUrl+"/v1/genres?name="+name,null);
    }

    @HystrixCommand(fallbackMethod = "getAll_fallback",   commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"), @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")})
    public GenreInfo[] getAll(){
            // HttpHeaders
            HttpHeaders headers = new HttpHeaders();

            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
            // Request to return JSON format
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.set("my_other_key", "my_other_value");

            // HttpEntity<String>: To get result as String.
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            // RestTemplate
            //RestTemplate restTemplate = new RestTemplate();

            // Send request with GET method, and Headers.
            ResponseEntity<GenreInfo[]> response = this.restTemplate.exchange(serviceUrl+"/v1/genres",
                    HttpMethod.GET, entity, GenreInfo[].class);
            return response.getBody();
    }

    @SuppressWarnings("unused")
    public GenreInfo[] getAll_fallback(){
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON_UTF8);
        throw new ApiException("Service unavailable.",new ConnectException(),500,h,"Service unavailable. Connection refused.",serviceUrl+"/v1/genres",null);
    }


    public GenreInfo getById(Long id){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("my_other_key", "my_other_value");

        //HttpEntity<String>: To get result as String.
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        // RestTemplate
        //.RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<GenreInfo> response;
        try {
            response = this.restTemplate.exchange(serviceUrl + "/v1/genres/" + id,
                    HttpMethod.GET, entity, GenreInfo.class);
        }catch (HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/genres/"+id, null);
        }
        return response.getBody();
        // Send request with GET method, and Headers.
        //GenreInfo response = restTemplate.getForObject(serviceUrl+"/v1/genres/"+id, GenreInfo.class);
    }

    @HystrixCommand(fallbackMethod = "deleteFallback",   commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"), @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")})
    public GenreInfo delete(Long genre){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        //RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GenreInfo> response;
        try {
            response = this.restTemplate.exchange(serviceUrl + "/v1/genres/delete/" + genre
                    , HttpMethod.POST, entity, GenreInfo.class);
        }catch (HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());

            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/genres/delete/"+genre, null);
        }
        return response.getBody();
    }

    @SuppressWarnings("unused")
    public GenreInfo deleteFallback(Long genre){
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON_UTF8);
        throw new ApiException("Service unavailable.",new ConnectException(),500,h,"Service unavailable. Connection refused.",serviceUrl+"/v1/genres",null);
    }

    /*If genre service successfull delete genre, but FilmService is down, then insert deleted genre and cancel all changes.*/
    public URI restoreGenre(GenreInfo genre){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(genre),headers);
        ResponseEntity<Void> response = this.restTemplate.exchange(serviceUrl+"/v1/genres/restore",
                HttpMethod.POST,entity,Void.class);

        return response.getHeaders().getLocation();
    }

    public URI createGenre(CreateGenreR data){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(data),headers);
        // RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = this.restTemplate.exchange(serviceUrl+"/v1/genres/create",
                HttpMethod.POST,entity,Void.class);

        return response.getHeaders().getLocation();
    }
}
