package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Controller
@RequestMapping("/v1")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/",produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity home() {
        logger.info("Home path: /v1/");
        return ResponseEntity.ok("{\"serviceName\": \"CinemaService\"}");
        //return "index";
    }
}
