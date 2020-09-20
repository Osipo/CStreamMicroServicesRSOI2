package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.models.CreateOrder;
import ru.osipov.deploy.models.OrderInfo;
import ru.osipov.deploy.models.UpdateOrder;
import ru.osipov.deploy.services.OrderService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService oService;

    private JwtTokenProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService os, JwtTokenProvider tokenProvider){
        this.oService = os;
        this.tokenProvider = tokenProvider;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getToken(@RequestHeader HttpHeaders headers, HttpServletRequest req) {
        logger.info("GET http://{}/v1/api/token", headers.getHost());
        return tokenProvider.getToken(req).toMap();
    }

    @GetMapping(path = "/{ID}",produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getById(@PathVariable(name = "ID", required = true) Long id){
        logger.info("GET /v1/orders/{}'",id);
        OrderInfo info = oService.getById(id);
        if(info.getStatus() == null){
            logger.info("Got empty entity -> not found!");
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping(path = "/sum/{S}",produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getBySum(@PathVariable(name = "S") Double sum){
        logger.info("GET /v1/orders/sum/{}'",sum);
        List<OrderInfo> res = oService.getBySum(sum);
        if(res.size() == 0){
            logger.info("Got empty entity -> not found!");
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "/status/{S}",produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getByStatus(@PathVariable(name = "S") String st){
        logger.info("GET /v1/orders/status/{}'",st);
        List<OrderInfo> res = oService.getByStatus(st);
        if(res.size() == 0){
            logger.info("Got empty entity -> not found!");
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(res);
    }


    @GetMapping(path = "/",produces = APPLICATION_JSON_UTF8_VALUE, params = "cdate")
    public ResponseEntity getByCDate(@RequestParam(name = "cdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        logger.info("GET /v1/orders/?cdate={}'",date);
        List<OrderInfo> res = oService.getByCreationDate(date);
        if(res.size() == 0){
            logger.info("Got empty entity -> not found!");
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "/", produces = APPLICATION_JSON_UTF8_VALUE, params = "ctime")
    public ResponseEntity getByCTime(@RequestParam(name = "ctime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time){
        logger.info("GET /v1/orders/?ctime={}'",time);
        List<OrderInfo> res = oService.getByCreationTime(time);
        if(res.size() == 0){
            logger.info("Got empty entity -> not found!");
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "/",produces = APPLICATION_JSON_UTF8_VALUE, params = "udate")
    public ResponseEntity getByUDate(@RequestParam(name = "udate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        logger.info("GET /v1/orders/?udate={}'",date);
        List<OrderInfo> res = oService.getByUpdatedDate(date);
        if(res.size() == 0){
            logger.info("Got empty entity -> not found!");
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping(path = "/create", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createOrder(@RequestBody @Valid CreateOrder o){
        logger.info("POST /v1/orders/create");
        URI uri = oService.createOrder(o);
        return ResponseEntity.created(uri).build();
    }

    //POST: /v1/orders/delete/{order_id}
    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/delete/{oid}"})
    public ResponseEntity deleteOrder(@PathVariable(name = "oid") Long id){
        logger.info("POST /v1/orders/delete/'{}'",id);
        OrderInfo info = oService.deleteOrder(id);
        if(info.getStatus() == null){
            logger.info("Got empty entity -> not found!");
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(info);
    }

    //PATCH: /v1/orders/update
    //protected: [only for Cinema_Admin]
    @PatchMapping(path ="/update", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateOrder(@RequestBody @Valid UpdateOrder o){
        logger.info("PATCH /v1/orders/update");
        OrderInfo res = oService.updateOrder(o);
        if(res.getStatus() == null){
            logger.info("Got empty entity -> not found!");
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(res);
    }
}
