package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.models.CreateGenreR;
import ru.osipov.deploy.models.GenreInfo;
import ru.osipov.deploy.services.GenreService;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/genres")
public class GenreController {

    private final GenreService gService;

    private static final Logger logger = LoggerFactory.getLogger(GenreController.class);

    @Autowired
    public GenreController(GenreService gs){
        this.gService = gs;
    }


    //GET: /v1/genres/{genre_id}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/{id}"})
    public ResponseEntity getById(@PathVariable(name = "id", required = true) Long id){
        logger.info("getById");
        logger.info("/v1/genres/'{}'",id);
        GenreInfo g = null;
        try{
            g = gService.getGenreById(id);
        }
        catch (IllegalStateException e){
            logger.info("not found. 404");
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(g);
    }

    //GET: /v1/genres?name='...'
    //If no any name was specified -> getAll() [GET: /v1/genres, /v1/genres/]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={})
    public ResponseEntity getAllByName(@RequestParam(required = false, name= "name") String name){
        logger.info("getAllByName");
        logger.info("/v1/genres");
        List<GenreInfo> genres;
        if(name == null || name.equals("")) {
            logger.info("Name was not specified. Get all.");
            genres = gService.getAllGenres();
        }
        else {
            logger.info("/v1/genres?name='{}'",name);
            logger.info("Name is '{}'",name);
            genres = new ArrayList<GenreInfo>();
            GenreInfo g = gService.getByName(name);
            if(g.getId() == -2L){
                logger.info("not found. 404");
                return ResponseEntity.status(404).body("Genre with name = "+name+" was not found.");
            }

            genres.add(gService.getByName(name));
        }
        logger.info("Count: "+genres.size());
        return ResponseEntity.ok(genres);
    }

    //GET: /v1/genres/remarks?r='...'
    //if no remark was specified and all too, then -> getByRemarks(null)
    //  [GET: /v1/genres/remarks, /v1/genres/remarks?r=, /v1/genres/remarks?r=&&all=, /v1/genres/remarks?all, /v1/genres/remarks?all=]
    //if no remark was specified BUT all was, then -> getAll() [GET: /v1/genres/remarks?all=true, /v1/genres/remarks?r=&&all=true]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = "/remarks")
    public List<GenreInfo> getAllByRemarks(@RequestParam(required = false, name="r") String remarks, @RequestParam(required = false, name = "all", defaultValue = "false") Boolean all){
        List<GenreInfo> genres;
        logger.info("/v1/genres/remarks");
        if( (remarks == null || remarks.equals("")) && all){
            logger.info("Remarks is null but fetch all cause p_all = '{}' ",all);
            genres = gService.getAllGenres();
        }
        else if(remarks == null || remarks.equals("")){
            logger.info("Remarks is null. Fetch by remarks cause p_all = '{}'",all);
            genres = gService.getByRemarks(null);
        }
        else{
            logger.info("Remarks = '{}'",remarks);
            genres = gService.getByRemarks(remarks);
        }
        logger.info("Count: "+genres.size());
        return genres;
    }


    //POST: /v1/genres/update/{genre_name}?newName='...'
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE,path={"/update/{genre}"})
    public ResponseEntity updateGenreName(@PathVariable(name = "genre") String genre, @RequestParam(name = "newName") String name){
        if(name.equals("")){
            logger.info("Empty name = '{}'",name);
            return ResponseEntity.status(400).body("Parameter name was not specified.");
        }
        GenreInfo g = null;
        logger.info("/v1/genres/update/'{}'",genre);
        try {
            g = gService.updateGenre(genre, name);
        }
        catch(IllegalStateException e){
            String v = e.getMessage();
            if(v.contains("not exist")){
                return ResponseEntity.status(404).body(e.getMessage());
            }
            else{
                return ResponseEntity.status(400).body(e.getMessage());
            }
//            logger.info("Return json object with error message...");
//            JsonObject obj = new JsonObject();
//            obj.addProperty("error",e.getMessage());
            //return ResponseEntity.ok(obj.toString());
        }
        return ResponseEntity.ok(g);
    }

    //PATCH: /V1/genres/{genre_id}
    //If no any genre_id was specified -> badRequest()
    //If no any data was in body -> badRequest()
    @PatchMapping(consumes = APPLICATION_JSON_UTF8_VALUE,produces = APPLICATION_JSON_UTF8_VALUE,path = {"/{id}"})
    public ResponseEntity updateGenre(@PathVariable(name = "id", required = true) Long id, @RequestBody @Valid CreateGenreR data){
        GenreInfo g;
        try{
            g = gService.updateGenre(id,data);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(g);
    }

    //POST: /v1/genres/create
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = "/create")
    public ResponseEntity createGenre(@Valid @RequestBody CreateGenreR request) {
        logger.info("/v1/genres/create");
        final URI location = gService.createGenre(request);
        return ResponseEntity.created(location).build();
    }

    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = "/restore")
    public ResponseEntity restoreGenre(@Valid @RequestBody GenreInfo request){
        logger.info("/v1/genres/restore");
        final URI location = gService.restoreGenre(request);
        return ResponseEntity.created(location).build();
    }

    //POST: /v1/genres/delete/{genre_name}
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/delete/{genre}","/delete/"})
    public ResponseEntity deleteGenre(@PathVariable(name = "genre")Long id){
        logger.info("/v1/genres/delete/'{}'",id);
        GenreInfo g = null;
        try{
            g = gService.deleteGenre(id);
        }
        catch (IllegalStateException e){
            logger.info("Return not found 404");
            //JsonObject obj = new JsonObject();
            //obj.addProperty("error",e.getMessage());
            //return ResponseEntity.ok(obj.toString());
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(g);
    }
}
