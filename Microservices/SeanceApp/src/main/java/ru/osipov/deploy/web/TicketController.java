package ru.osipov.deploy.web;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.models.SeanceInfo;
import ru.osipov.deploy.models.TicketInfo;
import ru.osipov.deploy.services.SeanceService;
import ru.osipov.deploy.services.TicketService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/tickets")
public class TicketController {

    @Qualifier("TicketProcedures")
    private final TicketService service;

    private JwtTokenProvider tokenProvider;

    private static final Logger logger = getLogger(SeanceController.class);

    @Autowired
    public TicketController(TicketService service, JwtTokenProvider tokenProvider){
        this.service = service;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE,path = "/price/{price}")
    public List<TicketInfo> getAllByDate(@PathVariable("price") Double price){
        logger.info("/v1/tickets/price/"+price);
        List<TicketInfo> result;
        if(price == null || price < 0){
            logger.info("Price was not specified. Get all.");
            result = service.getAllTickets();
        }
        else{
            logger.info("Price = '{}'",price);
            result = service.getAllTicketsByPrice(price);
        }
        return result;
    }

}
