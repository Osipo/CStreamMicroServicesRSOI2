package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.models.*;
import ru.osipov.deploy.services.CUDRoomService;
import ru.osipov.deploy.services.CUDSeatService;
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
@RequestMapping("/v1/cinemas/{cid}/rooms")
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);



    private final CUDRoomService roomService;

    private final CUDSeatService seatService;

    @Autowired
    private HttpServletRequest req;

    @Autowired
    public RoomController(CUDRoomService s1, CUDSeatService s2){
        this.roomService = s1; this.seatService = s2;
    }

    //GET: /v1/cinemas/{cinema_id}/rooms
    //GET: /v1/cinemas/{cinema_id}/rooms/
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path="/")
    public List<RoomInfo> getAll(@PathVariable(name = "cid", required = true) Long cid){
        //long cid = cid();//cinema id is specified in path.
        logger.info("/v1/cinemas/'{}'/rooms/",cid);
        logger.info("Get all rooms in cinema with id =  '{}'",cid);
        return roomService.getByCid(cid);
    }

    //GET: /v1/cinemas/{cinema_id}/rooms/{room_id}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = "/{rid}")
    public ResponseEntity getById(@PathVariable(name = "cid", required = true) Long cid,@PathVariable(name = "rid",required = true) Long id){
        //long cid = cid();
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

    //GET: /v1/cinemas/{cinema_id}/rooms/category/{cat}
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path= "/category/{cat}")
    public List<RoomInfo> getByCategory(@PathVariable(name = "cid", required = true) Long cid,@PathVariable(name = "cat",required = false) String category){
        //long cid = cid();//cinema id is specified in path.
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
    public ResponseEntity createRoom(@PathVariable(name = "cid", required = true) Long cid,@Valid @RequestBody CreateRoom request, @RequestHeader HttpHeaders headers) {
        //long cid = cid();
        logger.info("/v1/cinemas/'{}'/rooms/create",cid);
        final URI location = roomService.createRoom(cid,request);
        return ResponseEntity.created(location).build();
    }

    //POST: /v1/cinemas/{cinema_id}/rooms/delete/{room_id}
    //POST: /v1/cinemas/{cinema_id}/rooms/delete/
    //PROTECTED [only for Cinema_Admin]
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/delete/{rid}","/delete/"})
    public ResponseEntity deleteRoom(@PathVariable(name = "cid", required = true) Long cid,@PathVariable(name = "rid")Long id, @RequestHeader HttpHeaders headers){
        //long cid = cid();
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
    public ResponseEntity updateRoom(@PathVariable(name = "cid", required = true) Long cid,@PathVariable(required = true, name = "rid") Long id, @RequestBody @Valid CreateRoom request, @RequestHeader HttpHeaders headers){
        RoomInfo c;
        //long cid = cid();
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

    //POST: /v1/cinemas/{cid}/rooms/{rid}/add
    //PROTECTED [only for Admin and Cinema_Admin]
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = "/{rid}/add")
    public ResponseEntity createSeat(@PathVariable(name = "cid", required = true) Long cid,
                                     @PathVariable(name = "rid", required = true)Long id,
                                     @Valid @RequestBody CreateSeat request,
                                     @RequestHeader HttpHeaders headers){
        logger.info("/v1/cinemas/'{}'/rooms/'{}'/add",cid,id);
        final URI location;
        try {
            location = seatService.createSeat(id, request);
            return ResponseEntity.created(location).build();
        }
        catch (IllegalStateException e){
            logger.info("Cannot add seat without room! BadRequest!");
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    //POST: /v1/cinemas/{cid}/rooms/{rid}/update/{sid}
    //PUBLIC [only for Admin and Cinema_Admin]
    @PatchMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = "/{rid}/update/{sid}")
    public ResponseEntity updateSeat(@PathVariable(name = "cid", required = true) Long cid,
                                     @PathVariable(name = "rid", required = true)Long id,
                                     @PathVariable(name = "sid", required = true) Long sid,
                                     @Valid @RequestBody CreateSeat request,
                                     @RequestHeader HttpHeaders headers){
        logger.info("/v1/cinemas/'{}'/rooms/'{}'/update/'{}'",cid,id,sid);
        final SeatInfo s;
        try {
            logger.info("Update seat with Id = '{}'",sid);
            s = seatService.updateSeat(sid, request);
        }
        catch (IllegalStateException e){
            logger.info("Not found 404.");
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(s);
    }

    //POST: /v1/cinemas/{cid}/rooms/{rid}/delete/{sid}
    //PROTECTED [only for Cinema_Admin]
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = "/{rid}/delete/{sid}")
    public ResponseEntity deleteSeat(@PathVariable(name = "cid", required = true) Long cid,
                                     @PathVariable(name = "rid", required = true)Long id,
                                     @PathVariable(name = "sid", required = true) Long sid,
                                     @RequestHeader HttpHeaders headers){
        logger.info("/v1/cinemas/'{}'/rooms/'{}'/delete/'{}'",cid,id,sid);
        final SeatInfo s;
        try {
            logger.info("Update seat with Id = '{}'",sid);
            s = seatService.deleteSeat(sid);
        }
        catch (IllegalStateException e){
            logger.info("Not found 404.");
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(s);
    }

    //PATCH: /v1/cinemas/{cid}/rooms/{rid}/edit/state
    @PatchMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = "/{rid}/edit/state")
    public ResponseEntity changeSeatsState(@PathVariable(name = "cid", required = true) Long cid,
                                           @PathVariable(name = "rid", required = true)Long id,
                                           @Valid @RequestBody List<ChangeSeatState> request,
                                           @RequestHeader HttpHeaders headers){
        logger.info("/v1/cinemas/'{}'/rooms/'{}'/edit/state",cid,id);
        final List<SeatInfo> s;
        try {
            logger.info("Update seat in room Id = '{}'",id);
            s = seatService.setNewState(request);
            return ResponseEntity.ok(s);
        }
        catch (IllegalStateException e){
            logger.info("One or more seat has been already booked.");
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
