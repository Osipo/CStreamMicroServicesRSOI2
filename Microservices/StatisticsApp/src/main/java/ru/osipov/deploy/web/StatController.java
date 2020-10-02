package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

import ru.osipov.deploy.models.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1")
public class StatController {
    @GetMapping(path = "/getActions/{username}")
    public List<UserActions> getActions(@PathVariable(name = "username") String username){
        
        return null;
    }
    
    @GetMapping(path = "/getRequests/{service}/{controller}/{method}/{type}/{uid}")
    public List<UserRequests> getRequests(@PathVariable(name = "service") String service,@PathVariable(name = "controller") String controller,@PathVariable(name = "method") String method,@PathVariable(name = "type") String type,@PathVariable(name = "uid") Long uid){
        return null;
    }
    
    @PostMapping(path = "/createRequest")
    public ResponseEntity createRequest(){
        
        return null;
    }
    
    @GetMapping(path = "/createAction")
    public ResponseEntity createAction(){
        
        return null;
    }
}