package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.models.*;
import ru.osipov.deploy.services.WebCinemaService;
import ru.osipov.deploy.services.WebFilmService;
import ru.osipov.deploy.services.WebGenreService;
import ru.osipov.deploy.services.WebSeanceService;
import ru.osipov.deploy.utils.Paginator;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/api")
public class ApiController {

    protected WebFilmService filmService;

    protected WebGenreService genreService;

    protected WebCinemaService cinemaService;

    protected WebSeanceService seanceService;

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    public ApiController(WebFilmService fs, WebGenreService gs, WebCinemaService cs, WebSeanceService ss){
        this.filmService = fs;
        this.genreService = gs;
        this.cinemaService = cs;
        this.seanceService = ss;
    }


    //GET: /v1/api/home
    @GetMapping(path = {"/home"})
    public String home(){
        return "index";
    }

    //GET: /v1/api/films?r=number
    //GET: /v1/api/films?r=number&page=X&size=Y
    //GET: /v1/api/films?r=number&page=X[size = 1]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE,path="/films")
    public ResponseEntity getAllFilms(@RequestParam(required = false, name= "r") Short rating,
                                      @RequestParam(required = false, name = "page") Integer page,
                                      @RequestParam(required = false, name = "size") Integer size){
        FilmInfo[] f;
        if(rating == null){
            f = filmService.getAll();
        }
        else{
            f = filmService.getByRating(rating);
        }
        List<FilmGenre> result = new ArrayList<>();
        for (FilmInfo i : f){
            GenreInfo g = genreService.getById(i.getGid());
            FilmGenre fg = new FilmGenre(i.getId(),i.getName(),i.getRating(),g);
            result.add(fg);
        }
        if(page != null){
            if(size == null || size <= 0)//size was not specified. [set size = 1]
                size = 1;
            result = Paginator.getResult(result,page,size,page);
        }
        else if(size != null){//size specified but page not. [set page = 1]
            if(size <= 0)
                size = 1;
            result = Paginator.getResult(result,1,size,1);
        }
        return ResponseEntity.ok(result);
    }


    //GET: /v1/api/films/genre/{genre_id}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE,path = {"/films/genre/{gid}","/films/genre/"})
    public ResponseEntity getFilmsByGenreId(@PathVariable(required = true, name = "gid") Long gid,
                                            @RequestParam(required = false, name = "page") Integer page,
                                            @RequestParam(required = false, name = "size") Integer size){
        List<FilmGenre> result = new ArrayList<>();
        FilmInfo[]  f = filmService.getByGid(gid);
        GenreInfo g =  genreService.getById(gid);
        for(FilmInfo i : f){
            FilmGenre fg = new FilmGenre(i.getId(),i.getName(),i.getRating(),g);
            result.add(fg);
        }

        if(page != null){
            if(size == null || size <= 0)//size was not specified. [set size = 1]
                size = 1;
            result = Paginator.getResult(result,page,size,page);
        }
        else if(size != null){//size specified but page not. [set page = 1]
            if(size <= 0)
                size = 1;
            result = Paginator.getResult(result,1,size,1);
        }

        return ResponseEntity.ok(result);
    }

    //GET: /v1/api/films/{film_id}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = {"/films/{id}"})
    public ResponseEntity getFilmById(@PathVariable(name = "id",required = true) Long id){
        FilmInfo f = filmService.getById(id);
        GenreInfo g = genreService.getById(f.getGid());
        FilmGenre fg = new FilmGenre(f.getId(),f.getName(),f.getRating(),g);
        return ResponseEntity.ok(fg);
    }
    //PATCH: /v1/api/films/{film_id}
    @PatchMapping(produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE,path = {"/films/{id}"})
    public ResponseEntity updateFilm(@PathVariable(name = "id",required = true) Long id, @RequestBody @Valid CreateFilm data){
        GenreInfo g = genreService.getById(data.getGid());
        FilmInfo f = filmService.updateFilm(id,data);
        return ResponseEntity.ok(new FilmGenre(f.getId(),f.getName(),f.getRating(),g));
    }

