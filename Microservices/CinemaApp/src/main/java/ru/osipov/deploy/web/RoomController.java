package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.CreateCinema;
import ru.osipov.deploy.models.CreateRoom;
import ru.osipov.deploy.models.RoomInfo;
import ru.osipov.deploy.services.CUDRoomService;
import ru.osipov.deploy.services.RoomService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

//Access to the rooms in specified cinema set with its cinema_id (cid)
//If that cinema does not exsits then only empty list will be returned.
@RestController
@RequestMapping("/v1/cinemas/{cid}/rooms/")
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    private final CUDRoomService roomService;

    @Autowired
    private HttpServletRequest req;

    @Autowired
    public RoomController(CUDRoomService s){
        this.roomService = s;
    }

    //GET: /v1/cinemas/{cinema_id}/rooms
    //GET: /v1/cinemas/{cinema_id}/rooms/
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<RoomInfo> getAll(){
        long cid = cid();//cinema id is specified in path.
        logger.info("/v1/cinemas/'{}'/rooms/",cid);
        logger.info("Get all rooms in cinema with id =  '{}'",cid);
        return roomService.getByCid(cid);
    }

    //GET: /v1/cinemas/{cinema_id}/rooms/{room_id}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = "/{rid}")
    public ResponseEntity getById(@PathVariable(name = "rid",required = true) Long id){
        long cid = cid();
        logger.info("getById");
        logger.info("/v1/cinemas/'{}'/rooms/'{}'",cid,id);
        RoomInfo r = null;
        try{
            r = roomService.getRoomById(id);
        }
        catch (IllegalStateException e){
            logger.info("not found. 404");
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(r);
    }

    //GET: /v1/cinemas/{cinema_id}/rooms/?category=''
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path= "")
    public List<RoomInfo> getByCategory(@RequestParam(name = "category",required = true) String category){
        long cid = cid();//cinema id is specified in path.
        logger.info("/v1/cinemas/'{}'/rooms/?category='{}'",cid,category);
        if(category == null || category.equals("")) {
            logger.info("No category was specified. Return all!");
            return roomService.getByCid(cid);
        }
        else{
            return roomService.getByCategory(category);
        }
    }

    //POST: /v1/cinemas/{cid}/rooms/create
    //PROTECTED [only for Admin and Cinema_Admin]
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = "/create")
    public ResponseEntity createRoom(@Valid @RequestBody CreateRoom request, @RequestHeader HttpHeaders headers) {
        long cid = cid();
        logger.info("/v1/cinemas/'{}'/rooms/create",cid);
        final URI location = roomService.createRoom(cid,request);
        return ResponseEntity.created(location).build();
    }

    //POST: /v1/cinemas/{cinema_id}/rooms/delete/{room_id}
    //POST: /v1/cinemas/{cinema_id}/rooms/delete/
    //PROTECTED [only for Admin and Cinema_Admin]
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/delete/{rid}","/delete/"})
    public ResponseEntity deleteRoom(@PathVariable(name = "rid")Long id, @RequestHeader HttpHeaders headers){
        long cid = cid();
        logger.info("/v1/cinemas/'{}'/rooms/delete/'{}'",cid,id);
        RoomInfo g = null;
        try{
            g = roomService.deleteRoom(id);
        }
        catch (IllegalStateException e){
            logger.info("Return not found 404");
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(g);
    }


    //PATCH: /v1/cinemas/{cinema_id}/rooms/{room_id}
    //PROTECTED [only for Admin and Cinema_Admin]
    @PatchMapping(consumes = APPLICATION_JSON_UTF8_VALUE,produces = APPLICATION_JSON_UTF8_VALUE, path = {"/{rid}"})
    public ResponseEntity updateRoom(@PathVariable(required = true, name = "rid") Long id, @RequestBody @Valid CreateRoom request, @RequestHeader HttpHeaders headers){
        RoomInfo c;
        long cid = cid();
        logger.info("updateRoom");
        logger.info("/v1/cinemas/'{}'/rooms/'{}'",cid,id);
        try{
            logger.info("Id = '{}'",id);
            c = roomService.updateRoom(id,request);
        }
        catch (IllegalStateException e){
            logger.info("not found. 404");
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(c);
    }

    private Long cid() {
        Map<String, Long> variables = (Map<String, Long>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return variables.get("cid");
    }
}
