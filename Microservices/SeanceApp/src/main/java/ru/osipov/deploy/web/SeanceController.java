package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.models.CreateSeance;
import ru.osipov.deploy.models.SeanceInfo;
import ru.osipov.deploy.services.SeanceService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/seances")
public class SeanceController {
    private final SeanceService seanceService;

    private static final Logger logger = getLogger(SeanceController.class);

    @Autowired
    public SeanceController(SeanceService service){
        this.seanceService = service;
    }

    //GET: /v1/seances?date=yyyy-MM-dd
    //if no any date was specified -> getAll() [GET: /v1/seances]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<SeanceInfo> getAllByDate(@RequestParam(required = false, name = "date") String date){
        logger.info("/v1/seances");
        List<SeanceInfo> result;
        if(date == null || date.equals("")){
            logger.info("Date was not specified. Get all.");
            result = seanceService.getAllSeances();
        }
        else{
            logger.info("Date = '{}'",date);
            result = seanceService.getSeancesByDate(date);
        }
        return result;
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = {"/{id}","/"})
    public List<SeanceInfo> getAllByCid(@PathVariable(required = false, name = "id") Long cid){
        List<SeanceInfo> result;
        if(cid == null || cid == 0){
            logger.info("Cinema_id was not specified. Get all.");
            result = seanceService.getAllSeances();
        }
        else{
            logger.info("Cinema_id = '{}'",cid);
            result = seanceService.getSeancesInCinema(cid);
        }
        return result;
    }

    //GET: /v1/seances/dates?d1=YYYY-mm-dd && d2=YYYY-mm-dd
    //if no any date was specified -> getAll() [GET: /v1/seances]
    //if only second date was specified -> getAllByDateBefore() [GET: /v1/seanes/date?d2='...']
    //if only first date was specified -> getAllByDate() [GET: /v1/seances/date?d1='...']
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path="/date")
    public List<SeanceInfo> getAllByDateBetween(@RequestParam(required = false,name="d1") String d1, @RequestParam(required = false, name="d2") String d2){
        logger.info("/v1/seances/date");
        List<SeanceInfo> result;
        if((d1 == null ||  d1.equals("")) && (d2 == null || d2.equals(""))){
            logger.info("No any date was specified. Get all.");
            result = seanceService.getAllSeances();
        }
        else if(d1 == null || d1.equals("")){
            logger.info("Starting date was not specified. Get all seances til second date.");
            logger.info("Second date = '{}'",d2);
            result = seanceService.getSeancesByDateBefore(d2);
        }
        else if(d2 == null || d2.equals("")){
            logger.info("Ending date was not specified. Get all seances by starting date.");
            logger.info("Date = '{}'",d1);
            result = seanceService.getSeancesByDate(d1);
        }
        else{
            logger.info("Get all including from");
            logger.info("Date 1 = '{}' to Date 2 = '{}' including",d1,d2);
            result = seanceService.getSeancesByDateBetween(d1,d2);
        }
        return result;
    }

    //POST: /v1/seances/delete?fid='..'
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = "/delete")
    public ResponseEntity deleteSeances(@RequestParam(required = true,name = "fid", defaultValue = "-1") Long fid){
        try{
            seanceService.deleteSeancesWithFilm(fid);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok("All deleted.");
    }

    @PatchMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = {"/{cid}/{fid}"})
    public ResponseEntity updateSeance(@PathVariable(required = true, name = "cid") Long cid, @PathVariable(required = true, name = "fid") Long fid, @RequestBody @Valid CreateSeance request){
        SeanceInfo s;
        try{
            s = seanceService.updateSeance(cid,fid,request);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(s);
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = {"/{cid}/{fid}"})
    public ResponseEntity getSeanceByID(@PathVariable(required = true, name = "cid") Long cid, @PathVariable(required = true, name = "fid") Long fid){
        SeanceInfo s = seanceService.getSeanceByFilmAndCinema(fid,cid);
        if(s.getCid() == -1L)
            return ResponseEntity.status(404).body("There are no any seance with cinema "+cid+"and film "+fid);
        return ResponseEntity.ok(s);
    }

    //POST: /v1/seances/create
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, path = "/create")
    public ResponseEntity createSeance(@RequestBody @Valid CreateSeance data){
        logger.info("/v1/seances/create");
        final URI url = seanceService.createSeance(data);
        return ResponseEntity.created(url).build();
    }
}
