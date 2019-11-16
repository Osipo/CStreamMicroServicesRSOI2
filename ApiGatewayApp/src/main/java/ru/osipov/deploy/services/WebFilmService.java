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
import ru.osipov.deploy.models.CreateFilm;
import ru.osipov.deploy.models.FilmGenre;
import ru.osipov.deploy.models.FilmInfo;

import java.util.Arrays;
import java.util.List;

@Service
public class WebFilmService {
    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Value("${service.film.url}")
    protected String serviceUrl;
    private static final Logger logger = LoggerFactory.getLogger(WebFilmService.class);
    private static final Gson gson = new GsonBuilder().create();

    public WebFilmService(){}

    public FilmInfo[] getByRating(Short rating){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("my_other_key", "my_other_value");

        // HttpEntity<String>: To get result as String.
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<FilmInfo[]> response = restTemplate.exchange(serviceUrl+"/v1/films?r="+rating.toString(),
                HttpMethod.GET, entity, FilmInfo[].class);
        return response.getBody();
    }

    public FilmInfo[] getAll(){
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("my_other_key", "my_other_value");

        // HttpEntity<String>: To get result as json_String.
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<FilmInfo[]> response = restTemplate.exchange(serviceUrl+"/v1/films",
                HttpMethod.GET, entity, FilmInfo[].class);//As it returns raw JSONArray (without any top-level object.)
        //ResponseEntity<FilmInfo[]> response = restTemplate.getForEntity(serviceUrl+"/v1/films", FilmInfo[].class);
        return response.getBody();
    }

    public FilmInfo[] getByGid(Long gid){
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FilmInfo[]> response = restTemplate.exchange(serviceUrl+"/v1/films/genre/"+gid,
                HttpMethod.GET,entity,FilmInfo[].class);
        return response.getBody();
    }

    public FilmInfo getById(Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON_UTF8));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FilmInfo> response;
        try {
            response = restTemplate.exchange(serviceUrl + "/v1/films/" + id
                    , HttpMethod.GET, entity, FilmInfo.class);
        }catch (HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/films/"+id, null);
        }
        return response.getBody();
    }

    public FilmInfo[] changeGenre(Long old){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        headers.set("Content-Type",MediaType.TEXT_PLAIN_VALUE);
        HttpEntity<String> entity = new HttpEntity<String>("-1",headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FilmInfo[]> response = restTemplate.exchange(serviceUrl+"/v1/films/genre/"+old,
                HttpMethod.POST,entity,FilmInfo[].class);
        return response.getBody();
    }

    public FilmInfo[] changeGenre(Long old, Long nval){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        headers.set("Content-Type",MediaType.TEXT_PLAIN_VALUE);
        HttpEntity<String> entity = new HttpEntity<String>(nval.toString(),headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FilmInfo[]> response = restTemplate.exchange(serviceUrl+"/v1/films/genre/"+old,
                HttpMethod.POST,entity,FilmInfo[].class);
        return response.getBody();
    }

    public FilmInfo updateFilm(Long id, CreateFilm data){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(data)/*"{ id:+"data.getId()+"name:"+data.getName()+"rating:"+data.getRating()+"gid:"+data.getGid()+ "}"*/,headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<FilmInfo> response;
        try {
            response = restTemplate.exchange(serviceUrl + "/v1/films/" + id, HttpMethod.PATCH, entity, FilmInfo.class);
        }
        catch(HttpClientErrorException e){
            logger.info("Error message: '{}'",e.getMessage());
            logger.info("Error status: '{}'",e.getRawStatusCode());
            logger.info("Response: '{}'",e.getResponseBodyAsString());
            throw new ApiException(e.getMessage(), e, e.getRawStatusCode(), e.getResponseHeaders(),
                    e.getResponseBodyAsString(), serviceUrl+"/v1/films/"+id, null);
        }
        return response.getBody();
    }
}
