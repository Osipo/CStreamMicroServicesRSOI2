package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.RoomInfo;
import ru.osipov.deploy.services.CinemaService;
import ru.osipov.deploy.services.RoomService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

//Access to the rooms in specified cinema set with its cinema_id (cid)
//If that cinema does not exsits then only empty list will be returned.
@RestController
@RequestMapping("/v1/cinemas/{cid}/rooms/")
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    private final RoomService roomService;

    @Autowired
    private HttpServletRequest req;

    @Autowired
    public RoomController(RoomService s){
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

    private Long cid() {
        Map<String, Long> variables = (Map<String, Long>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return variables.get("cid");
    }
}
