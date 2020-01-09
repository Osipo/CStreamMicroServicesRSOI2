package ru.osipov.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.osipov.deploy.models.CreateCinema;
import ru.osipov.deploy.services.QueueService;

@RequestMapping("/v1")
@RestController
public class QueueController {

    private final QueueService service;

    @Autowired
    public QueueController(QueueService service){
        this.service = service;
    }

    @PostMapping(path = {"/queue/insert"})
    public ResponseEntity insert(CreateCinema data){
        this.service.push(data);
        return ResponseEntity.ok("Successfull added to working queue.");
    }

    @GetMapping(path = {"/queue/size"})
    public ResponseEntity getSize(){
        return ResponseEntity.ok(this.service.size());
    }

    @GetMapping(path = {"/queue/item"})
    public CreateCinema get(){
        return this.service.get();
    }
}