    //GET: /v1/api/genres
    //GET: /v1/api/genres?page=X&size=Y
    //GET: /v1/api/genres?page=X
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE,path={"/genres","/genres/"})
    public ResponseEntity getAllGenres(@RequestParam(required = false, name = "page") Integer page,
                                       @RequestParam(required = false, name = "size") Integer size){
        List<GenreInfo> result = List.of(genreService.getAll());
        if(page != null){
            if(size == null || size <= 0)//size was not specified. [set size = 1]
                size = 1;
            result = Paginator.getResult(result,page,size,page);
        }
        else if(size != null){//size specified but page not. [set page = 1]
            if(size <= 0)
                size = 1;
            result = Paginator.getResult(result,1,size,1);
        }
        return ResponseEntity.ok(result);
    }


    //GET: /v1/api/genres/{genre_id}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE,path = "/genres/{id}")
    public ResponseEntity getGenreById(@PathVariable(required = true, name = "id") Long id){return ResponseEntity.ok(genreService.getById(id));}

    //POST: /v1/api/genres/delete/{genre_id}
    //This is also deletes old genre_id from films.
    //FIRST UPDATE.
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE,path = "/genres/delete/{id}")
    public ResponseEntity deleteGenre(@PathVariable(name = "id") Long genre){
        GenreInfo g = genreService.delete(genre);
        Long id = g.getId();
        filmService.changeGenre(id);
        return ResponseEntity.ok(g);
    }


    //POST: /v1/api/genres/create
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,path = {"/genres/create"},produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createGenre(@RequestBody @Valid CreateGenreR data){
        URI url = genreService.createGenre(data);
        Long nid = Long.parseLong(url.getPath().substring(url.getPath().lastIndexOf("/") + 1));
        return ResponseEntity.created(url).body("{\"id\":"+nid+"}");
    }


    //POST: /v1/api/films/genre/{genre_id}
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE,path={"/films/genre/{ogid}"})
    public ResponseEntity updateFilmGenre(@PathVariable(required = true, name = "ogid") Long ogid, @RequestBody @Valid NewGenre ngid){
        FilmInfo[] f = filmService.changeGenre(ogid,ngid.getVal());
        List<FilmGenre> result = new ArrayList<>();
        GenreInfo g = genreService.getById(ngid.getVal());
        for(FilmInfo i : f){
            result.add(new FilmGenre(i.getId(),i.getName(),i.getRating(),g));
        }
        return ResponseEntity.ok(result);
    }

    /*CINEMAS*/
    //GET: /v1/api/cinemas
    //GET: /v1/api/cinemas/{cinema_id}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/cinemas","/cinemas/","/cinemas/{id}"})
    public ResponseEntity getAllCinemas(@PathVariable(required = false, name = "id") Long id){
        if(id == null || id == 0){
            return ResponseEntity.ok(cinemaService.getAll());
        }
        else
            return ResponseEntity.ok(cinemaService.getById(id));
    }

    //GET: /v1/api/cinemas/{cinema_id}/seances
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/cinemas/{id}/seances"})
    public ResponseEntity getCinemaSeances(@PathVariable(required = true, name = "id") Long id){
        CinemaInfo c = cinemaService.getById(id);
        SeanceInfo[] seances = seanceService.getByCid(c.getId());
        return ResponseEntity.ok(new CinemaSeances(c,seances));
    }


    //GET: /v1/api/cinemas/{cinema_id}/seances/{seance_id}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = {"/cinemas/{cid}/seances/{fid}"})
    public ResponseEntity getSeance(@PathVariable(required = true, name = "cid") Long cid, @PathVariable(required = true, name = "fid") Long fid){
        CinemaInfo c = cinemaService.getById(cid);
        SeanceInfo seance = seanceService.getByCidFid(cid,fid);
        FilmInfo f = filmService.getById(seance.getFid());
        GenreInfo genre = genreService.getById(f.getGid());
        SeanceDescription result = new SeanceDescription(c.getName(),c.getCountry()+"  "+c.getCity(),f.getName(),genre.getName(),f.getRating(),seance.getDate());
        return ResponseEntity.ok(result);
    }

    //PATCH: /v1/api/cinemas/{cinema_id}
    //SECOND UPDATE.
    @PatchMapping(consumes = APPLICATION_JSON_UTF8_VALUE, path = {"/cinemas/{cid}"})
    public ResponseEntity updateSeance(@PathVariable(required = true, name = "cid") Long id, @RequestBody @Valid CreateCinema request){
        final CinemaInfo c = cinemaService.updateCinema(id,request);
        CreateSeance[] sl = request.getSeances();
        if(sl != null && sl.length > 0)
            for(CreateSeance s : sl){
                seanceService.createSeance(s);
            }
        SeanceInfo[] updated = seanceService.getByCid(c.getId());
        return ResponseEntity.ok(new CinemaSeances(c,updated));
    }

    /*SEANCES */
    //GET: /v1/api/seances
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/seances"})
    public ResponseEntity getAllSeances(){
        return ResponseEntity.ok(seanceService.getAll());
    }
}
