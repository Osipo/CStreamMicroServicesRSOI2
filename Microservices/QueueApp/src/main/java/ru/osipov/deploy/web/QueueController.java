package ru.osipov.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.osipov.deploy.models.UpdateCinema;
import ru.osipov.deploy.services.QueueService;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RequestMapping("/v1")
@RestController
public class QueueController {

    private final QueueService service;

    @Autowired
    public QueueController(QueueService service){
        this.service = service;
    }

    @PostMapping(path = {"/queue/insert"})
    public ResponseEntity insert(UpdateCinema data){
        this.service.push(data);
        return ResponseEntity.ok("Successfull added to working queue.");
    }

    @GetMapping(path = {"/queue/size"})
    public Integer getSize(){
        return this.service.size();
    }

    @GetMapping(path = {"/queue/item"})
    public UpdateCinema get(){
        return this.service.get();
    }

    @GetMapping(path = {"/queue/all"})
    public List<UpdateCinema> getAll(){
        Queue<UpdateCinema> q = this.service.getQueue();
        List<UpdateCinema> r = new ArrayList<>();
        for(UpdateCinema c : q){
            r.add(c);
        }
        return r;
    }
}
