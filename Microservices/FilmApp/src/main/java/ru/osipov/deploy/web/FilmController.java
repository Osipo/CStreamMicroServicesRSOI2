package ru.osipov.deploy.web;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.models.CreateFilm;
import ru.osipov.deploy.models.FilmInfo;
import ru.osipov.deploy.services.FilmService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/v1/films")
public class FilmController {
    private final FilmService fService;
    private JwtTokenProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmController(FilmService fs, JwtTokenProvider tokenProvider){
        this.fService = fs;
        this.tokenProvider = tokenProvider;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getToken(@RequestHeader HttpHeaders headers, HttpServletRequest req) {
        logger.info("GET http://{}/v1/api/token", headers.getHost());
        return tokenProvider.getToken(req).toMap();
    }

    //GET: /v1/films/
    //(By path:: /v1/films -> getByRating() -> getAll())
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/"})
    public List<FilmInfo> getAll(){
        logger.info("getAll");
        logger.info("/v1/films");
        return fService.getAllFilms();
    }

    //GET: /v1/films/{film_id}
    //if no id was specified -> getAll() [/v1/films/, /v1/films]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/{id}"})
    public ResponseEntity getById(@PathVariable(name = "id", required = true) Long id) {
        logger.info("getById");
        logger.info("/v1/films/'{}'", id);
        FilmInfo f = null;
        try {
            f = fService.getFilmById(id);
        } catch (IllegalStateException e) {
            logger.info("not found. 404");
            return ResponseEntity.status(404).body("Film with id "+id+" not found.");
        }
        return ResponseEntity.ok(f);
    }

    //GET: /v1/films/genre/{genre_id}
    //If no genre_id was specified -> empty list.
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE,path = {"/genre/{gid}"})
    public List<FilmInfo> getByGid(@PathVariable(required = true, name = "gid") Long gid){
        return fService.getFilmsByGid(gid);
    }

    //POST: /v1/films/genre/{genre_id}
    //if no ngid was specified then -> badRequest()
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = {"/genre/{gid}"}, consumes = {TEXT_PLAIN_VALUE})
    public ResponseEntity updateGenre(@PathVariable(required = true, name = "gid") Long oldgid, @Valid @RequestBody String ngid){
        if(ngid == null || ngid.equals("")){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(fService.updateGenre(oldgid,Long.parseLong(ngid)));
    }


    //GET: /v1/films/name/{film_name}
    //If no any name was specified -> getAll() [GET: /v1/films/name, /v1/films/name/]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/name/{name}","/name/"})
    public ResponseEntity getAllByName(@PathVariable(required = false, name= "name") String name){
        logger.info("Get by name");
        logger.info("/v1/films/name/");
        List<FilmInfo> films;
        if(name == null || name.equals("")) {
            logger.info("Name was not specified. Get all.");
            films = fService.getAllFilms();
        }
        else {
            logger.info("/v1/films/name/'{}'",name);
            logger.info("Name is '{}'",name);
            films = new ArrayList<FilmInfo>();
            FilmInfo f = fService.getByName(name);
            if(f.getId() == -2L){
                logger.info("not found. 404");
                return ResponseEntity.status(404).body("Film with name = "+name+" was not found.");
            }
            films.add(fService.getByName(name));

        }
        logger.info("Count: "+films.size());
        return ResponseEntity.ok(films);
    }

    //GET: /v1/films?r='...'
    //If no any name was specified -> getAll() [GET: /v1/films]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<FilmInfo> getByRating(@RequestParam(required = false, name= "r") Short rating){
        logger.info("Get by rating");
        logger.info("/v1/films");
        List<FilmInfo> films;
        if(rating == null) {
            logger.info("Rating was not specified. Get all.");
            films = fService.getAllFilms();
        }
        else {
            logger.info("Rating = '{}'",rating);
            films = fService.getByRating(rating);
        }
        logger.info("Count: "+films.size());
        return films;
    }

    //POST: /v1/films/update/{film_name}?r='...'
    //if no parameter was specified -> badRequest()
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = {"/update/{fname}"})
    public ResponseEntity updateRating(@PathVariable(name = "fname") String fname, @RequestParam(required = true,name = "r",defaultValue = "-1") Short rating){
        logger.info("/v1/films/update/");
        logger.info("Rating = '{}'",rating);
        FilmInfo f = null;
        try{
            f = fService.updateFilmRating(fname,rating);
        }
        catch (IllegalStateException e){
//            logger.info("Return json object with error message...");
//            JsonObject obj = new JsonObject();
//            obj.addProperty("error",e.getMessage());
            String v = e.getMessage();
            if(v.contains("not exist")){
                return ResponseEntity.notFound().build();
            }
            else{
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(f);
    }

    //PATCH: /v1/films/{film_id}
    //If no film_id was specified -> badRequest()
    //If no any data in body was specified -> badRequest()
    @PatchMapping(produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE, path = "/{id}")
    public ResponseEntity updateFilm(@PathVariable(name = "id",required = true) Long id, @RequestBody @Valid CreateFilm data){
        FilmInfo f = null;
        try{
            f = fService.updateFilm(id,data);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(f);
    }

    //POST: /v1/films/delete/{film_name}
    //if no parameter was specified -> badRequest()
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE,path = "/delete/{fname}")
    public ResponseEntity deleteFilm(@PathVariable(name = "fname")String name){
        logger.info("/v1/films/delete/'{}'",name);
        FilmInfo f = null;
        try{
            f = fService.deleteFilm(name);
        }
        catch (IllegalStateException e){
            logger.info("Return 404 not found.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(f);
    }
}
